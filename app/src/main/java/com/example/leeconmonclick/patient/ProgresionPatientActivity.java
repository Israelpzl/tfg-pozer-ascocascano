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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.ErrorActivity;
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

public class ProgresionPatientActivity extends AppCompatActivity {

    private TextView levelText,namePatientText,titleText;
    private Context context= this;

    private TextView lvl1,lvl2,lvl3,lvl4,lvl5,lvl6,saveChang,lvlText;

    private CircleImageView ima1Lvl1,ima2Lvl1,ima1Lvl2,ima2Lvl2,ima3Lvl2,ima1Lvl3,ima2Lvl3,ima1Lvl4,ima2Lvl4,ima3Lvl4,ima1Lvl5,ima2Lvl5,ima1Lvl6,ima2Lvl6,ima3Lvl6,imagePatient;
    private String namePatient;
    private String icon;
    private int actualLvl;
    private ProgressBar pb;

    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progresion_patient);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.progresionPatient);

        Boolean valor = getIntent().getExtras().getBoolean("music");
        if(valor){
            AudioPlay.restart();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namePatient = preferences.getString("userPatient","null").toLowerCase(Locale.ROOT);
        /*levelText = findViewById(R.id.level);*/
        namePatientText = findViewById(R.id.namePatientId);
        namePatientText.setText(namePatient);

        imagePatient = findViewById(R.id.iconPatientProgresionId);

        //accesibility settings
        titleText = findViewById(R.id.textViewProgresionTitle);
        lvl1 = findViewById(R.id.textView10);
        lvl2 = findViewById(R.id.textView18);
        lvl3 = findViewById(R.id.textView19);
        lvl4 = findViewById(R.id.textView20);
        lvl5 = findViewById(R.id.textView21);
        lvl6 = findViewById(R.id.textView22);
        saveChang = findViewById(R.id.buttonSaveProgresionPatient2);
        pb = findViewById(R.id.lvlProgressBar);
        lvlText = findViewById(R.id.actualLvlPatient);

        pb.setMax(100);

        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int progreso = Integer.parseInt(snapshot.child("progression").getValue().toString());
                pb.setProgress(progreso);
                lvlText.setText("NIVEL ACTUAL: "+snapshot.child("lvlPatient").getValue().toString()+"."+snapshot.child("progression").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //icono
                String icon = snapshot.child("icon").getValue().toString();

                databaseReference.child("iconPatient").child(icon).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if(!ProgresionPatientActivity.this.isFinishing()){
                            Glide.with(context).load(dataSnapshot.getValue().toString()).into(imagePatient);
                        }
                    }
                });

                String size = snapshot.child("sett").child("0").getValue().toString();
                if(size.equals("grande")){
                    lvl1.setTextSize(30);
                    lvl2.setTextSize(30);
                    lvl3.setTextSize(30);
                    lvl4.setTextSize(30);
                    lvl5.setTextSize(30);
                    lvl6.setTextSize(30);
                    namePatientText.setTextSize(30);
                    titleText.setTextSize(30);
                }else if(size.equals("normal")){
                    lvl1.setTextSize(20);
                    lvl2.setTextSize(20);
                    lvl3.setTextSize(20);
                    lvl4.setTextSize(20);
                    lvl5.setTextSize(20);
                    lvl6.setTextSize(20);
                    namePatientText.setTextSize(20);
                    titleText.setTextSize(20);
                }else if(size.equals("peque")){
                    lvl1.setTextSize(10);
                    lvl2.setTextSize(10);
                    lvl3.setTextSize(10);
                    lvl4.setTextSize(10);
                    lvl5.setTextSize(10);
                    lvl6.setTextSize(10);
                    namePatientText.setTextSize(10);
                    titleText.setTextSize(10);
                }
                String dalto = snapshot.child("sett").child("1").getValue().toString();
                if(dalto.equals("tritanopia")){
                    constraintLayout.setBackgroundResource(R.color.background_tritano);
                    saveChang.setBackgroundResource(R.drawable.button_style_tritano);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getIcons();
        setIconPatient();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(ProgresionPatientActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });

        opacity();
    }

    public void saveChanges(View v){

        //Icono
        icon = setIconPatient();

        databaseReference.child("userPatient").child(namePatient).child("icon").setValue(icon);

        Toast.makeText(getApplicationContext(),"Datos guardados correctamente",Toast.LENGTH_LONG).show();
        //goHome();
        finish();

    }

    private String setIconPatient(){

        databaseReference.child("userPatient").child(namePatient).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                icon = dataSnapshot.child("icon").getValue().toString();
                String patientLvl = dataSnapshot.child("lvlPatient").getValue().toString();
                actualLvl = Integer.parseInt(patientLvl);

                ima1Lvl1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        inicializateBackgroundIcon(ima1Lvl1);
                        icon = "1lvl1";
                    }
                });

                ima2Lvl1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        inicializateBackgroundIcon(ima2Lvl1);
                        icon = "1lvl2";

                    }
                });

                ima1Lvl2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (actualLvl < 2) {
                            Toast.makeText(getApplicationContext(), "Aun no tienes suficiente nivel", Toast.LENGTH_LONG).show();
                            ima1Lvl2.setClickable(false);
                        } else {

                            inicializateBackgroundIcon(ima1Lvl2);
                            icon = "2lvl1";
                        }

                    }
                });

                ima2Lvl2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (actualLvl < 2) {
                            Toast.makeText(getApplicationContext(), "Aun no tienes suficiente nivel", Toast.LENGTH_LONG).show();
                            ima1Lvl2.setClickable(false);
                        } else {
                            inicializateBackgroundIcon(ima2Lvl2);
                            icon = "2lvl2";
                        }
                    }
                });

                ima3Lvl2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (actualLvl < 2) {
                            Toast.makeText(getApplicationContext(), "Aun no tienes suficiente nivel", Toast.LENGTH_LONG).show();
                            ima1Lvl2.setClickable(false);
                        } else {
                            inicializateBackgroundIcon(ima3Lvl2);
                            icon = "2lvl3";
                        }
                    }
                });

                ima1Lvl3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (actualLvl < 3) {
                            Toast.makeText(getApplicationContext(), "Aun no tienes suficiente nivel", Toast.LENGTH_LONG).show();
                            ima1Lvl2.setClickable(false);
                        } else {
                            inicializateBackgroundIcon(ima1Lvl3);
                            icon = "3lvl1";
                        }
                    }
                });

                ima2Lvl3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (actualLvl < 3) {
                            Toast.makeText(getApplicationContext(), "Aun no tienes suficiente nivel", Toast.LENGTH_LONG).show();
                            ima1Lvl2.setClickable(false);
                        } else {
                            inicializateBackgroundIcon(ima2Lvl3);
                            icon = "3lvl2";
                        }
                    }
                });

                ima1Lvl4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (actualLvl < 4) {
                            Toast.makeText(getApplicationContext(), "Aun no tienes suficiente nivel", Toast.LENGTH_LONG).show();
                            ima1Lvl2.setClickable(false);
                        } else {
                            inicializateBackgroundIcon(ima1Lvl4);
                            icon = "4lvl1";
                        }
                    }
                });

                ima2Lvl4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (actualLvl < 4) {
                            Toast.makeText(getApplicationContext(), "Aun no tienes suficiente nivel", Toast.LENGTH_LONG).show();
                            ima1Lvl2.setClickable(false);
                        } else {
                            inicializateBackgroundIcon(ima2Lvl4);
                            icon = "4lvl2";
                        }
                    }
                });

                ima3Lvl4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (actualLvl < 4) {
                            Toast.makeText(getApplicationContext(), "Aun no tienes suficiente nivel", Toast.LENGTH_LONG).show();
                            ima1Lvl2.setClickable(false);
                        } else {
                            inicializateBackgroundIcon(ima3Lvl4);
                            icon = "4lvl3";
                        }
                    }
                });

                ima1Lvl5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (actualLvl < 5) {
                            Toast.makeText(getApplicationContext(), "Aun no tienes suficiente nivel", Toast.LENGTH_LONG).show();
                            ima1Lvl2.setClickable(false);
                        } else {
                            inicializateBackgroundIcon(ima1Lvl5);
                            icon = "5lvl1";
                        }
                    }
                });

                ima2Lvl5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (actualLvl < 5) {
                            Toast.makeText(getApplicationContext(), "Aun no tienes suficiente nivel", Toast.LENGTH_LONG).show();
                            ima1Lvl2.setClickable(false);
                        } else {
                            inicializateBackgroundIcon(ima2Lvl5);
                            ima2Lvl5.setBackgroundResource(R.drawable.bg_select_icon);
                            icon = "5lvl2";
                        }
                    }
                });

                ima1Lvl6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (actualLvl < 6) {
                            Toast.makeText(getApplicationContext(), "Aun no tienes suficiente nivel", Toast.LENGTH_LONG).show();
                            ima1Lvl2.setClickable(false);
                        } else {
                            inicializateBackgroundIcon(ima1Lvl6);
                            ima1Lvl6.setBackgroundResource(R.drawable.bg_select_icon);
                            icon = "6lvl1";
                        }
                    }
                });

                ima2Lvl6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (actualLvl < 6) {
                            Toast.makeText(getApplicationContext(), "Aun no tienes suficiente nivel", Toast.LENGTH_LONG).show();
                            ima1Lvl2.setClickable(false);
                        } else {
                            inicializateBackgroundIcon(ima2Lvl6);
                            icon = "6lvl2";
                        }
                    }
                });

                ima3Lvl6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (actualLvl < 6) {
                            Toast.makeText(getApplicationContext(), "Aun no tienes suficiente nivel", Toast.LENGTH_LONG).show();
                            ima1Lvl2.setClickable(false);
                        } else {
                            inicializateBackgroundIcon(ima3Lvl6);
                            icon = "6lvl3";
                        }
                    }
                });

            }
        });
        return icon;

    }

    public void goBack(View v){
        finish();
    }

    private void opacity(){

        float OPACITY = (float) 0.2;
        float OPACITYY = (float) 1;

        ima1Lvl2.setAlpha(OPACITY);
        ima2Lvl2.setAlpha(OPACITY);
        ima3Lvl2.setAlpha(OPACITY);
        ima1Lvl3.setAlpha(OPACITY);
        ima2Lvl3.setAlpha(OPACITY);
        ima1Lvl4.setAlpha(OPACITY);
        ima2Lvl4.setAlpha(OPACITY);
        ima3Lvl4.setAlpha(OPACITY);
        ima1Lvl5.setAlpha(OPACITY);
        ima2Lvl5.setAlpha(OPACITY);
        ima1Lvl6.setAlpha(OPACITY);
        ima2Lvl6.setAlpha(OPACITY);
        ima3Lvl6.setAlpha(OPACITY);


        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String lvl = snapshot.child("lvlPatient").getValue().toString();


                switch (lvl){
                    case "2":{
                        ima1Lvl2.setAlpha(OPACITYY);
                        ima2Lvl2.setAlpha(OPACITYY);
                        ima3Lvl2.setAlpha(OPACITYY);
                        break;
                    }
                    case "3":{
                        ima1Lvl2.setAlpha(OPACITYY);
                        ima2Lvl2.setAlpha(OPACITYY);
                        ima3Lvl2.setAlpha(OPACITYY);
                        ima1Lvl3.setAlpha(OPACITYY);
                        ima2Lvl3.setAlpha(OPACITYY);
                        break;
                    }
                    case "4":{
                        ima1Lvl2.setAlpha(OPACITYY);
                        ima2Lvl2.setAlpha(OPACITYY);
                        ima3Lvl2.setAlpha(OPACITYY);
                        ima1Lvl3.setAlpha(OPACITYY);
                        ima2Lvl3.setAlpha(OPACITYY);
                        ima1Lvl4.setAlpha(OPACITYY);
                        ima2Lvl4.setAlpha(OPACITYY);
                        ima3Lvl4.setAlpha(OPACITYY);
                        break;
                    }
                    case "5":{
                        ima1Lvl2.setAlpha(OPACITYY);
                        ima2Lvl2.setAlpha(OPACITYY);
                        ima3Lvl2.setAlpha(OPACITYY);
                        ima1Lvl3.setAlpha(OPACITYY);
                        ima2Lvl3.setAlpha(OPACITYY);
                        ima1Lvl4.setAlpha(OPACITYY);
                        ima2Lvl4.setAlpha(OPACITYY);
                        ima3Lvl4.setAlpha(OPACITYY);
                        ima1Lvl5.setAlpha(OPACITYY);
                        ima2Lvl5.setAlpha(OPACITYY);
                        break;
                    }
                    case "6":{
                        ima1Lvl2.setAlpha(OPACITYY);
                        ima2Lvl2.setAlpha(OPACITYY);
                        ima3Lvl2.setAlpha(OPACITYY);
                        ima1Lvl3.setAlpha(OPACITYY);
                        ima2Lvl3.setAlpha(OPACITYY);
                        ima1Lvl4.setAlpha(OPACITYY);
                        ima2Lvl4.setAlpha(OPACITYY);
                        ima3Lvl4.setAlpha(OPACITYY);
                        ima1Lvl5.setAlpha(OPACITYY);
                        ima2Lvl5.setAlpha(OPACITYY);
                        ima1Lvl6.setAlpha(OPACITYY);
                        ima2Lvl6.setAlpha(OPACITYY);
                        ima3Lvl6.setAlpha(OPACITYY);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getIcons(){

        ima1Lvl1 = findViewById(R.id.iconPatientlvl1a);
        ima2Lvl1 = findViewById(R.id.iconPatientlvl1b);
        ima1Lvl2 = findViewById(R.id.iconPatientlvl2a);
        ima2Lvl2 = findViewById(R.id.iconPatientlvl2b);
        ima3Lvl2 = findViewById(R.id.iconPatientlvl2c);
        ima1Lvl3 = findViewById(R.id.iconPatientlvl3a);
        ima2Lvl3 = findViewById(R.id.iconPatientlvl3b);
        ima1Lvl4 = findViewById(R.id.iconPatientlvl4a);
        ima2Lvl4 = findViewById(R.id.iconPatientlvl4b);
        ima3Lvl4 = findViewById(R.id.iconPatientlvl4c);
        ima1Lvl5 = findViewById(R.id.iconPatientlvl5a);
        ima2Lvl5 = findViewById(R.id.iconPatientlvl5b);
        ima1Lvl6 = findViewById(R.id.iconPatientlvl6a);
        ima2Lvl6 = findViewById(R.id.iconPatientlvl6b);
        ima3Lvl6 = findViewById(R.id.iconPatientlvl6c);


        databaseReference.child("iconPatient").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Glide.with(context).load(dataSnapshot.child("1lvl1").getValue().toString()).into(ima1Lvl1);
                Glide.with(context).load(dataSnapshot.child("1lvl2").getValue().toString()).into(ima2Lvl1);
                Glide.with(context).load(dataSnapshot.child("2lvl1").getValue().toString()).into(ima1Lvl2);
                Glide.with(context).load(dataSnapshot.child("2lvl2").getValue().toString()).into(ima2Lvl2);
                Glide.with(context).load(dataSnapshot.child("2lvl3").getValue().toString()).into(ima3Lvl2);
                Glide.with(context).load(dataSnapshot.child("3lvl1").getValue().toString()).into(ima1Lvl3);
                Glide.with(context).load(dataSnapshot.child("3lvl2").getValue().toString()).into(ima2Lvl3);
                Glide.with(context).load(dataSnapshot.child("4lvl1").getValue().toString()).into(ima1Lvl4);
                Glide.with(context).load(dataSnapshot.child("4lvl2").getValue().toString()).into(ima2Lvl4);
                Glide.with(context).load(dataSnapshot.child("4lvl3").getValue().toString()).into(ima3Lvl4);
                Glide.with(context).load(dataSnapshot.child("5lvl1").getValue().toString()).into(ima1Lvl5);
                Glide.with(context).load(dataSnapshot.child("5lvl2").getValue().toString()).into(ima2Lvl5);
                Glide.with(context).load(dataSnapshot.child("6lvl1").getValue().toString()).into(ima1Lvl6);
                Glide.with(context).load(dataSnapshot.child("6lvl2").getValue().toString()).into(ima2Lvl6);
                Glide.with(context).load(dataSnapshot.child("6lvl3").getValue().toString()).into(ima3Lvl6);
            }
        });
    }

    private void inicializateBackgroundIcon(CircleImageView circleImageView){

        ima1Lvl1.setBackground(null);
        ima2Lvl1.setBackground(null);
        ima1Lvl2.setBackground(null);
        ima2Lvl2.setBackground(null);
        ima3Lvl2.setBackground(null);
        ima1Lvl3.setBackground(null);
        ima2Lvl3.setBackground(null);
        ima1Lvl4.setBackground(null);
        ima2Lvl4.setBackground(null);
        ima3Lvl4.setBackground(null);
        ima1Lvl5.setBackground(null);
        ima2Lvl5.setBackground(null);
        ima1Lvl6.setBackground(null);
        ima2Lvl6.setBackground(null);
        ima3Lvl6.setBackground(null);

        circleImageView.setBackgroundResource(R.drawable.bg_select_icon);
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
        Boolean valor = getIntent().getExtras().getBoolean("music");
        if(valor){
            AudioPlay.restart();
        }
        super.onRestart();
    }
}