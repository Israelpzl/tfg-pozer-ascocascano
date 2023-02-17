package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.leeconmonclick.patient.LoginPatient2Activity;
import com.example.leeconmonclick.patient.ProgresionPatientActivity;
import com.example.leeconmonclick.professional.leeconmonclick.professional.HomeProfesionalActivity;
import com.example.leeconmonclick.professional.leeconmonclick.professional.LoginProfesionalActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class ProfilesActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(ProfilesActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });
    }

    public void goPatient(View v){
        Intent helpIntent = new Intent(this, LoginPatient2Activity.class);
        startActivity(helpIntent);
    }

    public void goProfessional(View v){
        Intent helpIntent = new Intent(this, LoginProfesionalActivity.class);
        startActivity(helpIntent);
    }

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            Intent goHome = new Intent(this, HomeProfesionalActivity.class);
            startActivity(goHome);
            finish();
        }


    }
}