package com.example.leeconmonclick.professional.leeconmonclick.professional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;

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
import java.util.List;

import es.leerconmonclick.util.Content;
import es.leerconmonclick.util.ListAdapterContent;


public class ContentListActivity extends AppCompatActivity {

    private List<Content> contentItems;
    private DatabaseReference databaseReference;
    private FirebaseAuth db;
    private RecyclerView recyclerView;
    private ListAdapterContent listAdapterContent;

    private String userCollection;
    private StorageReference storageReference;
    private FirebaseUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        contentItems = new ArrayList<>();
        db = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        recyclerView = findViewById(R.id.listContentRecyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(ContentListActivity.this,3));

        user = db.getCurrentUser();
        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.contentList);

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

        readData();

    }


    private void readData(){
        databaseReference.child("content").child(userCollection).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                contentItems.clear();

                for (DataSnapshot objDataSnapshot : snapshot.getChildren()){

                    String word  = (String) objDataSnapshot.child("word").getValue();
                    String determinant  = (String) objDataSnapshot.child("determinant").getValue();
                    String img  = (String) objDataSnapshot.child("img").getValue();
                    //List<String> syllables = (List<String>) objDataSnapshot.child("syllables").getValue();
                    Content content = new Content(word,img,null,determinant);
                    contentItems.add(content);

                }

                listAdapterContent = new ListAdapterContent(contentItems, ContentListActivity.this);
                recyclerView.setAdapter(listAdapterContent);
                listAdapterContent.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void goBack(View view){
        finish();
    }

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

    public void goAddContent(View v){
        Intent addContent = new Intent(this, AddContentActivity.class);
        addContent.putExtra("modeEdit",false);
        startActivity(addContent);
    }



}