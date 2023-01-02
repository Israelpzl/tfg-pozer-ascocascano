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
    private FirebaseAuth mAuth;
    private TextView levelText,namePatientText;
    private Context context = this;
    private CircleImageView iconPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_patient);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        databaseReference = FirebaseDatabase.getInstance().getReference();


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String namePatient = preferences.getString("userName","null");

        levelText = findViewById(R.id.level);
        namePatientText = findViewById(R.id.namePatientId);
        iconPatient = findViewById(R.id.iconPatientId);
        namePatientText.setText(namePatient);

        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String icon = snapshot.child("icon").getValue().toString();
                Glide.with(context).load(icon).into(iconPatient);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void goSettings ( View v){
        DialogSettingPatient dialogSettingPatient = new DialogSettingPatient();
        dialogSettingPatient.show(getSupportFragmentManager(),"example");

    }

    public void goGameSelecction(View v){
        Intent gameSelecctionIntent = new Intent(this, GameSelecctionActivity.class);
        startActivity(gameSelecctionIntent);
    }

    public void logOutPatient(View v){
        Toast.makeText(getApplicationContext(),"LogOut",Toast.LENGTH_LONG).show();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.putString("user","null");
        editor.putString("userName","null");
        editor.apply();
        Intent profilesActivity = new Intent(this, ProfilesActivity.class);
        startActivity(profilesActivity);
        finish();
    }



    public void goBack(View v){
        finish();
    }


    @Override
    public void applyTexts(String number) {
        levelText.setText(number);
    }
}