package com.example.leeconmonclick.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class GameSelecctionActivity extends AppCompatActivity {

    private Spinner spinner;
    private ArrayAdapter<String> adapterSpinner;
    private DatabaseReference databaseReference;
    private Context context = this;
    private CircleImageView iconPatient;
    private TextView namePatientTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selecction);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        spinner = (Spinner) findViewById(R.id.spinnerId);
        String[] opciones = {"PRÁCTICA", "FÁCIL", "NORMAL", "DIFÍCIL"};

        adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapterSpinner);
        // spinner.getSelectedItem().toString()

        iconPatient = findViewById(R.id.iconPatientId);
        namePatientTxtView = findViewById(R.id.namePatientId);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String namePatient = preferences.getString("userName","null");

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

    public void goGameJoin(View v){
        Intent joinIntent = new Intent(this, JoinWordsGameActivity.class);
        startActivity(joinIntent);
    }

    public void goBack(View view){
        onBackPressed();
    }

}