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

import java.text.ParseException;
import java.util.ArrayList;

import es.leerconmonclick.util.utils.Note;

public class AddNoteActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Bundle data;
    private EditText titleNote,descriptionNote;
    private TextView titlePage;
    private Button saveNote;
    private long date;
    private String userCollection;
    private ConstraintLayout constraintLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Encontrar e inicializar los elementos de base de datos y de la actividad
        finElements();
        //Recuperar los setting del usuario
        getSettings();

        //Comprobar si nos encontramos en modo edición de un contenido y si es así, recupera los valores del contenido a modificar
        if (data.getBoolean("modeEdit")){
            try {
                modeEditOn();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    //Guarda la nota
    public void saveNote(View v){

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

    private void getSettings() {

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.personal_notes);

        databaseReference.child("Users").child(userCollection).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                switch (size) {
                    case "grande":
                        titlePage.setTextSize(30);
                        titleNote.setTextSize(30);
                        descriptionNote.setTextSize(30);
                        saveNote.setTextSize(30);
                        break;
                    case "normal":
                        titlePage.setTextSize(20);
                        titleNote.setTextSize(20);
                        descriptionNote.setTextSize(20);
                        saveNote.setTextSize(20);
                        break;
                    case "peque":
                        titlePage.setTextSize(10);
                        titleNote.setTextSize(10);
                        descriptionNote.setTextSize(10);
                        saveNote.setTextSize(10);
                        break;
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

    //Elimena la nota seleccionada
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

        TextView tittleActivityAddNote = findViewById(R.id.tittleActivityAddNoteId);
        tittleActivityAddNote.setText("EDITAR NOTA");
        titleNote.setText(data.getString("tittle"));
        date = data.getLong("date");
        saveNote.setText("Editar");
        descriptionNote.setText(data.getString("description"));

    }

    private void finElements(){


        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        data = getIntent().getExtras();

        titleNote = findViewById(R.id.editTextTextPersonName5);
        titlePage = findViewById(R.id.tittleActivityAddNoteId);
        descriptionNote = findViewById(R.id.editTextTextPersonName6);
        saveNote = findViewById(R.id.savenoteBtn);



        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        userCollection = user.getEmail();
        assert userCollection != null;
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();


    }

    public void goBack(View view){finish();}

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

}