package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        titleNote = findViewById(R.id.editTextTextPersonName5);
        descriptionNote = findViewById(R.id.editTextTextPersonName6);
        Button saveNote = findViewById(R.id.savenoteBtn);
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
                Note note = new Note();
                note.setTitle(titleNote.getText().toString());
                note.setDescription(descriptionNote.getText().toString());
                long createdTime = System.currentTimeMillis();
                note.setTime(createdTime);


                String userCollection = user.getEmail();
                String[] parts = userCollection.split("@");
                userCollection = parts[0];

                String finalUserCollection = userCollection;
                databaseReference.child("Users").child(userCollection).child("notas").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {

                        for(DataSnapshot objDataSnapshot : dataSnapshot.getChildren()){
                            Note notaNew = new Note();
                            notaNew.setTitle((String) objDataSnapshot.child("title").getValue());
                            notaNew.setDescription((String) objDataSnapshot.child("description").getValue());
                            notaNew.setTime((Long) objDataSnapshot.child("time").getValue());
                            notasNews.add(notaNew);
                        }
                        notasNews.add(note);
                        databaseReference.child("Users").child(finalUserCollection).child("notas").removeValue();
                        databaseReference.child("Users").child(finalUserCollection).child("notas").setValue(notasNews);
                    }
                });

                Toast.makeText(getApplicationContext(), "Nota guardada", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void modeEditOn() throws ParseException {

        tittleActivityAddNote = findViewById(R.id.tittleActivityAddNoteId);
        tittleActivityAddNote.setText("EDITAR NOTA");
        titleNote.setText(data.getString("tittle"));
        descriptionNote.setText(data.getString("description"));

    }

    public void goBack(View view){finish();}

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

}