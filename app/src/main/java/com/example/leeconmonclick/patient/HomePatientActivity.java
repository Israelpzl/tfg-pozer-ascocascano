package com.example.leeconmonclick.patient;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeconmonclick.ProfilesActivity;
import com.example.leeconmonclick.R;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;


import es.leerconmonclick.util.DialogSettingPatient;


public class HomePatientActivity extends AppCompatActivity implements DialogSettingPatient.DialogListener {

    private DatabaseReference databaseReference;
    private FirebaseAuth db;
    private TextView levelText,namePatientText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_patient);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String namePatient = preferences.getString("userPatient","null");

        levelText = findViewById(R.id.level);
        namePatientText = findViewById(R.id.namePatientId);
        namePatientText.setText(namePatient);

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