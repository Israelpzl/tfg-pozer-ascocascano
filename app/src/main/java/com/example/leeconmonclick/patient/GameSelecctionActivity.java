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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.AudioPlay;
import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class GameSelecctionActivity extends AppCompatActivity {


    private DatabaseReference databaseReference;
    private Context context = this;
    private CircleImageView iconPatient;
    private TextView namePatientTxtView,btn1,btn2,btn3,titlePage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selecction);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        AudioPlay.restart();

        iconPatient = findViewById(R.id.iconPatientId);
        namePatientTxtView = findViewById(R.id.namePatientId);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String namePatient = preferences.getString("userPatient","null").toLowerCase(Locale.ROOT);

        namePatientTxtView.setText(namePatient);


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
                        setContentView(R.layout.activity_error2);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setContentView(R.layout.activity_error2);
            }
        });

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.gameSelect);

        btn1 = findViewById(R.id.game1btnId);
        btn2 = findViewById(R.id.game2btnId);
        btn3 = findViewById(R.id.game3btnId);
        titlePage = findViewById(R.id.tittleActivityAddNoteId2);

        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                if(size.equals("grande")){
                    btn1.setTextSize(30);
                    btn2.setTextSize(30);
                    btn3.setTextSize(30);
                    titlePage.setTextSize(30);
                    namePatientTxtView.setTextSize(30);
                }else if(size.equals("normal")){
                    btn1.setTextSize(20);
                    btn2.setTextSize(20);
                    btn3.setTextSize(20);
                    titlePage.setTextSize(20);
                    namePatientTxtView.setTextSize(20);
                }else if(size.equals("peque")){
                    btn1.setTextSize(10);
                    btn2.setTextSize(10);
                    btn3.setTextSize(10);
                    titlePage.setTextSize(10);
                    namePatientTxtView.setTextSize(10);
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
                setContentView(R.layout.activity_error2);
            }
        });
    }

    public void goGameJoin(View v){
        Intent categoryIntent = new Intent(this,CategorySelecctionActivity.class);
        categoryIntent.putExtra("game","j");
        startActivity(categoryIntent);

    }

    public void goGameLetters(View v){
        Intent categoryIntent = new Intent(this,CategorySelecctionActivity.class);
        categoryIntent.putExtra("game","l");
        startActivity(categoryIntent);
    }

    public void goGameSyllables(View v){
        Intent SyllablesIntent = new Intent(this,SyllablesGameActivity.class);
        SyllablesIntent.putExtra("game","s");
        startActivity(SyllablesIntent);

    }

    public void goBack(View view){
        onBackPressed();
    }

    @Override
    protected void onPause() {
        AudioPlay.stopAudio();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        AudioPlay.restart();
        super.onRestart();
    }

}