package com.example.leeconmonclick.professional.leeconmonclick.professional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeconmonclick.ErrorActivity;
import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.LoadActivity;
import com.example.leeconmonclick.R;
import com.example.leeconmonclick.SplashActivity;
import com.example.leeconmonclick.patient.CategorySelecctionActivity;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import es.leerconmonclick.util.utils.Categories;
import es.leerconmonclick.util.utils.Difficulties;
import es.leerconmonclick.util.utils.Game;
import es.leerconmonclick.util.utils.JavaMail;
import es.leerconmonclick.util.utils.UserPatient;

public class AddPatientsActivity extends AppCompatActivity {

    private EditText namePatient, agePatient, emailPatient, descriptionPatient;
    private TextView nameTitle,ageTitle,emailTitle,descriptionTitle,title;
    private Button addPatientBtn;
    private DatabaseReference databaseReference;
    private  String pass;
    private Bundle data;
    private boolean find = false;

    private String userCollection;

    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patients);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Encontrar e inicializar los elementos de base de datos y de la actividad
        findElements();
        //Recuperar los setting del usuario
        getSettings();


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //Comprobar si nos encontramos en modo edición de un contenido y si es así, recupera los valores del contenido a modificar
        if (data.getBoolean("modeEdit")){
            modeEditOn();
        }

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(AddPatientsActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });

    }

    public void savePatient(View view) throws Exception {

        registerPatient();


    }

    private void modeEditOn() {
        namePatient.setText(data.getString("namePatient"));
        agePatient.setText(data.getString("agePatient"));
        emailPatient.setText(data.getString("emailPatient"));
        descriptionPatient.setText(data.getString("descriptionPatient"));
        title.setText("EDITAR PACIENTE");
    }

    private void sendEmail()  {
        if (!data.getBoolean("modeEdit")){
            JavaMail javaMail = new JavaMail(this, emailPatient.getText().toString(),"NUEVO USUARIO","", namePatient.getText().toString(),pass);
            javaMail.execute();
        }
    }

    private void registerPatient() throws Exception {

        pass = generatePassword();
        String passEncrypt = encrypt(pass);

        if (data.getBoolean("modeEdit")){
            passEncrypt = data.getString("passPatient");
            databaseReference.child("userPatient").child(data.getString("namePatient")).removeValue();
        }

        Difficulties difficultiesStadistic = new Difficulties(0,0,0);

        Map<String,Difficulties> difficultiesMap = new HashMap<>();
        difficultiesMap.put("FÁCIL",difficultiesStadistic);
        difficultiesMap.put("NORMAL",difficultiesStadistic);
        difficultiesMap.put("DIFÍCIL",difficultiesStadistic);
        difficultiesMap.put("PRÁCTICA",difficultiesStadistic);

        Categories categoryStadistic = new Categories(0);

        Map<String,Categories> categoriesMap = new HashMap<>();
        categoriesMap.put("Hogar",categoryStadistic);
        categoriesMap.put("Animales",categoryStadistic);
        categoriesMap.put("Comidas",categoryStadistic);
        categoriesMap.put(userCollection,categoryStadistic);

        Game gameStadistic = new Game(0,categoriesMap,difficultiesMap);

        Map<String,Game> stadistic = new HashMap<>();
        stadistic.put("joinWords",gameStadistic);
        stadistic.put("letters",gameStadistic);

        String icon = "6lvl3";
        String progress = "0";

        ArrayList<String> sett = new ArrayList<>();
        sett.add("normal");
        sett.add("no");

        String lvlPatient = "1";

        UserPatient userPatient = new UserPatient(
                namePatient.getText().toString().toLowerCase(Locale.ROOT).trim(),
                agePatient.getText().toString(),
                emailPatient.getText().toString(),
                passEncrypt,
                descriptionPatient.getText().toString(),
                userCollection,
                icon,
                stadistic,
                sett,
                lvlPatient,
                progress
        );

        databaseReference.child("userPatient").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<String> listPatient = new ArrayList<>();

                for(DataSnapshot objDataSnapshot: snapshot.getChildren()){
                    listPatient.add(objDataSnapshot.child("namePatient").getValue().toString());
                }

                if (!listPatient.contains(namePatient.getText().toString().toLowerCase(Locale.ROOT).trim())){
                    databaseReference.child("userPatient").child(namePatient.getText().toString().toLowerCase(Locale.ROOT).trim()).setValue(userPatient).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                if (!data.getBoolean("modeEdit")){
                                    Toast.makeText(getApplicationContext(),"Se ha añadido el paciente correctamente",Toast.LENGTH_LONG).show();
                                    sendEmail();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Se ha editado el paciente correctamente",Toast.LENGTH_LONG).show();
                                }
                                databaseReference.child("userPatient").child(namePatient.getText().toString().toLowerCase(Locale.ROOT)).child("stadistic").child("syllables").setValue(difficultiesStadistic);
                            }
                        }
                    });



                }else {
                    Toast.makeText(getApplicationContext(),"Nombre ocupado",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if (data.getBoolean("modeEdit")){
            Intent addPatientIntent = new Intent(this, LoadActivity.class);
            addPatientIntent.putExtra("add","addPatient");
            startActivity(addPatientIntent);
        }


    }

    public String encrypt(String value) throws Exception {
        Key key = generateKeyPassword();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;
    }

    private Key generateKeyPassword() throws Exception {
        return new SecretKeySpec(KEY.getBytes(),ALGORITHM);
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

        for(int i = 0; i<=3; i++){
            int numCaracteres = caracteres.length();
            int numRandom = (int)(Math.random()*numCaracteres);
            pass.append((caracteres.toString()).charAt(numRandom));
        }
        return pass.toString();
    }

    private void getSettings(){

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.addPatient);

        databaseReference.child("Users").child(userCollection).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                switch (size) {
                    case "grande":
                        namePatient.setTextSize(30);
                        agePatient.setTextSize(30);
                        emailPatient.setTextSize(30);
                        descriptionPatient.setTextSize(30);
                        addPatientBtn.setTextSize(30);
                        nameTitle.setTextSize(30);
                        ageTitle.setTextSize(30);
                        emailTitle.setTextSize(30);
                        descriptionTitle.setTextSize(30);
                        break;
                    case "normal":
                        namePatient.setTextSize(20);
                        agePatient.setTextSize(20);
                        emailPatient.setTextSize(20);
                        descriptionPatient.setTextSize(20);
                        addPatientBtn.setTextSize(20);
                        nameTitle.setTextSize(20);
                        ageTitle.setTextSize(20);
                        emailTitle.setTextSize(20);
                        descriptionTitle.setTextSize(20);
                        break;
                    case "peque":
                        namePatient.setTextSize(10);
                        agePatient.setTextSize(10);
                        emailPatient.setTextSize(10);
                        descriptionPatient.setTextSize(10);
                        addPatientBtn.setTextSize(10);
                        nameTitle.setTextSize(10);
                        ageTitle.setTextSize(10);
                        emailTitle.setTextSize(10);
                        descriptionTitle.setTextSize(10);
                        break;
                }
                String dalto = snapshot.child("sett").child("1").getValue().toString();
                if(dalto.equals("tritanopia")){
                    constraintLayout.setBackgroundResource(R.color.background_tritano);
                    addPatientBtn.setBackgroundResource(R.drawable.button_style_tritano);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setContentView(R.layout.activity_error2);
            }
        });

    }

    private void findElements(){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        data = getIntent().getExtras();

        namePatient = (EditText) findViewById(R.id.namePacientId);
        agePatient = (EditText) findViewById(R.id.agePacientId);
        emailPatient = (EditText) findViewById(R.id.emailPacientId);
        descriptionPatient = (EditText) findViewById(R.id.descriptionPacientId);
        addPatientBtn = (Button) findViewById(R.id.addPacientBtn);

        nameTitle = findViewById(R.id.nameId);
        ageTitle = findViewById(R.id.textView16);
        emailTitle = findViewById(R.id.textView17);
        descriptionTitle = findViewById(R.id.descriptionPatient);
        title = findViewById(R.id.tittleAddPacientId);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        userCollection = user.getEmail();
        assert userCollection != null;
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();

    }

    public void goBack(View view){finish();}

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

}
