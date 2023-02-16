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

import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import es.leerconmonclick.util.ListAdapterNotes;
import es.leerconmonclick.util.Note;


public class PersonalNotesActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ArrayList<Note> listNotes = new ArrayList<Note>();
    private ListAdapterNotes listAdapterNotes;
    private RecyclerView recyclerView;
    private ImageButton addNoteBtn;
    private String userCollection;
    private FirebaseUser user;
    private StorageReference storageReference;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_notes);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PersonalNotesActivity.this));

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.personal_notes);

        readData();

        FirebaseUser user = mAuth.getCurrentUser();
        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();

        databaseReference.child("Users").child(userCollection).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dalto = snapshot.child("sett").child("1").getValue().toString();
                if(dalto.equals("tritanopia")){
                    constraintLayout.setBackgroundResource(R.color.background_tritano);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void readData(){

        FirebaseUser user = mAuth.getCurrentUser();
        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();


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
                listAdapterNotes = new ListAdapterNotes(PersonalNotesActivity.this,listNotes);
                recyclerView.setAdapter(listAdapterNotes);
                listAdapterNotes.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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


}