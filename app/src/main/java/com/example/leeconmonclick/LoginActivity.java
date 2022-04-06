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
        Intent helpIntent = new Intent(this, MainActivity.class);//Esta tiene que llevar a la de ayuda
        startActivity(helpIntent);
    }
    public void home(View v){
        Intent helpIntent = new Intent(this, MainActivity.class);//Esta tiene que llevar a la de ayuda
        startActivity(helpIntent);
    }
}