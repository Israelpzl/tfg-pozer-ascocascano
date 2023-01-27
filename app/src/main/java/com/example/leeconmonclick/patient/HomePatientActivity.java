package com.example.leeconmonclick.patient;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.ProfilesActivity;
import com.example.leeconmonclick.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import de.hdodenhof.circleimageview.CircleImageView;
import es.leerconmonclick.util.DialogSettingPatient;


public class HomePatientActivity extends AppCompatActivity implements DialogSettingPatient.DialogListener {

    private DatabaseReference databaseReference;
    private TextView levelText,namePatientText;
    private Context context = this;
    private CircleImageView iconPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_patient);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        findElement();


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String namePatient = preferences.getString("userPatient","null");

        namePatientText.setText(namePatient);

        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String icon = snapshot.child("icon").getValue().toString();
                databaseReference.child("iconPatient").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Glide.with(context).load(snapshot.child(icon).getValue().toString()).into(iconPatient);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void goSettings(View v){
        DialogSettingPatient dialogSettingPatient = new DialogSettingPatient();
        dialogSettingPatient.show(getSupportFragmentManager(),"example");
    }

    public void goProgression(View v){
        Intent progress = new Intent(this, ProgresionPatientActivity.class);
        startActivity(progress);
    }

    public void goGameSelecction(View v){
        Intent gameSelecctionIntent = new Intent(this, GameSelecctionActivity.class);
        startActivity(gameSelecctionIntent);
    }





    public void goBack(View v){
        finish();
    }

    public void findElement(){

        levelText = findViewById(R.id.level);
        namePatientText = findViewById(R.id.namePatientId);
        iconPatient = findViewById(R.id.iconPatientId);
    }


    @Override
    public void applyTexts(String number,int x,int y) {

        int numeroInt = Integer.parseInt(number);

        if ((x+y) == numeroInt){
            Intent settingPatient = new Intent(this, SettingsPatientActivity.class);
            startActivity(settingPatient);
        }else{
            Toast.makeText(getApplicationContext(), "Suma Incorrecta", Toast.LENGTH_LONG).show();
        }
    }
}