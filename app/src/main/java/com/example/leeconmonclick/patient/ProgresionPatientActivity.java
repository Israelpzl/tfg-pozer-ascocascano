package com.example.leeconmonclick.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.AudioPlay;
import com.example.leeconmonclick.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProgresionPatientActivity extends AppCompatActivity {

    private TextView levelText,namePatientText,titleText;
    private Context context= this;

    private TextView lvl1,lvl2,lvl3,lvl4,lvl5,lvl6,saveChang;

    private CircleImageView ima1Lvl1,ima2Lvl1,ima1Lvl2,ima2Lvl2,ima3Lvl2,ima1Lvl3,ima2Lvl3,ima1Lvl4,ima2Lvl4,ima3Lvl4,ima1Lvl5,ima2Lvl5,ima1Lvl6,ima2Lvl6,ima3Lvl6,imagePatient;
    private String namePatient;
    private String icon;

    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progresion_patient);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.progresionPatient);

        databaseReference = FirebaseDatabase.getInstance().getReference();


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namePatient = preferences.getString("userPatient","null");
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
                setContentView(R.layout.activity_error2);
            }
        });

        getIcons();
        setIconPatient();
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
            }
        });



        ima1Lvl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ima1Lvl1.setBackgroundResource(R.drawable.bg_select_icon);
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
                icon = "1lvl1";
            }
        });

        ima2Lvl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ima1Lvl1.setBackground(null);
                ima2Lvl1.setBackgroundResource(R.drawable.bg_select_icon);
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
                icon = "1lvl2";

            }
        });

        ima1Lvl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ima1Lvl1.setBackground(null);
                ima2Lvl1.setBackground(null);
                ima1Lvl2.setBackgroundResource(R.drawable.bg_select_icon);
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
                icon = "2lvl1";

            }
        });

        ima2Lvl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ima1Lvl1.setBackground(null);
                ima2Lvl1.setBackground(null);
                ima1Lvl2.setBackground(null);
                ima2Lvl2.setBackgroundResource(R.drawable.bg_select_icon);
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
                icon = "2lvl2";

            }
        });

        ima3Lvl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ima1Lvl1.setBackground(null);
                ima2Lvl1.setBackground(null);
                ima1Lvl2.setBackground(null);
                ima2Lvl2.setBackground(null);
                ima3Lvl2.setBackgroundResource(R.drawable.bg_select_icon);
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
                icon = "2lvl3";

            }
        });

        ima1Lvl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ima1Lvl1.setBackground(null);
                ima2Lvl1.setBackground(null);
                ima1Lvl2.setBackground(null);
                ima2Lvl2.setBackground(null);
                ima3Lvl2.setBackground(null);
                ima1Lvl3.setBackgroundResource(R.drawable.bg_select_icon);
                ima2Lvl3.setBackground(null);
                ima1Lvl4.setBackground(null);
                ima2Lvl4.setBackground(null);
                ima3Lvl4.setBackground(null);
                ima1Lvl5.setBackground(null);
                ima2Lvl5.setBackground(null);
                ima1Lvl6.setBackground(null);
                ima2Lvl6.setBackground(null);
                ima3Lvl6.setBackground(null);
                icon = "3lvl1";

            }
        });

        ima2Lvl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ima1Lvl1.setBackground(null);
                ima2Lvl1.setBackground(null);
                ima1Lvl2.setBackground(null);
                ima2Lvl2.setBackground(null);
                ima3Lvl2.setBackground(null);
                ima1Lvl3.setBackground(null);
                ima2Lvl3.setBackgroundResource(R.drawable.bg_select_icon);
                ima1Lvl4.setBackground(null);
                ima2Lvl4.setBackground(null);
                ima3Lvl4.setBackground(null);
                ima1Lvl5.setBackground(null);
                ima2Lvl5.setBackground(null);
                ima1Lvl6.setBackground(null);
                ima2Lvl6.setBackground(null);
                ima3Lvl6.setBackground(null);
                icon = "3lvl2";

            }
        });

        ima1Lvl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ima1Lvl1.setBackground(null);
                ima2Lvl1.setBackground(null);
                ima1Lvl2.setBackground(null);
                ima2Lvl2.setBackground(null);
                ima3Lvl2.setBackground(null);
                ima1Lvl3.setBackground(null);
                ima2Lvl3.setBackground(null);
                ima1Lvl4.setBackgroundResource(R.drawable.bg_select_icon);
                ima2Lvl4.setBackground(null);
                ima3Lvl4.setBackground(null);
                ima1Lvl5.setBackground(null);
                ima2Lvl5.setBackground(null);
                ima1Lvl6.setBackground(null);
                ima2Lvl6.setBackground(null);
                ima3Lvl6.setBackground(null);
                icon = "4lvl1";

            }
        });

        ima2Lvl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ima1Lvl1.setBackground(null);
                ima2Lvl1.setBackground(null);
                ima1Lvl2.setBackground(null);
                ima2Lvl2.setBackground(null);
                ima3Lvl2.setBackground(null);
                ima1Lvl3.setBackground(null);
                ima2Lvl3.setBackground(null);
                ima1Lvl4.setBackground(null);
                ima2Lvl4.setBackgroundResource(R.drawable.bg_select_icon);
                ima3Lvl4.setBackground(null);
                ima1Lvl5.setBackground(null);
                ima2Lvl5.setBackground(null);
                ima1Lvl6.setBackground(null);
                ima2Lvl6.setBackground(null);
                ima3Lvl6.setBackground(null);
                icon = "4lvl2";

            }
        });

        ima3Lvl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ima1Lvl1.setBackground(null);
                ima2Lvl1.setBackground(null);
                ima1Lvl2.setBackground(null);
                ima2Lvl2.setBackground(null);
                ima3Lvl2.setBackground(null);
                ima1Lvl3.setBackground(null);
                ima2Lvl3.setBackground(null);
                ima1Lvl4.setBackground(null);
                ima2Lvl4.setBackground(null);
                ima3Lvl4.setBackgroundResource(R.drawable.bg_select_icon);
                ima1Lvl5.setBackground(null);
                ima2Lvl5.setBackground(null);
                ima1Lvl6.setBackground(null);
                ima2Lvl6.setBackground(null);
                ima3Lvl6.setBackground(null);
                icon = "4lvl3";

            }
        });

        ima1Lvl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                ima1Lvl5.setBackgroundResource(R.drawable.bg_select_icon);
                ima2Lvl5.setBackground(null);
                ima1Lvl6.setBackground(null);
                ima2Lvl6.setBackground(null);
                ima3Lvl6.setBackground(null);
                icon = "5lvl1";

            }
        });

        ima2Lvl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                ima2Lvl5.setBackgroundResource(R.drawable.bg_select_icon);
                ima1Lvl6.setBackground(null);
                ima2Lvl6.setBackground(null);
                ima3Lvl6.setBackground(null);
                icon = "5lvl2";

            }
        });

        ima1Lvl6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                ima1Lvl6.setBackgroundResource(R.drawable.bg_select_icon);
                ima2Lvl6.setBackground(null);
                ima3Lvl6.setBackground(null);
                icon = "6lvl1";

            }
        });

        ima2Lvl6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                ima2Lvl6.setBackgroundResource(R.drawable.bg_select_icon);
                ima3Lvl6.setBackground(null);
                icon = "6lvl2";

            }
        });

        ima3Lvl6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                ima3Lvl6.setBackgroundResource(R.drawable.bg_select_icon);
                icon = "6lvl3";

            }
        });

        return icon;

    }

    public void goBack(View v){
        finish();
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
}