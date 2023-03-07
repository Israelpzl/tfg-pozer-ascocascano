package com.example.leeconmonclick.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.leeconmonclick.ErrorActivity;
import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class LoginPatient2Activity extends AppCompatActivity {

    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    private EditText namePatient, passPatient;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch remeberSession;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_patient2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findElements();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(LoginPatient2Activity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });
    }

    public void loginPatient(View v){

        AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.passPacientId, ".{4,}", R.string.error_pass);

        if (awesomeValidation.validate()){
            databaseReference.child("userPatient").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    boolean exitUser = false;
                    String user = "";
                    for(DataSnapshot objDataSnapshot : snapshot.getChildren()){
                        String nPatient = objDataSnapshot.child("namePatient").getValue().toString().toLowerCase(Locale.ROOT);


                        if (nPatient.equals(namePatient.getText().toString().toLowerCase(Locale.ROOT).trim())){
                            exitUser = true;
                            user = nPatient;
                            break;
                        }

                    }

                    if (!exitUser){
                        Toast.makeText(getApplicationContext(),"No se encuentra el usuario",Toast.LENGTH_LONG).show();
                    }else{
                        String pass = snapshot.child(user).child("password").getValue().toString();

                        try {
                            pass = decrypt(pass);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (pass.equals(passPatient.getText().toString())){
                            Toast.makeText(getApplicationContext(),"Usuario Encontrado",Toast.LENGTH_LONG).show();
                            goHomePatient();
                        }else{
                            Toast.makeText(getApplicationContext(),"La contraseña no coincide",Toast.LENGTH_LONG).show();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void findElements(){
        databaseReference = FirebaseDatabase.getInstance().getReference();

        namePatient =  findViewById(R.id.namePacientIId);
        passPatient = findViewById(R.id.passPacientId);
        remeberSession = (Switch) findViewById(R.id.switch_remember1);
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
        return new SecretKeySpec(KEY.getBytes(),ALGORITHM);
    }

    public void saveStateSession(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", remeberSession.isChecked());
        editor.putString("user","patient");
        editor.putString("userPatient",namePatient.getText().toString().toLowerCase(Locale.ROOT).trim());
        editor.apply();
    }

    private void goHomePatient(){
        Intent i = new Intent(this, HomePatientActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        saveStateSession();
        startActivity(i);
        finish();
    }

}