package com.example.leeconmonclick.professional.leeconmonclick.professional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.leeconmonclick.ErrorActivity;
import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.leerconmonclick.util.AudioPlay;
import es.leerconmonclick.util.adapters.ListAdapterNotes;
import es.leerconmonclick.util.utils.Note;


public class NotesListActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ArrayList<Note> listNotes;
    private ListAdapterNotes listAdapterNotes;
    private RecyclerView recyclerView;
    private String userCollection;
    private TextView titlePage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_notes);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findElements();
        getSettings();
        readData();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(NotesListActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });

    }

    public void readData(){


        databaseReference.child("Users").child(userCollection).child("notas").addValueEventListener(new ValueEventListener() {
            @Override
            @SuppressLint("NotifyDataSetChanged")
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listNotes.removeAll(listNotes);

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Note noteExist = new Note();
                    String tittle = (String) dataSnapshot.child("title").getValue();
                    String description = (String) dataSnapshot.child("description").getValue();
                    Long date = (Long) dataSnapshot.child("time").getValue();
                    noteExist.setTitle(tittle);
                    noteExist.setDescription(description);
                    noteExist.setTime(date);
                    listNotes.add(noteExist);
                }
                listAdapterNotes = new ListAdapterNotes(NotesListActivity.this,listNotes);
                recyclerView.setAdapter(listAdapterNotes);
                listAdapterNotes.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setContentView(R.layout.activity_error2);
            }
        });

    }

    private void getSettings(){

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.personal_notes);

        databaseReference.child("Users").child(userCollection).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String size = snapshot.child("sett").child("0").getValue().toString();
                switch (size) {
                    case "grande":
                        titlePage.setTextSize(30);
                        break;
                    case "normal":
                        titlePage.setTextSize(20);
                        break;
                    case "peque":
                        titlePage.setTextSize(10);
                        break;
                }
                String dalto = snapshot.child("sett").child("1").getValue().toString();
                if(dalto.equals("tritanopia")){
                    constraintLayout.setBackgroundResource(R.color.background_tritano);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setContentView(R.layout.activity_error2);
            }
        });
    }

    private void findElements(){

        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(NotesListActivity.this));

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        titlePage = findViewById(R.id.titleTask);

        listNotes = new ArrayList<Note>();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();


    }

    public void goBack(View view){finish();}

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

    public void goAddNote (View v){
        Intent addNoteIntent = new Intent(this, AddNoteActivity.class);
        addNoteIntent.putExtra("modeEdit",false);
        startActivity(addNoteIntent);
    }

    @Override
    protected void onPause() {
        boolean valor = AudioPlay.isIsplayingAudio();
        if(valor){
            AudioPlay.stopAudio();
        }
        super.onPause();
    }


}