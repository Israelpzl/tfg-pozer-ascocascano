package com.example.leeconmonclick.professional.leeconmonclick.professional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import java.util.ArrayList;
import java.util.List;

import es.leerconmonclick.util.ListAdapterUserPatient;
import es.leerconmonclick.util.UserPatient;

public class ListPatientActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private List<UserPatient> userPatientList;
    private ListAdapterUserPatient listAdapterUserPatient;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patient);

        userPatientList = new ArrayList<>();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth =  FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recycleViewUserPatientId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListPatientActivity.this));


        getListUserPatient();
    }



    private void getListUserPatient() {

        databaseReference.child("userPatient").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                FirebaseUser user = mAuth.getCurrentUser();
                String userCollection = user.getEmail();
                String[] parts = userCollection.split("@");
                userCollection = parts[0];
                userCollection = userCollection.toLowerCase();

                userPatientList.clear();

                for(DataSnapshot objDataSnapshot : snapshot.getChildren()){
                    String namePatient = (String) objDataSnapshot.child("namePatient").getValue();
                    String age = (String) objDataSnapshot.child("agePatient").getValue();
                    String email = (String) objDataSnapshot.child("emailtacient").getValue();
                    String nameProfessional = (String) objDataSnapshot.child("nameProfessionals").getValue();
                    String pass = (String) objDataSnapshot.child("password").getValue();
                    String description = (String) objDataSnapshot.child("descriptionPatient").getValue();
                    String icon = (String) objDataSnapshot.child("icon").getValue();

                    if (nameProfessional.equals(userCollection)){

                        UserPatient userPatient = new UserPatient(namePatient,age,email,pass,description,nameProfessional,icon);
                        userPatientList.add(userPatient);

                    }

                }

                listAdapterUserPatient = new ListAdapterUserPatient(userPatientList, ListPatientActivity.this);
                recyclerView.setAdapter(listAdapterUserPatient);
                listAdapterUserPatient.notifyDataSetChanged();
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
    public void goAddPatient(View v){
        Intent addPatientIntent = new Intent(this, AddPatientsActivity.class);
        addPatientIntent.putExtra("modeEdit",false);
        startActivity(addPatientIntent);
    }
}