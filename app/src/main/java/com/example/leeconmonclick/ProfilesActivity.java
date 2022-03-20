package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class ProfilesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
    }

    public void user(View v){
        Intent helpIntent = new Intent(this, MainActivity.class);//Esta tiene que llevar a la de login alumno
        startActivity(helpIntent);
    }

    public void professional(View v){
        Intent helpIntent = new Intent(this, MainActivity.class);//Esta tiene que llevar a la de login profesional
        startActivity(helpIntent);
    }
}