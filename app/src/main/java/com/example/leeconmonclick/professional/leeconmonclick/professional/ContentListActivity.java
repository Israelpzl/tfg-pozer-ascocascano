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
import android.widget.TextView;

import com.example.leeconmonclick.ErrorActivity;
import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.R;
import com.example.leeconmonclick.patient.CategorySelecctionActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

import es.leerconmonclick.util.AudioPlay;
import es.leerconmonclick.util.utils.Content;
import es.leerconmonclick.util.adapters.ListAdapterContent;


public class ContentListActivity extends AppCompatActivity {

    private List<Content> contentItems;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private ListAdapterContent listAdapterContent;
    private TextView titlePage;

    private String userCollection;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findElements();
        getSettings();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(ContentListActivity.this,3));

        readData();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(ContentListActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });
    }


    private void readData(){
        databaseReference.child("content").child(userCollection).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                contentItems.clear();

                for (DataSnapshot objDataSnapshot : snapshot.getChildren()){

                    String word  = (String) objDataSnapshot.child("word").getValue();
                    String difficulty  = (String) objDataSnapshot.child("difficulty").getValue();
                    String img  = (String) objDataSnapshot.child("img").getValue();
                    Content content = new Content(word,img,"",difficulty,false);
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

    private void getSettings(){

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.contentList);

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
        contentItems = new ArrayList<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.listContentRecyclerViewId);
        titlePage = findViewById(R.id.tittleContentListId);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        userCollection = user.getEmail();
        assert userCollection != null;
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();


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

    @Override
    protected void onPause() {
        boolean valor = AudioPlay.isIsplayingAudio();
        if(valor){
            AudioPlay.stopAudio();
        }
        super.onPause();
    }



}