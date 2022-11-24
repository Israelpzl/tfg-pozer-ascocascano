package com.example.leeconmonclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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

    private static final String STRING_PREFERENCES = "leeconmonclick.login";
    private static final String PREFERENCES_STATE_BUTTON = "leeconmonclick.login.button";

    private EditText namePatient, passPatient;
    private Button btnLoginPatient;
    private Switch remeberSession;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_patient2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        namePatient =  findViewById(R.id.namePacientIId);
        passPatient = findViewById(R.id.passPacientId);
        btnLoginPatient = findViewById(R.id.BtnLoginPacientId);
        remeberSession = (Switch) findViewById(R.id.switch_remember1);


        btnLoginPatient.setOnClickListener(new View.OnClickListener() {
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
                    String nPacient = (String) objDataSnapshot.child("namePatient").getValue();
                    String pass = (String) objDataSnapshot.child("password").getValue();


                    try {
                        pass = decrypt(pass);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (nPacient.equals(namePatient.getText().toString()) && pass.equals(passPatient.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Usuario Encontrado",Toast.LENGTH_LONG).show();
                        goHomePatient();
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

    public void saveStateSession(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCES_STATE_BUTTON,remeberSession.isChecked()).apply();
    }

    private void goHomePatient(){
        Intent i = new Intent(this, HomePatientActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        saveStateSession();
        startActivity(i);
        finish();
    }

}