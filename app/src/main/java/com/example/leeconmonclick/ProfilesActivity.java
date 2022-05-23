package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ProfilesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
    }

    public void user(View v){
        Intent helpIntent = new Intent(this, LoginPatientActivity.class);
        startActivity(helpIntent);
    }

    public void professional(View v){
        Intent helpIntent = new Intent(this, LoginProfesionalActivity.class);
        startActivity(helpIntent);
    }

    public void help(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }
}