package com.example.leeconmonclick.professional.leeconmonclick.professional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import com.example.leeconmonclick.ErrorActivity;
import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.R;
import com.example.leeconmonclick.patient.CategorySelecctionActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    private TextView titlePage;
    private Button saveNote;
    private long date;
    private String userCollection;
    private StorageReference storageReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        storageReference = FirebaseStorage.getInstance().getReference();
        titleNote = findViewById(R.id.editTextTextPersonName5);
        titlePage = findViewById(R.id.tittleActivityAddNoteId);
        descriptionNote = findViewById(R.id.editTextTextPersonName6);
        saveNote = findViewById(R.id.savenoteBtn);
        ArrayList<Note> notasNews = new ArrayList<>();

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.personal_notes);

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

        user = db.getCurrentUser();
        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();

        databaseReference.child("Users").child(userCollection).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                if(size.equals("grande")){
                    titlePage.setTextSize(30);
                    titleNote.setTextSize(30);
                    descriptionNote.setTextSize(30);
                    saveNote.setTextSize(30);
                }else if(size.equals("normal")){
                    titlePage.setTextSize(20);
                    titleNote.setTextSize(20);
                    descriptionNote.setTextSize(20);
                    saveNote.setTextSize(20);
                }else if(size.equals("peque")){
                    titlePage.setTextSize(10);
                    titleNote.setTextSize(10);
                    descriptionNote.setTextSize(10);
                    saveNote.setTextSize(10);
                }
                String dalto = snapshot.child("sett").child("1").getValue().toString();
                if(dalto.equals("tritanopia")){
                    constraintLayout.setBackgroundResource(R.color.background_tritano);
                    saveNote.setBackgroundResource(R.drawable.button_style_tritano);
                    titleNote.setBackgroundResource(R.drawable.button_style_tritano);
                    descriptionNote.setBackgroundResource(R.drawable.button_style_tritano);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(AddNoteActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
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