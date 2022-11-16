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

import javax.mail.Session;

import es.leerconmonclick.util.JavaMail;
import es.leerconmonclick.util.UserPatient;

public class AddPacientsActivity extends AppCompatActivity {

    private EditText namePatient, agePatient, emailPatient, descriptionPatient;
    private Button addPacientBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private  String pass;
    private Session session;


    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pacients);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        mAuth =  FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        namePatient = (EditText) findViewById(R.id.namePacientId);
        agePatient = (EditText) findViewById(R.id.agePacientId);
        emailPatient = (EditText) findViewById(R.id.emailPacientId);
        descriptionPatient = (EditText) findViewById(R.id.descriptionPacientId);
        addPacientBtn = (Button) findViewById(R.id.addPacientBtn);


        addPacientBtn.setOnClickListener(new View.OnClickListener() {
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

    private void sendEmail()  {

        JavaMail javaMail = new JavaMail(this, emailPatient.getText().toString(),"NUEVO USUARIO","", namePatient.getText().toString(),pass);
        javaMail.execute();


    }


    private void registerPatient() throws Exception {

        FirebaseUser user = mAuth.getCurrentUser();
        String userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();


        pass = generatePassword();
        String passEncrypt = encrypt(pass);

        UserPatient userPatient = new UserPatient(
                namePatient.getText().toString(),
                agePatient.getText().toString(),
                emailPatient.getText().toString(),
                passEncrypt,
                descriptionPatient.getText().toString(),
                userCollection
        );

        databaseReference.child("userPacient").child(generateKey()).setValue(userPatient).addOnCompleteListener(new OnCompleteListener<Void>() {
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
