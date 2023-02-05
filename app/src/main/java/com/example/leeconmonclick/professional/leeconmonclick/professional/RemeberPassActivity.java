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

import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RemeberPassActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText email;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remeber_pass);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        firebaseAuth = FirebaseAuth.getInstance();
        findElement();
    }

    private void findElement(){
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
}