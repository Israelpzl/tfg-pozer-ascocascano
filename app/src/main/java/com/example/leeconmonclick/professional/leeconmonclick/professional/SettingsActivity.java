package com.example.leeconmonclick.professional.leeconmonclick.professional;

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
import android.widget.Toast;
import android.widget.ToggleButton;

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

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.leerconmonclick.util.AudioPlay;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    private CircleImageView maleDoctorIcon,femaleDoctorIcon,maleProfesorIcon,femaleProfesorIcon;

    private String userCollection;

    private TextView userName;
    private ToggleButton noDaltonic,daltonic,bigSize,midSize,smallSize;
    private final Context context= this;
    private String icon;

    //Texts
    private TextView titleText;
    private TextView textName;
    private TextView iconText;
    private TextView daltoText;
    private TextView syzeText;
    private TextView saveText;
    private TextView logOutText;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findElements();
        getSettings();

        getIcons();
        settings();
        setIconProfesional();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(SettingsActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });
    }

    public void saveChanges(View v){

        TextView userName = findViewById(R.id.editNameUser);
        ToggleButton noDaltonic = findViewById(R.id.toggleButtonNoDalto);
        ToggleButton daltonic = findViewById(R.id.toggleButtonDalto);
        ToggleButton bigSize = findViewById(R.id.toggleButtonBig);
        ToggleButton midSize = findViewById(R.id.toggleButtonMid);
        ToggleButton smallSize = findViewById(R.id.toggleButtonSmall);

        //Nombre
        String name = userName.getText().toString();
        //Daltonismo
        String dalto;
        if(noDaltonic.isChecked()){
            dalto = "no";
        }else if(daltonic.isChecked()){
            dalto = "tritanopia";
        }else{
            dalto = "no";
        }
        //Tamaño letra
        String size;
        if(bigSize.isChecked()){
            size = "grande";
        }else if(midSize.isChecked()){
            size = "normal";
        }else if(smallSize.isChecked()){
            size = "peque";
        }else{
            size = "normal";
        }
        //Icono
        String icon = setIconProfesional();

        databaseReference.child("Users").child(userCollection).child("icon").setValue(icon);
        databaseReference.child("Users").child(userCollection).child("nombre").setValue(name);
        databaseReference.child("Users").child(userCollection).child("sett").child("1").setValue(dalto);
        databaseReference.child("Users").child(userCollection).child("sett").child("0").setValue(size);

        Toast.makeText(getApplicationContext(),"Datos guardados correctamente",Toast.LENGTH_LONG).show();
        finish();

    }

    public void settings(){

        databaseReference.child("Users").child(userCollection).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                userName.setText(Objects.requireNonNull(dataSnapshot.child("nombre").getValue()).toString());
                // Pendiente de como se programa los iconos
                String icon = Objects.requireNonNull(dataSnapshot.child("icon").getValue()).toString();
                if (icon.equals("maleDoctor") || icon.equals("maleDoctorTritano")){
                    maleDoctorIcon.setBackgroundResource(R.drawable.bg_select_icon);
                }else if(icon.equals("femaleDoctor") || icon.equals("femaleDoctorTritano")){
                    femaleDoctorIcon.setBackgroundResource(R.drawable.bg_select_icon);
                }else if(icon.equals("maleProfesor") || icon.equals("maleProfesorTritano")){
                    maleProfesorIcon.setBackgroundResource(R.drawable.bg_select_icon);
                }else if(icon.equals("femaleProfesor") || icon.equals("femaleProfesorTritano")){
                    femaleProfesorIcon.setBackgroundResource(R.drawable.bg_select_icon);
                }
                //Seccion para daltonismo
                String daltonism = Objects.requireNonNull(dataSnapshot.child("sett").child("1").getValue()).toString();
                if(daltonism.equals("no")){
                    noDaltonic.setChecked(true);
                }else{
                    daltonic.setChecked(true);
                }
                //Seccion tamaño
                String size = Objects.requireNonNull(dataSnapshot.child("sett").child("0").getValue()).toString();
                if(size.equals("grande")){
                    bigSize.setChecked(true);
                }else if(size.equals("normal")){
                    midSize.setChecked(true);
                }else{
                    smallSize.setChecked(true);
                }

            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    public void changeDalto(View v){
        ToggleButton noDaltonic = findViewById(R.id.toggleButtonNoDalto);
        ToggleButton daltonic = findViewById(R.id.toggleButtonDalto);
        switch (v.getId()){
            case R.id.toggleButtonNoDalto:
                daltonic.setChecked(false);
                break;
            case R.id.toggleButtonDalto:
                noDaltonic.setChecked(false);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void changeSize(View v){
        ToggleButton bigSize = findViewById(R.id.toggleButtonBig);
        ToggleButton midSize = findViewById(R.id.toggleButtonMid);
        ToggleButton smallSize = findViewById(R.id.toggleButtonSmall);
        switch (v.getId()){
            case R.id.toggleButtonBig:
                midSize.setChecked(false);
                smallSize.setChecked(false);
                break;
            case R.id.toggleButtonMid:
                bigSize.setChecked(false);
                smallSize.setChecked(false);
                break;
            case R.id.toggleButtonSmall:
                bigSize.setChecked(false);
                midSize.setChecked(false);
                break;
        }
    }

    public void goBack(View view){finish();}

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

    private String setIconProfesional(){

        databaseReference.child("Users").child(userCollection).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                icon = dataSnapshot.child("icon").getValue().toString();
                String tritano = dataSnapshot.child("sett").child("1").getValue().toString();

                maleDoctorIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        maleDoctorIcon.setBackgroundResource(R.drawable.bg_select_icon);
                        maleProfesorIcon.setBackground(null);
                        femaleDoctorIcon.setBackground(null);
                        femaleProfesorIcon.setBackground(null);
                        if(tritano.equals("no")){
                            icon = "maleDoctor";
                        }else{
                            icon = "maleDoctorTritano";
                        }
                    }
                });

                femaleDoctorIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        femaleDoctorIcon.setBackgroundResource(R.drawable.bg_select_icon);
                        maleDoctorIcon.setBackground(null);
                        maleProfesorIcon.setBackground(null);
                        femaleProfesorIcon.setBackground(null);
                        if(tritano.equals("no")){
                            icon = "femaleDoctor";
                        }else{
                            icon = "femaleDoctorTritano";
                        }
                    }
                });

                maleProfesorIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        maleProfesorIcon.setBackgroundResource(R.drawable.bg_select_icon);
                        femaleDoctorIcon.setBackground(null);
                        maleDoctorIcon.setBackground(null);
                        femaleProfesorIcon.setBackground(null);
                        if(tritano.equals("no")){
                            icon = "maleProfesor";
                        }else{
                            icon = "maleProfesorTritano";
                        }
                    }
                });

                femaleProfesorIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        femaleProfesorIcon.setBackgroundResource(R.drawable.bg_select_icon);
                        femaleDoctorIcon.setBackground(null);
                        maleDoctorIcon.setBackground(null);
                        maleProfesorIcon.setBackground(null);
                        if(tritano.equals("no")){
                            icon = "femaleProfesor";
                        }else{
                            icon = "femaleProfesorTritano";
                        }
                    }
                });
            }
        });
        return icon;
    }

    private void getIcons(){

        databaseReference.child("Users").child(userCollection).child("sett").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("1").getValue().toString().equals("no")){
                    iconsNormal();
                }else{
                    iconsTritano();
                }
            }
        });
    }

    private void iconsNormal(){
        databaseReference.child("iconImg").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Glide.with(context).load(dataSnapshot.child("maleDoctor").getValue().toString()).into(maleDoctorIcon);
                Glide.with(context).load(dataSnapshot.child("femaleDoctor").getValue().toString()).into(femaleDoctorIcon);
                Glide.with(context).load(dataSnapshot.child("maleProfesor").getValue().toString()).into(maleProfesorIcon);
                Glide.with(context).load(dataSnapshot.child("femaleProfesor").getValue().toString()).into(femaleProfesorIcon);
            }
        });
    }

    private void iconsTritano(){
        databaseReference.child("iconImg").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Glide.with(context).load(dataSnapshot.child("maleDoctorTritano").getValue().toString()).into(maleDoctorIcon);
                Glide.with(context).load(dataSnapshot.child("femaleDoctorTritano").getValue().toString()).into(femaleDoctorIcon);
                Glide.with(context).load(dataSnapshot.child("maleProfesorTritano").getValue().toString()).into(maleProfesorIcon);
                Glide.with(context).load(dataSnapshot.child("femaleProfesorTritano").getValue().toString()).into(femaleProfesorIcon);
            }
        });
    }

    private void getSettings(){

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.settings);

        databaseReference.child("Users").child(userCollection).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                switch (size) {
                    case "grande":
                        userName.setTextSize(30);
                        titleText.setTextSize(30);
                        textName.setTextSize(30);
                        iconText.setTextSize(30);
                        daltoText.setTextSize(30);
                        syzeText.setTextSize(30);
                        saveText.setTextSize(30);
                        logOutText.setTextSize(30);
                        noDaltonic.setTextSize(30);
                        daltonic.setTextSize(30);
                        bigSize.setTextSize(30);
                        midSize.setTextSize(30);
                        smallSize.setTextSize(30);
                        break;
                    case "normal":
                        userName.setTextSize(20);
                        titleText.setTextSize(20);
                        textName.setTextSize(20);
                        iconText.setTextSize(20);
                        daltoText.setTextSize(20);
                        syzeText.setTextSize(20);
                        saveText.setTextSize(20);
                        logOutText.setTextSize(20);
                        noDaltonic.setTextSize(20);
                        daltonic.setTextSize(20);
                        bigSize.setTextSize(20);
                        midSize.setTextSize(20);
                        smallSize.setTextSize(20);
                        break;
                    case "peque":
                        userName.setTextSize(10);
                        titleText.setTextSize(10);
                        textName.setTextSize(10);
                        iconText.setTextSize(10);
                        daltoText.setTextSize(10);
                        syzeText.setTextSize(10);
                        saveText.setTextSize(10);
                        logOutText.setTextSize(10);
                        noDaltonic.setTextSize(10);
                        daltonic.setTextSize(10);
                        bigSize.setTextSize(10);
                        midSize.setTextSize(10);
                        smallSize.setTextSize(10);
                        break;
                }
                String dalto = snapshot.child("sett").child("1").getValue().toString();
                if(dalto.equals("tritanopia")){
                    constraintLayout.setBackgroundResource(R.color.background_tritano);
                    logOutText.setBackgroundResource(R.drawable.button_style_red_tritano);
                    saveText.setBackgroundResource(R.drawable.button_style_tritano);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setContentView(R.layout.activity_error2);
            }
        });
    }

    private void findElements(){

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        userName = findViewById(R.id.editNameUser);
        noDaltonic = findViewById(R.id.toggleButtonNoDalto);
        daltonic = findViewById(R.id.toggleButtonDalto);
        bigSize = findViewById(R.id.toggleButtonBig);
        midSize = findViewById(R.id.toggleButtonMid);
        smallSize = findViewById(R.id.toggleButtonSmall);

        titleText = findViewById(R.id.textView13);
        textName = findViewById(R.id.textViewName);
        iconText = findViewById(R.id.textViewIcon);
        daltoText = findViewById(R.id.textViewDaltonism);
        syzeText = findViewById(R.id.textViewSize);
        saveText = findViewById(R.id.buttonSaveChanges);
        logOutText = findViewById(R.id.buttonLogOut);

        maleDoctorIcon = findViewById(R.id.maleDoctorIconId);
        femaleDoctorIcon = findViewById(R.id.femaleDoctorIconId);
        maleProfesorIcon = findViewById(R.id.maleProfesorIconId);
        femaleProfesorIcon = findViewById(R.id.femaleProfesorIconId);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();

    }

    public void help(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

    public void logOut(View v) {
        saveStateSession();
        firebaseAuth.signOut();
        Intent profileIntent = new Intent(this, ProfilesActivity.class);
        startActivity(profileIntent);
        finish();
    }
    public void saveStateSession(){
        Toast.makeText(getApplicationContext(),"LogOut",Toast.LENGTH_LONG).show();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.putString("user","null");
        editor.apply();
    }

    @Override
    protected void onRestart() {
        getIcons();
        super.onRestart();
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