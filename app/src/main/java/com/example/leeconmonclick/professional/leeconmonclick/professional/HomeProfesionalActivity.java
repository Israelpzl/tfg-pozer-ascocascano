package com.example.leeconmonclick.professional.leeconmonclick.professional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.ErrorActivity;
import com.example.leeconmonclick.HelpActivity;
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
import es.leerconmonclick.util.AudioPlay;

public class HomeProfesionalActivity extends AppCompatActivity {

    private TextView nameProfesional;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private static final String STRING_PREFERENCES = "leeconmonclick.login";
    private static final String PREFERENCES_STATE_BUTTON = "leeconmonclick.login.button";
    private CircleImageView iconProfesional;
    private final Context context = this;

    private TextView btnPatients;
    private TextView btnContent;
    private TextView btnNotes;
    private TextView btnDates;
    private TextView btnSett;

    private String userCollection;
    private  ConstraintLayout constraintLayout;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_profesional);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findElements();
        getSettings();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(HomeProfesionalActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });


    }

    private void findElements(){


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        nameProfesional = findViewById(R.id.nameProfesionalId);
        iconProfesional = findViewById(R.id.iconPatientId);
        btnPatients = findViewById(R.id.button15);
        btnContent = findViewById(R.id.button16);
        btnSett = findViewById(R.id.button19);
        btnNotes = findViewById(R.id.button17);
        btnDates = findViewById(R.id.button18);
        constraintLayout = findViewById(R.id.home_profesional);

        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();


    }

    private void getSettings(){
        databaseReference.child("Users").child(userCollection).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                nameProfesional.setText(snapshot.child("nombre").getValue().toString());
                String icon = snapshot.child("icon").getValue().toString();

                databaseReference.child("iconImg").child(icon).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        Glide.with(context).load(dataSnapshot.getValue().toString()).into(iconProfesional);
                    }
                });
                String size = snapshot.child("sett").child("0").getValue().toString();
                switch (size) {
                    case "grande":
                        nameProfesional.setTextSize(30);
                        btnPatients.setTextSize(30);
                        btnContent.setTextSize(30);
                        btnSett.setTextSize(30);
                        btnNotes.setTextSize(30);
                        btnDates.setTextSize(30);
                        break;
                    case "normal":
                        nameProfesional.setTextSize(20);
                        btnPatients.setTextSize(20);
                        btnContent.setTextSize(20);
                        btnSett.setTextSize(20);
                        btnNotes.setTextSize(20);
                        btnDates.setTextSize(20);
                        break;
                    case "peque":
                        nameProfesional.setTextSize(10);
                        btnPatients.setTextSize(10);
                        btnContent.setTextSize(10);
                        btnSett.setTextSize(10);
                        btnNotes.setTextSize(10);
                        btnDates.setTextSize(10);
                        break;
                }
                String dalto = snapshot.child("sett").child("1").getValue().toString();
                if(dalto.equals("tritanopia")){
                    String iconText = snapshot.child("icon").getValue().toString();
                    if (iconText.indexOf("Tri") == -1){
                        iconText = iconText + "Tritano";
                    }
                    databaseReference.child("Users").child(userCollection).child("icon").setValue(iconText);
                    constraintLayout.setBackgroundResource(R.color.background_tritano);
                    btnPatients.setBackgroundResource(R.drawable.button_style_tritano);
                    btnContent.setBackgroundResource(R.drawable.button_style_tritano);
                    btnSett.setBackgroundResource(R.drawable.button_style_tritano);
                    btnNotes.setBackgroundResource(R.drawable.button_style_tritano);
                    btnDates.setBackgroundResource(R.drawable.button_style_tritano);
                }else {
                    String iconText = snapshot.child("icon").getValue().toString();
                    String[] parts = iconText.split("Trita");
                    String datePart = parts[0];
                    databaseReference.child("Users").child(userCollection).child("icon").setValue(datePart);
                    constraintLayout.setBackgroundResource(R.color.background);
                    btnPatients.setBackgroundResource(R.drawable.button_style);
                    btnContent.setBackgroundResource(R.drawable.button_style);
                    btnSett.setBackgroundResource(R.drawable.button_style);
                    btnNotes.setBackgroundResource(R.drawable.button_style);
                    btnDates.setBackgroundResource(R.drawable.button_style);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }


    public void goListPatient(View v){
        Intent helpIntent = new Intent(this, PatientListActivity.class);
        startActivity(helpIntent);
    }

    public void goListTask(View v){
        Intent helpIntent = new Intent(this, TaskListActivity.class);
        startActivity(helpIntent);
    }

    public void goContent(View v){
        Intent helpIntent = new Intent(this, ContentListActivity.class);
        startActivity(helpIntent);
    }


    public void goSettiings(View v){
        Intent helpIntent = new Intent(this, SettingsActivity.class);
        startActivity(helpIntent);
    }



    public void goNotes(View v){
        Intent helpIntent = new Intent(this, NotesListActivity.class);
        startActivity(helpIntent);
    }


    public void logOut(View v) {
        saveStateSession();
        FirebaseAuth.getInstance().signOut();
        Intent profileIntent = new Intent(this, ProfilesActivity.class);
        startActivity(profileIntent);
    }


    public void saveStateSession(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCES_STATE_BUTTON,false).apply();
    }


    @Override
    protected void onRestart() {
        super.onRestart();

       FirebaseUser u =  firebaseAuth.getCurrentUser();
       if (u == null){
           finish();
       }
       getSettings();
    }

    @Override
    protected void onPause() {
        boolean valor = AudioPlay.isIsplayingAudio();
        if(valor){
            AudioPlay.stopAudio();
        }
        super.onPause();
    }
}