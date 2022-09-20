package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddPacientsActivity extends AppCompatActivity {

    private EditText namePacient,agePacient,emailPacient,descriptionPacient;
    private Button addPacientBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pacients);

        namePacient = (EditText) findViewById(R.id.namePacientId);
        agePacient = (EditText) findViewById(R.id.agePacientId);
        emailPacient = (EditText) findViewById(R.id.emailPacientId);
        descriptionPacient = (EditText) findViewById(R.id.descriptionPacientId);
        addPacientBtn = (Button) findViewById(R.id.addPacientBtn);

        addPacientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });
    }

    private void sendMail() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setData(Uri.parse("mailto:"));
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL,emailPacient.getText().toString());
        email.putExtra(Intent.EXTRA_SUBJECT,"USUARIO PACIENTE");
        email.putExtra(Intent.EXTRA_TEXT,"SE HA CREADO EL USUARIO CORRECTAMENTE");
        startActivity(Intent.createChooser(email,"send email"));
    }

    public void goBack(View view){finish();}

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }
}