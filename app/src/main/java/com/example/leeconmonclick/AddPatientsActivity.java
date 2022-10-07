package com.example.leeconmonclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Key;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import es.leerconmonclick.util.JavaMail;
import es.leerconmonclick.util.UserPatient;

public class AddPatientsActivity extends AppCompatActivity {

    private EditText namePatient, agePatient, emailPatient, descriptionPatient;
    private Button addPatientBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private  String pass;
    private Bundle data;


    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patients);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        mAuth =  FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        namePatient = (EditText) findViewById(R.id.namePacientId);
        agePatient = (EditText) findViewById(R.id.agePacientId);
        emailPatient = (EditText) findViewById(R.id.emailPacientId);
        descriptionPatient = (EditText) findViewById(R.id.descriptionPacientId);
        addPatientBtn = (Button) findViewById(R.id.addPacientBtn);

        data = getIntent().getExtras();
        if (data.getBoolean("modeEdit")){
            modeEditOn();
        }


        addPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    registerPatient();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                sendEmail();

            }
        });
    }

    private void modeEditOn() {
        namePatient.setText(data.getString("namePatient"));
        agePatient.setText(data.getString("agePatient"));
        emailPatient.setText(data.getString("emailPatient"));
        descriptionPatient.setText(data.getString("descriptionPatient"));
    }

    private void sendEmail()  {

        if (!data.getBoolean("modeEdit")){
            JavaMail javaMail = new JavaMail(this, emailPatient.getText().toString(),"NUEVO USUARIO","", namePatient.getText().toString(),pass);
            javaMail.execute();
        }



    }


    private void registerPatient() throws Exception {

        FirebaseUser user = mAuth.getCurrentUser();
        String userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();


        pass = generatePassword();
        String passEncrypt = encrypt(pass);

        if (data.getBoolean("modeEdit")){
            passEncrypt = data.getString("passPatient");
        }

        UserPatient userPatient = new UserPatient(
                namePatient.getText().toString(),
                agePatient.getText().toString(),
                emailPatient.getText().toString(),
                passEncrypt,
                descriptionPatient.getText().toString(),
                userCollection
        );

        databaseReference.child("userPatient").child(namePatient.getText().toString()).setValue(userPatient).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Se ha añadido el paciente correctamente",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void goBack(View view){finish();}

    private String generateKey(){
        return UUID.randomUUID().toString();
    }

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }


    public String encrypt(String value) throws Exception {
        Key key = generateKeyPassword();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;
    }

    private Key generateKeyPassword() throws Exception {
        Key key = new SecretKeySpec(KEY.getBytes(),ALGORITHM);
        return key;
    }

    private String generatePassword(){
        char [] capiLetters = {'A','B','C','D','E','F','G','H','I','J'};
        char [] lowerCase = {'a','b','c','d','e','f','g','h','i','j'};
        char [] number = {'1','2','3','4','5','6','7','8','9','0'};
        StringBuilder caracteres = new StringBuilder();
        caracteres.append(capiLetters);
        caracteres.append(lowerCase);
        caracteres.append(number);

        StringBuilder pass = new StringBuilder();

        for(int i = 0; i<=10; i++){
            int numCaracteres = caracteres.length();
            int numRandom = (int)(Math.random()*numCaracteres);
            pass.append((caracteres.toString()).charAt(numRandom));
        }
        return pass.toString();
    }
}
