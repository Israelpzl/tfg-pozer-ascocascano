package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    //Simples intent que estan pendientes de redirigir a su pagina real
    public void home(View v){
        Intent homeIntent = new Intent(this, MainActivity.class);//Esta tiene que llevar a la principal
        startActivity(homeIntent);
    }

    public void help(View v){
        Intent helpIntent = new Intent(this, MainActivity.class);//Esta tiene que llevar a la de ayuda
        startActivity(helpIntent);
    }
}