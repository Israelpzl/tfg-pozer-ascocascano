package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class PersonalNotesActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseAuth db = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_notes);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = db.getCurrentUser();
        String userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        ArrayList<ArrayList<String>> listNotes = new ArrayList<ArrayList<String>>();

        databaseReference.child("Users").child(userCollection).child("notas").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String count = dataSnapshot.getValue().toString();
                for(DataSnapshot objDataSnapshot : dataSnapshot.getChildren()){
                    ArrayList<String> note = new ArrayList<>();
                    String tittle = (String) objDataSnapshot.child("0").getValue();
                    String description = (String) objDataSnapshot.child("1").getValue();
                    String date = (String) objDataSnapshot.child("2").getValue();
                    note.add(tittle);
                    note.add(description);
                    note.add(date);
                    /*for(int z=0;z<3;z++){
                        String wh = String.valueOf(i);
                        String fo = String.valueOf(z);
                        String aux = dataSnapshot.child(wh).child(fo).getValue().toString();
                        note.add(aux);
                        note.add(count);
                    }*/
                    listNotes.add(note);
                }
            }
        });

        Button addNoteBtn = findViewById(R.id.materialbutt);
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalNotesActivity.this,AddNote.class));
            }
        });

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Note> noteList = realm.where(Note.class).findAll().sort("time", Sort.DESCENDING);//revisar

        RecyclerView recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdapterNotes adapterNotes = new AdapterNotes(getApplicationContext(),noteList);
        recyclerView.setAdapter(adapterNotes);

        noteList.addChangeListener(new RealmChangeListener<RealmResults<Note>>() {
            @Override
            public void onChange(RealmResults<Note> notes) {
                adapterNotes.notifyDataSetChanged();
            }
        });
    }
}