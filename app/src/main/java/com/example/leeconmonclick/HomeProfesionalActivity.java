package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class HomeProfesionalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_profesional);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }


    public void goListPatient(View v){
        Intent helpIntent = new Intent(this, ListPatientActivity.class);
        startActivity(helpIntent);
    }

    public void goListTask(View v){
        Intent helpIntent = new Intent(this, TaskListActivity.class);
        startActivity(helpIntent);
    }

    public void goContent(View v){
        Intent helpIntent = new Intent(this, ContentListActivity.class);
        startActivity(helpIntent);
    }




    /*

      public void goListNotes(View v){
        Intent helpIntent = new Intent(this, ListNotes.class);
        startActivity(helpIntent);
    }

    public void goSettings(View v){
        Intent helpIntent = new Intent(this, Setting.class);
        startActivity(helpIntent);
    }
    */






}