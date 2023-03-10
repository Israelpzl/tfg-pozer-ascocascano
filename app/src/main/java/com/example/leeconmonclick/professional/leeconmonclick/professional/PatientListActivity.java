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
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import java.util.List;

import es.leerconmonclick.util.adapters.ListAdapterUserPatient;
import es.leerconmonclick.util.utils.UserPatient;

public class PatientListActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private List<UserPatient> userPatientList;
    private ListAdapterUserPatient listAdapterUserPatient;
    private RecyclerView recyclerView;
    private String userCollection;
    private TextView title;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patient);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findElements();
        getSettings();

        getListUserPatient();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(PatientListActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });
    }


    private void getListUserPatient() {

        databaseReference.child("userPatient").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                userPatientList.clear();

                for(DataSnapshot objDataSnapshot : snapshot.getChildren()){
                    String namePatient = (String) objDataSnapshot.child("namePatient").getValue();
                    String age = (String) objDataSnapshot.child("agePatient").getValue();
                    String email = (String) objDataSnapshot.child("emailPatient").getValue();
                    String nameProfessional = (String) objDataSnapshot.child("nameProfessional").getValue();
                    String pass = (String) objDataSnapshot.child("password").getValue();
                    String description = (String) objDataSnapshot.child("descriptionPatient").getValue();
                    String icon = (String) objDataSnapshot.child("icon").getValue();
                    String lvlPatient = objDataSnapshot.child("lvlPatient").getValue().toString();

                    if (nameProfessional.equals(userCollection)){

                        UserPatient userPatient = new UserPatient(namePatient,age,email,pass,description,nameProfessional,icon,null,null,lvlPatient,null);
                        userPatientList.add(userPatient);

                    }

                }


                listAdapterUserPatient = new ListAdapterUserPatient(userPatientList, PatientListActivity.this, new ListAdapterUserPatient.OnItemClickListener() {
                    @Override
                    public void onItemClick(UserPatient userPatient) {
                        Intent stadisticIntent = new Intent(getApplicationContext(),StadisticActivity.class);
                        stadisticIntent.putExtra("userPatient",userPatient.getNamePatient());
                        startActivity(stadisticIntent);
                    }
                });
                recyclerView.setAdapter(listAdapterUserPatient);
                listAdapterUserPatient.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getSettings(){

        final ConstraintLayout constraintLayout;
        constraintLayout = findViewById(R.id.listPatient);

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

    private void findElements(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        userPatientList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycleViewUserPatientId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PatientListActivity.this));

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

    public void goAddPatient(View v){
        Intent addPatientIntent = new Intent(this, AddPatientsActivity.class);
        addPatientIntent.putExtra("modeEdit",false);
        startActivity(addPatientIntent);
    }



    @Override
    protected void onStop() {
        super.onStop();
        Glide.with(this).pauseRequests();
    }

    @Override
    protected void onRestart() {

        super.onRestart();
    }

}