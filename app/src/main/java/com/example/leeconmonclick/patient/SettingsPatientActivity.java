package com.example.leeconmonclick.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.ErrorActivity;
import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.ProfilesActivity;
import com.example.leeconmonclick.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.leerconmonclick.util.AudioPlay;

public class SettingsPatientActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;


    private TextView userName;
    private ToggleButton noDaltonic,daltonic,bigSize,midSize,smallSize;
    private final Context context= this;
    private CircleImageView iconPatient;

    //Texts
    private TextView titleText;
    private TextView textName;
    private TextView daltoText;
    private TextView syzeText;
    private TextView saveText;
    private TextView logOutText;
    private String namePatient;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_patient);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        findElements();
        getSettings();
        settings();

        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String icon = snapshot.child("icon").getValue().toString();
                databaseReference.child("iconPatient").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Glide.with(context.getApplicationContext()).load(snapshot.child(icon).getValue().toString()).into(iconPatient);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(SettingsPatientActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });
    }

    public void saveChanges(View v){

        TextView userName = findViewById(R.id.editTextTextPersonNameEditPatient);
        ToggleButton noDaltonic = findViewById(R.id.toggleButtonNoDaltoPatient);
        ToggleButton daltonic = findViewById(R.id.toggleButtonDaltoPatient);
        ToggleButton bigSize = findViewById(R.id.toggleButtonBigPatient);
        ToggleButton midSize = findViewById(R.id.toggleButtonMidPatient);
        ToggleButton smallSize = findViewById(R.id.toggleButtonSmallPatient);

        //Nombre
        String name = userName.getText().toString();
        //Daltonismo
        String dalto;
        if(noDaltonic.isChecked()){
            dalto = "no";
        }else if(daltonic.isChecked()){
            dalto = "tritanopia";
        }else{
            dalto = "no";
        }
        //Tamaño letra
        String size;
        if(bigSize.isChecked()){
            size = "grande";
        }else if(midSize.isChecked()){
            size = "normal";
        }else if(smallSize.isChecked()){
            size = "peque";
        }else{
            size = "normal";
        }

        databaseReference.child("userPatient").child(namePatient).child("namePatient").setValue(name);
        databaseReference.child("userPatient").child(namePatient).child("sett").child("1").setValue(dalto);
        databaseReference.child("userPatient").child(namePatient).child("sett").child("0").setValue(size);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userPatient",name);
        editor.apply();

        Toast.makeText(getApplicationContext(),"Datos guardados correctamente",Toast.LENGTH_LONG).show();
        //goHome();
        finish();

    }

    public void settings(){

        databaseReference.child("userPatient").child(namePatient).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                userName.setText(dataSnapshot.child("namePatient").getValue().toString());
                //Seccion para daltonismo
                String daltonism = dataSnapshot.child("sett").child("1").getValue().toString();
                if(daltonism.equals("no")){
                    noDaltonic.setChecked(true);
                }else{
                    daltonic.setChecked(true);
                }
                //Seccion tamaño
                String size = dataSnapshot.child("sett").child("0").getValue().toString();
                if(size.equals("grande")){
                    bigSize.setChecked(true);
                }else if(size.equals("normal")){
                    midSize.setChecked(true);
                }else{
                    smallSize.setChecked(true);
                }

            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    public void changeDalto(View v){
        ToggleButton noDaltonic = findViewById(R.id.toggleButtonNoDaltoPatient);
        ToggleButton daltonic = findViewById(R.id.toggleButtonDaltoPatient);
        switch (v.getId()){
            case R.id.toggleButtonNoDaltoPatient:
                daltonic.setChecked(false);
                break;
            case R.id.toggleButtonDaltoPatient:
                noDaltonic.setChecked(false);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void changeSize(View v){
        ToggleButton bigSize = findViewById(R.id.toggleButtonBigPatient);
        ToggleButton midSize = findViewById(R.id.toggleButtonMidPatient);
        ToggleButton smallSize = findViewById(R.id.toggleButtonSmallPatient);
        switch (v.getId()){
            case R.id.toggleButtonBigPatient:
                midSize.setChecked(false);
                smallSize.setChecked(false);
                break;
            case R.id.toggleButtonMidPatient:
                bigSize.setChecked(false);
                smallSize.setChecked(false);
                break;
            case R.id.toggleButtonSmallPatient:
                bigSize.setChecked(false);
                midSize.setChecked(false);
                break;
        }
    }

    private void getSettings(){

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.settingPatients);
        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                switch (size) {
                    case "grande":
                        userName.setTextSize(30);
                        titleText.setTextSize(30);
                        textName.setTextSize(30);
                        daltoText.setTextSize(30);
                        syzeText.setTextSize(30);
                        saveText.setTextSize(30);
                        logOutText.setTextSize(30);
                        noDaltonic.setTextSize(30);
                        daltonic.setTextSize(30);
                        bigSize.setTextSize(30);
                        midSize.setTextSize(30);
                        smallSize.setTextSize(30);
                        break;
                    case "normal":
                        userName.setTextSize(20);
                        titleText.setTextSize(20);
                        textName.setTextSize(20);
                        daltoText.setTextSize(20);
                        syzeText.setTextSize(20);
                        saveText.setTextSize(20);
                        logOutText.setTextSize(20);
                        noDaltonic.setTextSize(20);
                        daltonic.setTextSize(20);
                        bigSize.setTextSize(20);
                        midSize.setTextSize(20);
                        smallSize.setTextSize(20);
                        break;
                    case "peque":
                        userName.setTextSize(10);
                        titleText.setTextSize(10);
                        textName.setTextSize(10);
                        daltoText.setTextSize(10);
                        syzeText.setTextSize(10);
                        saveText.setTextSize(10);
                        logOutText.setTextSize(10);
                        noDaltonic.setTextSize(10);
                        daltonic.setTextSize(10);
                        bigSize.setTextSize(10);
                        midSize.setTextSize(10);
                        smallSize.setTextSize(10);
                        break;
                }
                String dalto = snapshot.child("sett").child("1").getValue().toString();
                if(dalto.equals("tritanopia")){
                    logOutText.setBackgroundResource(R.drawable.button_style_red_tritano);
                    saveText.setBackgroundResource(R.drawable.button_style_tritano);
                    constraintLayout.setBackgroundResource(R.color.background_tritano);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void findElements(){


        databaseReference = FirebaseDatabase.getInstance().getReference();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namePatient = preferences.getString("userPatient","null").toLowerCase(Locale.ROOT);

        userName = findViewById(R.id.editTextTextPersonNameEditPatient);
        noDaltonic = findViewById(R.id.toggleButtonNoDaltoPatient);
        daltonic = findViewById(R.id.toggleButtonDaltoPatient);
        bigSize = findViewById(R.id.toggleButtonBigPatient);
        midSize = findViewById(R.id.toggleButtonMidPatient);
        smallSize = findViewById(R.id.toggleButtonSmallPatient);

        titleText = findViewById(R.id.textViewTitlePatien);
        textName = findViewById(R.id.textViewNamePatient);
        daltoText = findViewById(R.id.textViewDaltonismPatient);
        syzeText = findViewById(R.id.textViewSizePatient);
        saveText = findViewById(R.id.buttonSaveChangesPatient);
        logOutText = findViewById(R.id.buttonLogOutPatient);

        iconPatient = findViewById(R.id.iconPatientId);

    }

    public void goBack(View v){
        finish();
    }

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

    public void logOut(View v) {
        Toast.makeText(getApplicationContext(),"LogOut",Toast.LENGTH_LONG).show();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.putString("user","null");
        editor.putString("userPatient","null");
        editor.apply();
        Intent profilesActivity = new Intent(this, ProfilesActivity.class);
        startActivity(profilesActivity);
        finish();
    }

    @Override
    protected void onPause() {
        boolean valor = AudioPlay.isIsplayingAudio();
        if(valor){
            AudioPlay.stopAudio();
        }
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}