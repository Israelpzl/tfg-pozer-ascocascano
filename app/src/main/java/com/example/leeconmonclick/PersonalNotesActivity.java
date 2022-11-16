package com.example.leeconmonclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.leerconmonclick.util.ListAdapterNotes;
import es.leerconmonclick.util.Note;


public class PersonalNotesActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseAuth db = FirebaseAuth.getInstance();
    ArrayList<Note> listNotes = new ArrayList<Note>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_notes);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = db.getCurrentUser();
        String userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];

        databaseReference.child("Users").child(userCollection).child("notas").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for(DataSnapshot objDataSnapshot : dataSnapshot.getChildren()){
                    Note noteExist = new Note();
                    String tittle = (String) objDataSnapshot.child("title").getValue();
                    String description = (String) objDataSnapshot.child("description").getValue();
                    Long date = (Long) objDataSnapshot.child("time").getValue();
                    noteExist.setTitle(tittle);
                    noteExist.setDescription(description);
                    noteExist.setTime(date);
                    listNotes.add(noteExist);
                }
                Button addNoteBtn = findViewById(R.id.materialbutt);
                addNoteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(PersonalNotesActivity.this, AddNoteActivity.class));
                    }
                });

                RecyclerView recyclerView = findViewById(R.id.recycleview);
                recyclerView.setLayoutManager(new LinearLayoutManager(PersonalNotesActivity.this));
                ListAdapterNotes listAdapterNotes = new ListAdapterNotes(getApplicationContext(),listNotes);
                recyclerView.setAdapter(listAdapterNotes);

                databaseReference.getRoot().addValueEventListener(new ValueEventListener() {
                    @Override
                    @SuppressLint("NotifyDataSetChanged")
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listNotes.removeAll(listNotes);
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            Note noteExist = new Note();
                            String tittle = (String) dataSnapshot1.child("title").getValue();
                            String description = (String) dataSnapshot1.child("description").getValue();
                            Long date = (Long) dataSnapshot1.child("time").getValue();
                            noteExist.setTitle(tittle);
                            noteExist.setDescription(description);
                            noteExist.setTime(date);
                            listNotes.add(noteExist);
                        }
                        listAdapterNotes.notifyDataSetChanged();
                        onRestart();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}