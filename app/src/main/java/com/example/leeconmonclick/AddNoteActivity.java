package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.leerconmonclick.util.Note;

public class AddNoteActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth db = FirebaseAuth.getInstance();
    private Bundle data;
    private EditText titleNote,descriptionNote;
    private TextView tittleActivityAddNote;
    private Button saveNote;
    private long date;
    private String userCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        titleNote = findViewById(R.id.editTextTextPersonName5);
        descriptionNote = findViewById(R.id.editTextTextPersonName6);
        saveNote = findViewById(R.id.savenoteBtn);
        ArrayList<Note> notasNews = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = db.getCurrentUser();

        data = getIntent().getExtras();
        if (data.getBoolean("modeEdit")){
            try {
                modeEditOn();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userCollection = user.getEmail();
                String[] parts = userCollection.split("@");
                userCollection = parts[0];


                databaseReference.child("Users").child(userCollection).child("notas").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot){

                        databaseReference.child("Users").child(userCollection).child("notas").removeValue();
                        int cont = 0;
                        for(DataSnapshot objDataSnapshot : dataSnapshot.getChildren()){
                            Note note = new Note();
                            note.setTitle((String) objDataSnapshot.child("title").getValue());
                            note.setDescription((String) objDataSnapshot.child("description").getValue());
                            note.setTime((Long) objDataSnapshot.child("time").getValue());
                            databaseReference.child("Users").child(userCollection).child("notas").child(cont+"").setValue(note);
                            cont++;
                        }

                        Note newNote = new Note();
                        newNote.setTitle(titleNote.getText().toString());
                        newNote.setDescription(descriptionNote.getText().toString());

                        if (data.getBoolean("modeEdit")){
                            removeNote();
                            newNote.setTime(date);
                        }else{
                            long createdTime = System.currentTimeMillis();
                            newNote.setTime(createdTime);
                        }
                        databaseReference.child("Users").child(userCollection).child("notas").child(cont+"").setValue(newNote).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                if (data.getBoolean("modeEdit")){
                                    Toast.makeText(getApplicationContext(),"Nota editada correctamente",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Nota guardada correctamente",Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                    }
                });

                finish();
            }
        });
    }

    private void removeNote() {

        databaseReference.child("Users").child(userCollection).child("notas").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for(DataSnapshot objDataSnapshot : dataSnapshot.getChildren()){
                    Long time = (Long) objDataSnapshot.child("time").getValue();
                    if(time == date){
                        objDataSnapshot.getRef().removeValue();
                    }
                }
            }
        });
    }

    private void modeEditOn() throws ParseException {

        tittleActivityAddNote = findViewById(R.id.tittleActivityAddNoteId);
        tittleActivityAddNote.setText("EDITAR NOTA");
        titleNote.setText(data.getString("tittle"));
        date = data.getLong("date");
        saveNote.setText("Editar");
        descriptionNote.setText(data.getString("description"));

    }

    public void goBack(View view){finish();}

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

}