package com.example.leeconmonclick.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.ErrorActivity;
import com.example.leeconmonclick.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.leerconmonclick.util.AudioPlay;

public class CategorySelecctionActivity extends AppCompatActivity {

    private Spinner spinner;
    private ArrayAdapter<String> adapterSpinner;
    private Context context = this;
    private CircleImageView iconPatient;
    private DatabaseReference databaseReference;
    private Bundle data;
    private TextView namePatientTxtView,btn1,btn2,btn3,btn4,titlePage;
    private String namePatient;
    private String nameProfessional;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selecction);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Boolean valor = getIntent().getExtras().getBoolean("music");
        if(valor){
            AudioPlay.restart();
        }

        findElement();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namePatient = preferences.getString("userPatient","null").toLowerCase(Locale.ROOT);


        String[] opciones = {"PRÁCTICA", "FÁCIL", "NORMAL", "DIFÍCIL"};

        adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapterSpinner);


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

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.categorySection);

        btn1 = findViewById(R.id.button9);
        btn2 = findViewById(R.id.button5);
        btn3 = findViewById(R.id.button8);
        btn4 = findViewById(R.id.button4);
        titlePage = findViewById(R.id.tittleActivityAddNoteId3);

        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                if(size.equals("grande")){
                    btn1.setTextSize(30);
                    btn2.setTextSize(30);
                    btn3.setTextSize(30);
                    btn4.setTextSize(30);
                    titlePage.setTextSize(30);
                    namePatientTxtView.setTextSize(30);
                }else if(size.equals("normal")){
                    btn1.setTextSize(20);
                    btn2.setTextSize(20);
                    btn3.setTextSize(20);
                    btn4.setTextSize(20);
                    titlePage.setTextSize(20);
                    namePatientTxtView.setTextSize(20);
                }else if(size.equals("peque")){
                    btn1.setTextSize(10);
                    btn2.setTextSize(10);
                    btn3.setTextSize(10);
                    btn4.setTextSize(10);
                    titlePage.setTextSize(10);
                    namePatientTxtView.setTextSize(10);
                }
                String dalto = snapshot.child("sett").child("1").getValue().toString();
                if(dalto.equals("tritanopia")){
                    btn1.setBackgroundResource(R.drawable.button_style_tritano);
                    btn2.setBackgroundResource(R.drawable.button_style_tritano);
                    btn3.setBackgroundResource(R.drawable.button_style_tritano);
                    btn4.setBackgroundResource(R.drawable.button_style_tritano);
                    spinner.setBackgroundResource(R.drawable.button_style_tritano);
                    constraintLayout.setBackgroundResource(R.color.background_tritano);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(CategorySelecctionActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });
    }

    public void goAnimalsCategory(View v) {
        data = getIntent().getExtras();
        String type =  data.getString("game");

        if (type.equals("j")){

            Intent joinIntent = new Intent(this, JoinWordsGameActivity.class);
            joinIntent.putExtra("category", "Animales");
            joinIntent.putExtra("difficulty",  spinner.getSelectedItem().toString());
            joinIntent.putExtra("music", AudioPlay.isIsplayingAudio());
            startActivity(joinIntent);

        }else if(type.equals("l")){
            Intent lettersIntent = new Intent(this, LetterGameActivity.class);
            lettersIntent.putExtra("category", "Animales");
            lettersIntent.putExtra("difficulty",  spinner.getSelectedItem().toString());
            lettersIntent.putExtra("music", AudioPlay.isIsplayingAudio());
            startActivity(lettersIntent);
        }

    }

    public void goFoodCategory(View v) {

        data = getIntent().getExtras();
        String type =  data.getString("game");

        if (type.equals("j")){

            Intent joinIntent = new Intent(this, JoinWordsGameActivity.class);
            joinIntent.putExtra("category", "Comidas");
            joinIntent.putExtra("difficulty",  spinner.getSelectedItem().toString());
            joinIntent.putExtra("music", AudioPlay.isIsplayingAudio());
            startActivity(joinIntent);

        }else if(type.equals("l")){
            Intent lettersIntent = new Intent(this, LetterGameActivity.class);
            lettersIntent.putExtra("category", "Comidas");
            lettersIntent.putExtra("difficulty",  spinner.getSelectedItem().toString());
            lettersIntent.putExtra("music", AudioPlay.isIsplayingAudio());
            startActivity(lettersIntent);
        }

    }

    public void goHouseCategory(View v) {

        data = getIntent().getExtras();
        String type =  data.getString("game");

        if (type.equals("j")){

            Intent joinIntent = new Intent(this, JoinWordsGameActivity.class);
            joinIntent.putExtra("category", "Hogar");
            joinIntent.putExtra("difficulty",  spinner.getSelectedItem().toString());
            joinIntent.putExtra("music", AudioPlay.isIsplayingAudio());
            startActivity(joinIntent);

        }else if(type.equals("l")){
            Intent lettersIntent = new Intent(this, LetterGameActivity.class);
            lettersIntent.putExtra("category", "Hogar");
            lettersIntent.putExtra("difficulty",  spinner.getSelectedItem().toString());
            lettersIntent.putExtra("music", AudioPlay.isIsplayingAudio());
            startActivity(lettersIntent);
        }

    }


    public void goPersonalCategory (View view){

        data = getIntent().getExtras();
        String type = data.getString("game");

        List<String> contentList = new ArrayList<>();

        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                nameProfessional = snapshot.child("nameProfessional").getValue().toString();

                databaseReference.child("content").child(nameProfessional).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot objDataSnapshot : snapshot.getChildren()) {
                            String w = (String) objDataSnapshot.child("word").getValue();
                            String difficulty = objDataSnapshot.child("difficulty").getValue().toString();

                            if (difficulty.equals(spinner.getSelectedItem().toString())) {
                                contentList.add(w);
                            } else if (spinner.getSelectedItem().toString().equals("PRÁCTICA")) {
                                contentList.add(w);
                            }
                        }

                            if (contentList.size()>3){
                                if (type.equals("j")){

                                    Intent joinIntent = new Intent(context, JoinWordsGameActivity.class);
                                    joinIntent.putExtra("category", nameProfessional);
                                    joinIntent.putExtra("difficulty",  spinner.getSelectedItem().toString());
                                    joinIntent.putExtra("music", AudioPlay.isIsplayingAudio());
                                    startActivity(joinIntent);

                                }else if(type.equals("l")){
                                    Intent lettersIntent = new Intent(context, LetterGameActivity.class);
                                    lettersIntent.putExtra("category", nameProfessional);
                                    lettersIntent.putExtra("difficulty",  spinner.getSelectedItem().toString());
                                    lettersIntent.putExtra("music", AudioPlay.isIsplayingAudio());
                                    startActivity(lettersIntent);
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Tiene que haber más contenido personalizado", Toast.LENGTH_LONG).show();
                            }

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

    }

    private void findElement(){

        iconPatient = findViewById(R.id.iconPatientId);
        namePatientTxtView = findViewById(R.id.namePatientId);
        spinner = (Spinner) findViewById(R.id.spinnerId);
    }

    public void goBack(View v){finish();}

    @Override
    protected void onPause() {
        AudioPlay.stopAudio();
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