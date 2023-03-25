package com.example.leeconmonclick.professional.leeconmonclick.professional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leeconmonclick.ErrorActivity;
import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.R;
import com.example.leeconmonclick.patient.CategorySelecctionActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import es.leerconmonclick.util.AudioPlay;

public class RemeberPassActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText email;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remeber_pass);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findElement();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(RemeberPassActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });
    }

    private void findElement(){
        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
    }


    public void goBack(View view){
        onBackPressed();
    }
    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

    public void rememberPass(View v){
        firebaseAuth.setLanguageCode("es");
        firebaseAuth.sendPasswordResetEmail(email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {

              if (task.isSuccessful()){
                  Toast.makeText(context, "Se envió el correo de recuperación", Toast.LENGTH_LONG).show();
                  Intent loginIntent = new Intent(context, LoginProfesionalActivity.class);
                  startActivity(loginIntent);
                  finish();
              }else{
                  Toast.makeText(context, "No se pudo enviar el correo", Toast.LENGTH_LONG).show();
              }
          }
      });
    }

    @Override
    protected void onPause() {
        boolean valor = AudioPlay.isIsplayingAudio();
        if(valor){
            AudioPlay.stopAudio();
        }
        super.onPause();
    }
}