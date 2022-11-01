package com.example.leeconmonclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.leerconmonclick.util.Content;
import es.leerconmonclick.util.ListAdapterContent;


public class ContentListActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private LayoutInflater mInflater;
    private ImageView imgViewContent;
    private ImageButton imgDeleteBtn,imgEditBtn;
    private TextView nameConntent;

    private List<Content> contentItems;
    private DatabaseReference databaseReference;
    private FirebaseAuth db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        contentItems = new ArrayList<>();
        db = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        getListContent();

    }


    private void getListContent(){
        databaseReference.child("content").addListenerForSingleValueEvent(new ValueEventListener() {
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

                    ListAdapterContent listAdapterContent = new ListAdapterContent(contentItems, ContentListActivity.this);


                    RecyclerView recyclerView = findViewById(R.id.listContentRecyclerViewId);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new GridLayoutManager(ContentListActivity.this,3));
                    recyclerView.setAdapter(listAdapterContent);
                }
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

}