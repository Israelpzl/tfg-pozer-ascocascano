package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;

import com.example.leeconmonclick.patient.HomePatientActivity;
import com.example.leeconmonclick.professional.leeconmonclick.professional.HomeProfesionalActivity;

public class ErrorActivity extends AppCompatActivity {

    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //Simples intent que estan pendientes de redirigir a su pagina real
    public void home(View v){

        if (getStateSession()) {
            switch (user){
                case("patient"):{
                    startActivity(new Intent(getApplicationContext(), HomePatientActivity.class));
                    finish();
                    break;
                }
                case ("professional"):{
                    startActivity(new Intent(getApplicationContext(), HomeProfesionalActivity.class));
                    finish();
                    break;
                }
            }
        }else{
            startActivity(new Intent(getApplicationContext(),ProfilesActivity.class));
            finish();
        }
    }

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

    public boolean getStateSession() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user = preferences.getString("user", "null");
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        return isLoggedIn;
    }

    public void goBack(View view){
        onBackPressed();
    }
}