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

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.ErrorActivity;
import com.example.leeconmonclick.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.leerconmonclick.util.AudioPlay;

public class GameSelecctionActivity extends AppCompatActivity {


    private DatabaseReference databaseReference;
    private final Context context = this;
    private CircleImageView iconPatient;
    private TextView namePatientTxtView,btn1,btn2,btn3,titlePage;
    private String namePatient;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selecction);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findElements();
        getSettings();

        music();

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
                Intent intent = new Intent(GameSelecctionActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });
    }

    private void music(){
        boolean valor = getIntent().getExtras().getBoolean("music");
        if(valor){
            AudioPlay.restart();
        }
    }

    private void getSettings(){

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.gameSelect);

        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                switch (size) {
                    case "grande":
                        btn1.setTextSize(30);
                        btn2.setTextSize(30);
                        btn3.setTextSize(30);
                        titlePage.setTextSize(30);
                        namePatientTxtView.setTextSize(30);
                        break;
                    case "normal":
                        btn1.setTextSize(20);
                        btn2.setTextSize(20);
                        btn3.setTextSize(20);
                        titlePage.setTextSize(20);
                        namePatientTxtView.setTextSize(20);
                        break;
                    case "peque":
                        btn1.setTextSize(10);
                        btn2.setTextSize(10);
                        btn3.setTextSize(10);
                        titlePage.setTextSize(10);
                        namePatientTxtView.setTextSize(10);
                        break;
                }
                String dalto = snapshot.child("sett").child("1").getValue().toString();
                if(dalto.equals("tritanopia")){
                    btn1.setBackgroundResource(R.drawable.button_style_tritano);
                    btn2.setBackgroundResource(R.drawable.button_style_tritano);
                    btn3.setBackgroundResource(R.drawable.button_style_tritano);
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

        iconPatient = findViewById(R.id.iconPatientId);
        namePatientTxtView = findViewById(R.id.namePatientId);

        btn1 = findViewById(R.id.game1btnId);
        btn2 = findViewById(R.id.game2btnId);
        btn3 = findViewById(R.id.game3btnId);
        titlePage = findViewById(R.id.tittleActivityAddNoteId2);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namePatient = preferences.getString("userPatient","null").toLowerCase(Locale.ROOT);

        namePatientTxtView.setText(namePatient);



    }

    public void goGameJoin(View v){
        Intent categoryIntent = new Intent(this,CategorySelecctionActivity.class);
        categoryIntent.putExtra("game","j");
        categoryIntent.putExtra("music", AudioPlay.isIsplayingAudio());
        startActivity(categoryIntent);

    }

    public void goGameLetters(View v){
        Intent categoryIntent = new Intent(this,CategorySelecctionActivity.class);
        categoryIntent.putExtra("game","l");
        categoryIntent.putExtra("music", AudioPlay.isIsplayingAudio());
        startActivity(categoryIntent);
    }

    public void goGameSyllables(View v){
        Intent categoryIntent = new Intent(this,SyllablesGameActivity.class);
        categoryIntent.putExtra("game","s");
        categoryIntent.putExtra("music", AudioPlay.isIsplayingAudio());
        startActivity(categoryIntent);

    }

    public void goBack(View view){
        onBackPressed();
    }

    @Override
    protected void onPause() {
        boolean valor = AudioPlay.isIsplayingAudio();
        AudioPlay.stopAudio();
        if(valor){
            AudioPlay.setIsplayingAudio(true);
        }
        super.onPause();
    }

    @Override
    protected void onRestart() {
        music();
        super.onRestart();
    }

}