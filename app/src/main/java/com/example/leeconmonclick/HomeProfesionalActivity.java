package com.example.leeconmonclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth mAuth;

    private List<UserPatient> userPatientItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_profesional);

        userPatientItems = new ArrayList<>();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth =  FirebaseAuth.getInstance();


        getListUserPatient();
    }



    private void getListUserPatient() {

        databaseReference.child("userPacient").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                FirebaseUser user = mAuth.getCurrentUser();
                String userCollection = user.getEmail();
                String[] parts = userCollection.split("@");
                userCollection = parts[0];
                userCollection = userCollection.toLowerCase();


                for(DataSnapshot objDataSnapshot : snapshot.getChildren()){
                    String namePatient = (String) objDataSnapshot.child("namePatient").getValue();
                    String age = (String) objDataSnapshot.child("agePatient").getValue();
                    String email = (String) objDataSnapshot.child("emailtacient").getValue();
                    String nameProfessional = (String) objDataSnapshot.child("nameProfessionals").getValue();
                    String pass = (String) objDataSnapshot.child("password").getValue();
                    String description = (String) objDataSnapshot.child("descriptionPatient").getValue();

                    if (nameProfessional.equals(userCollection)){

                        UserPatient userPatient = new UserPatient(namePatient,age,email,pass,description,nameProfessional);
                        userPatientItems.add(userPatient);

                        ListAdapterUserPatient listAdapterUserPatient = new ListAdapterUserPatient(userPatientItems,HomeProfesionalActivity.this);

                        RecyclerView recyclerView = findViewById(R.id.recycleViewUserPatientId);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(HomeProfesionalActivity.this));
                        recyclerView.setAdapter(listAdapterUserPatient);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}