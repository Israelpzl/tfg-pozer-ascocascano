package com.example.leeconmonclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.leerconmonclick.util.Content;
import es.leerconmonclick.util.ListAdapterUserPatient;
import es.leerconmonclick.util.UserPatient;

public class HomeProfesionalActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth db;

    private List<UserPatient> userPatientItems;
    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_profesional);

        userPatientItems = new ArrayList<>();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference();


        getListUserPatient();
    }



    private void getListUserPatient() {

        databaseReference.child("userPacient").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot objDataSnapshot : snapshot.getChildren()){
                    String namePatient = (String) objDataSnapshot.child("namePacient").getValue();
                    String age = (String) objDataSnapshot.child("agePacient").getValue();
                    String email = (String) objDataSnapshot.child("emailPacient").getValue();
                   //String nameProfesional = (String) objDataSnapshot.child("nameProfesional").getValue();
                    String pass = (String) objDataSnapshot.child("password").getValue();
                    String description = (String) objDataSnapshot.child("descriptionPacient").getValue();


                    UserPatient userPatient = new UserPatient(namePatient,age,email,pass,description,"bjbhb");
                    userPatientItems.add(userPatient);

                    ListAdapterUserPatient listAdapterUserPatient = new ListAdapterUserPatient(userPatientItems,HomeProfesionalActivity.this);

                    RecyclerView recyclerView = findViewById(R.id.recycleViewUserPatientId);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HomeProfesionalActivity.this));
                    recyclerView.setAdapter(listAdapterUserPatient);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}