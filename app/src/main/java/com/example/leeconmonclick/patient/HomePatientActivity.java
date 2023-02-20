package com.example.leeconmonclick.patient;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.AudioPlay;
import com.example.leeconmonclick.ErrorActivity;
import com.example.leeconmonclick.ProfilesActivity;
import com.example.leeconmonclick.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.leerconmonclick.util.DialogSettingPatient;


public class HomePatientActivity extends AppCompatActivity implements DialogSettingPatient.DialogListener {

    private DatabaseReference databaseReference;
    private TextView levelText, namePatientText;
    private Context context = this;
    private CircleImageView iconPatient;
    private ImageButton audio;
    private String namePatient;
    private  ConstraintLayout constraintLayout;
    private int points;

    private TextView btnJugar, btnProgresion, btnsett;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_patient);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        AudioPlay.playAudio(this, R.raw.homeaudio);



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namePatient = preferences.getString("userPatient", "null").toLowerCase(Locale.ROOT);


        findElements();
        setLvl();

        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String icon = snapshot.child("icon").getValue().toString();
                databaseReference.child("iconPatient").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Glide.with(context).load(snapshot.child(icon).getValue().toString()).into(iconPatient);
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

        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                if (size.equals("grande")) {
                    btnJugar.setTextSize(30);
                    btnsett.setTextSize(30);
                    btnProgresion.setTextSize(30);
                    namePatientText.setTextSize(30);
                    levelText.setTextSize(30);
                } else if (size.equals("normal")) {
                    btnJugar.setTextSize(20);
                    btnsett.setTextSize(20);
                    btnProgresion.setTextSize(20);
                    namePatientText.setTextSize(20);
                    levelText.setTextSize(20);
                } else if (size.equals("peque")) {
                    btnJugar.setTextSize(10);
                    btnsett.setTextSize(10);
                    btnProgresion.setTextSize(10);
                    namePatientText.setTextSize(10);
                    levelText.setTextSize(10);
                }
                String dalto = snapshot.child("sett").child("1").getValue().toString();
                if (dalto.equals("tritanopia")) {
                    btnsett.setBackgroundResource(R.drawable.button_style_tritano);
                    btnJugar.setBackgroundResource(R.drawable.button_style_tritano);
                    btnProgresion.setBackgroundResource(R.drawable.button_style_tritano);
                    constraintLayout.setBackgroundResource(R.color.background_tritano);
                }else{
                    constraintLayout.setBackgroundResource(R.color.background);
                    btnJugar.setBackgroundResource(R.drawable.button_style);
                    btnsett.setBackgroundResource(R.drawable.button_style);
                    btnProgresion.setBackgroundResource(R.drawable.button_style);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(HomePatientActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });
    }

    public void goSettings(View v) {
        DialogSettingPatient dialogSettingPatient = new DialogSettingPatient();
        dialogSettingPatient.show(getSupportFragmentManager(), "example");
    }

    public void goProgression(View v) {
        Intent progress = new Intent(this, ProgresionPatientActivity.class);
        Boolean test = AudioPlay.isIsplayingAudio();
        progress.putExtra("music", test);
        startActivity(progress);

    }

    public void goGameSelecction(View v) {
        Intent gameSelecctionIntent = new Intent(this, GameSelecctionActivity.class);
        gameSelecctionIntent.putExtra("music", AudioPlay.isIsplayingAudio());
        startActivity(gameSelecctionIntent);
    }


    public void Silenciar(View v) {
        if (AudioPlay.isIsplayingAudio()) {
            AudioPlay.stopAudio();
            audio.setImageResource(R.drawable.mute);
        } else {
            AudioPlay.restart();
            audio.setImageResource(R.drawable.nomute);
        }
    }


    public void goBack(View v) {
        finish();
    }

    private void findElements(){

        levelText = findViewById(R.id.level);
        namePatientText = findViewById(R.id.namePatientId);
        iconPatient = findViewById(R.id.iconPatientId);
        namePatientText.setText(namePatient);

        audio = findViewById(R.id.nomuteHome);

        btnJugar = findViewById(R.id.button11);
        btnsett = findViewById(R.id.button13);
        btnProgresion = findViewById(R.id.progressBtnId);


        constraintLayout = findViewById(R.id.homePatient);

    }


    public void applyTexts(String number, int x, int y) {

        int numeroInt = Integer.parseInt(number);

        if ((x + y) == numeroInt) {
            Intent settingPatient = new Intent(this, SettingsPatientActivity.class);
            startActivity(settingPatient);
        } else {
            Toast.makeText(getApplicationContext(), "Suma Incorrecta", Toast.LENGTH_LONG).show();
        }
    }

    public void setLvl(){
        databaseReference.child("userPatient").child(namePatient).child("stadistic").addValueEventListener(new ValueEventListener() {
            double totalbien = 0;
            int nuevolvl = 0;
            String dificilDB;
            String normalDB;
            String facilDB;
            int facil,normal,dificil;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot objDataSnapshot : snapshot.getChildren()) {
                    if(Objects.equals(objDataSnapshot.getKey(), "syllables")){
                        dificilDB = objDataSnapshot.child("succes").getValue().toString();
                        dificil += Integer.parseInt(dificilDB);
                    }else{
                        dificilDB = objDataSnapshot.child("difficulties").child("DIFÍCIL").child("succes").getValue().toString();
                        normalDB = objDataSnapshot.child("difficulties").child("NORMAL").child("succes").getValue().toString();
                        facilDB = objDataSnapshot.child("difficulties").child("FÁCIL").child("succes").getValue().toString();
                        facil += Integer.parseInt(facilDB);
                        normal += Integer.parseInt(normalDB);
                        dificil += Integer.parseInt(dificilDB);
                    }

                }
                totalbien = facil + (normal*2) + (dificil*3);
                totalbien = totalbien / 20;
                nuevolvl = (int) Math.floor(totalbien);
                if(nuevolvl < 1){
                    nuevolvl = 1;
                }
                //dar valor en firebase
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        Boolean valor = AudioPlay.isIsplayingAudio();
        AudioPlay.stopAudio();
        if(valor){
            AudioPlay.setIsplayingAudio(true);
        }
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Boolean valor = AudioPlay.isIsplayingAudio();
        if(valor){
            AudioPlay.restart();
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namePatient = preferences.getString("userPatient", "null");

        namePatientText.setText(namePatient);

        if (namePatient.equals("null")) {
            finish();
        }
        super.onRestart();
    }

/*    protected void onRestart() {
        Boolean valor = getIntent().getExtras().getBoolean("music");
        if(valor){
            AudioPlay.restart();
        }
        super.onRestart();
    }*/
}