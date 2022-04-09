package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void help(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }
    public void register(View v){
        Intent helpIntent = new Intent(this, RegisterProfessionalActivity.class);
        startActivity(helpIntent);
    }

    public void rememberPass(View v){
        Intent helpIntent = new Intent(this, RegisterProfessionalActivity.class);
        startActivity(helpIntent);
    }

    public void back(View v){
        finish();
    }
}