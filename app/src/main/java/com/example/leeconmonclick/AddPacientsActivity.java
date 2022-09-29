package com.example.leeconmonclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.Properties;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import javax.mail.Authenticator;
import javax.mail.Message;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import es.leerconmonclick.util.UserPatient;

public class AddPacientsActivity extends AppCompatActivity {

    private EditText namePacient,agePacient,emailPacient,descriptionPacient;
    private Button addPacientBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private Session session;
    private String email,pass;

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
        email = "pozeriaspozeriascocascano@gmail";
        pass = "xibhweslzkstsard";

        namePacient = (EditText) findViewById(R.id.namePacientId);
        agePacient = (EditText) findViewById(R.id.agePacientId);
        emailPacient = (EditText) findViewById(R.id.emailPacientId);
        descriptionPacient = (EditText) findViewById(R.id.descriptionPacientId);
        addPacientBtn = (Button) findViewById(R.id.addPacientBtn);


        addPacientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    sendEmail();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void sendEmail() throws MessagingException {
        Properties props = new Properties();
        /*
        //properties.put("mail.smtp.host", "mail.gmail.com");
        properties.setProperty("mail.smtp.host", "127.0.0.1");
        properties.put("mail.transport.protocol","smtp");
        //properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.user", "leerConMonclick@gmail.com");
        properties.put("mail.smtp.auth","false");
        properties.put("mail.smtp.port",25); //465
        props.put("mail.smtp.starttls.enable", "true");

         */

        props.put("mail.smtp.host", "smtp.googlemail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port","465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.transport.protocol","smtp");
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.port","465");
        props.put("mail.smtp.user", email);
        props.put("mail.smtp.pass", pass);







            session = Session.getDefaultInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email,pass);
                }
            });


                if (session != null){


                    try {
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(email));
                        message.setSubject("Nuevo Usuario LeerConMonclick");
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("israelpozuelo00@gmail.com"));
                        message.setContent("Se ha creado nuevo Usuario de Paciente","text/plain");
                        Transport.send(message);






                    } catch (MessagingException msg) {
                        msg.printStackTrace();
                    }finally {

                    }


                /*
                Transport t = session.getTransport("smtp");
                t.connect("no-reply@leerConMonclick.com","la password");
                t.sendMessage(message,message.getAllRecipients());
                t.close();

                 */

            }

    }


    private void registerPatient() throws Exception {

        FirebaseUser user = mAuth.getCurrentUser();
        String userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();


        String passEncrypt = encrypt(generatePassword());

        UserPatient userPatient = new UserPatient(
                namePacient.getText().toString(),
                agePacient.getText().toString(),
                emailPacient.getText().toString(),
                passEncrypt,
                descriptionPacient.getText().toString(),
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
