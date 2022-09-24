package com.example.leeconmonclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class LoginPatient2Activity extends AppCompatActivity {

    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    private EditText namePacient, passPacient;
    private Button btnLoginPacient;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_patient2);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        namePacient =  findViewById(R.id.namePacientIId);
        passPacient = findViewById(R.id.passPacientId);
        btnLoginPacient = findViewById(R.id.BtnLoginPacientId);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnLoginPacient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPacient();
            }
        });
    }

    private void loginPacient(){

        databaseReference.child("userPacient").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot objDataSnapshot : snapshot.getChildren()){
                    String nPacient = (String) objDataSnapshot.child("namePacient").getValue();
                    String pass = (String) objDataSnapshot.child("password").getValue();


                    try {
                        pass = decrypt(pass);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (nPacient.equals(namePacient.getText().toString()) && pass.equals(passPacient.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Usuario Encontrado",Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void goBack(View v){
        finish();
    }

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

    public String decrypt(String value) throws Exception
    {
        Key key = generateKeyPassword();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue,"utf-8");
        return decryptedValue;

    }

    private Key generateKeyPassword() throws Exception {
        Key key = new SecretKeySpec(KEY.getBytes(),ALGORITHM);
        return key;
    }

}