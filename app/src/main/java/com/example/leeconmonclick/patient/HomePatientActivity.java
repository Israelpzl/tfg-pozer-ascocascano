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
        namePatient = preferences.getString("userPatient", "null");

        findElements();

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
                        setContentView(R.layout.activity_error2);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setContentView(R.layout.activity_error2);
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
                setContentView(R.layout.activity_error2);
            }
        });

    }

    public void goSettings(View v) {
        DialogSettingPatient dialogSettingPatient = new DialogSettingPatient();
        dialogSettingPatient.show(getSupportFragmentManager(), "example");
    }

    public void goProgression(View v) {
        Intent progress = new Intent(this, ProgresionPatientActivity.class);
        startActivity(progress);

    }

    public void goGameSelecction(View v) {
        Intent gameSelecctionIntent = new Intent(this, GameSelecctionActivity.class);
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

    @Override
    protected void onRestart() {
        super.onRestart();

        if (namePatient.equals("null")) {
            finish();
        }
    }
}