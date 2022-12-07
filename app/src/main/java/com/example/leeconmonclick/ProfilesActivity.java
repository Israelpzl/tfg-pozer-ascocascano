package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.leeconmonclick.patient.LoginPatient2Activity;
import com.example.leeconmonclick.professional.leeconmonclick.professional.LoginProfesionalActivity;

public class ProfilesActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void goUser(View v){
        Intent helpIntent = new Intent(this, LoginPatient2Activity.class);
        startActivity(helpIntent);
        finish();
    }

    public void goProfessional(View v){
        Intent helpIntent = new Intent(this, LoginProfesionalActivity.class);
        startActivity(helpIntent);
        finish();
    }

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
        finish();
    }
}