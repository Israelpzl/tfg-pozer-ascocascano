package com.example.leeconmonclick.patient;


import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.leeconmonclick.R;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;


import es.leerconmonclick.util.DialogSettingPatient;


public class HomePatientActivity extends AppCompatActivity implements DialogSettingPatient.DialogListener {

    private DatabaseReference databaseReference;
    private FirebaseAuth db;
    private TextView level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_patient);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        level = findViewById(R.id.level);

    }

    public void goSettings ( View v){
        DialogSettingPatient dialogSettingPatient = new DialogSettingPatient();
        dialogSettingPatient.show(getSupportFragmentManager(),"example");

    }



    public void goBack(View v){
        finish();
    }


    @Override
    public void applyTexts(String number) {
        level.setText(number);
    }
}