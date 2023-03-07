package com.example.leeconmonclick;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.WindowManager;

import com.example.leeconmonclick.patient.HomePatientActivity;
import com.example.leeconmonclick.professional.leeconmonclick.professional.HomeProfesionalActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String STRING_PREFERENCES = "leeconmonclick.login";
    private static final String PREFERENCES_STATE_BUTTON = "leeconmonclick.login.button";

    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Convertir a pantalla completa

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Thread thread=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(3000);//abre la StartActivity tras 3 segundos

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

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
    public boolean getStateSession(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user = preferences.getString("user","null");
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn",false);
        return isLoggedIn;

    }
}