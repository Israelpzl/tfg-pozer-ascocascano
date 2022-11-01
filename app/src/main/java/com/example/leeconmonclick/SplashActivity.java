package com.example.leeconmonclick;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    private static final String STRING_PREFERENCES = "leeconmonclick.login";
    private static final String PREFERENCES_STATE_BUTTON = "leeconmonclick.login.button";

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
                    if (!getStateSession()){
                        startActivity(new Intent(getApplicationContext(),ProfilesActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(getApplicationContext(),HomeProfesionalActivity.class));
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
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCES_STATE_BUTTON,false);
    }
}