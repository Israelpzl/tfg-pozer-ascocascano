package com.example.leeconmonclick.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategorySelecctionActivity extends AppCompatActivity {

    private Spinner spinner;
    private ArrayAdapter<String> adapterSpinner;
    private Context context = this;
    private CircleImageView iconPatient;
    private DatabaseReference databaseReference;
    private Bundle data;
    private TextView namePatientTxtView;
    private String namePatient;
    private String nameProfessional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selecction);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        databaseReference = FirebaseDatabase.getInstance().getReference();


        findElement();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namePatient = preferences.getString("userName","null");


        String[] opciones = {"PRÁCTICA", "FÁCIL", "NORMAL", "DIFÍCIL"};

        adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapterSpinner);


        namePatientTxtView.setText(namePatient);
        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String icon = snapshot.child("icon").getValue().toString();
                Glide.with(context).load(icon).into(iconPatient);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            startActivity(joinIntent);

        }else if(type.equals("l")){
            Intent lettersIntent = new Intent(this, LetterGameActivity.class);
            lettersIntent.putExtra("category", "Animales");
            lettersIntent.putExtra("difficulty",  spinner.getSelectedItem().toString());
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
            startActivity(joinIntent);

        }else if(type.equals("l")){
            Intent lettersIntent = new Intent(this, LetterGameActivity.class);
            lettersIntent.putExtra("category", "Comidas");
            lettersIntent.putExtra("difficulty",  spinner.getSelectedItem().toString());
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
            startActivity(joinIntent);

        }else if(type.equals("l")){
            Intent lettersIntent = new Intent(this, LetterGameActivity.class);
            lettersIntent.putExtra("category", "Hogar");
            lettersIntent.putExtra("difficulty",  spinner.getSelectedItem().toString());
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
                                    startActivity(joinIntent);

                                }else if(type.equals("l")){
                                    Intent lettersIntent = new Intent(context, LetterGameActivity.class);
                                    lettersIntent.putExtra("category", nameProfessional);
                                    lettersIntent.putExtra("difficulty",  spinner.getSelectedItem().toString());
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
}