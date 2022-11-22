package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth db = FirebaseAuth.getInstance();

    private static final String STRING_PREFERENCES = "leeconmonclick.login";
    private static final String PREFERENCES_STATE_BUTTON = "leeconmonclick.login.button";

    private CircleImageView maleDoctorIcon,femaleDoctorIcon,maleProfesorIcon,femaleProfesorIcon;

    private String userCollection;
    private FirebaseUser user;
    private StorageReference storageReference;
    private String maleDoctorImg, femaleDoctorImg, maleProfesorImg, femaleProfesorImg;

    private TextView userName;
    private ToggleButton noDaltonic,daltonic,bigSize,midSize,smallSize;
    private Context context= this;
    private String icon;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


        userName = findViewById(R.id.editTextTextPersonNameEdit);
        noDaltonic = findViewById(R.id.toggleButtonNoDalto);
        daltonic = findViewById(R.id.toggleButtonDalto);
        bigSize = findViewById(R.id.toggleButtonBig);
        midSize = findViewById(R.id.toggleButtonMid);
        smallSize = findViewById(R.id.toggleButtonSmall);

        user = db.getCurrentUser();
        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();

        getIcons();
        getSettings();

        setIconProfesional();



    }



    public void saveChanges(View v){

        TextView userName = findViewById(R.id.editTextTextPersonNameEdit);
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
            dalto = "acromatico";
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
        databaseReference.child("Users").child(userCollection).child("daltonismo").setValue(dalto);
        databaseReference.child("Users").child(userCollection).child("tamanio").setValue(size);

        Toast.makeText(getApplicationContext(),"Datos guardados correctamente",Toast.LENGTH_LONG).show();
        finish();
    }

    public void getSettings(){

        databaseReference.child("Users").child(userCollection).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                userName.setText(dataSnapshot.child("nombre").getValue().toString());
                // Pendiente de como se programa los iconos
                String icon = dataSnapshot.child("icon").getValue().toString();
                if (icon.equals("maleDoctor")){
                    maleDoctorIcon.setBackgroundResource(R.drawable.bg_select_icon);
                }else if(icon.equals("femaleDoctor")){
                    femaleDoctorIcon.setBackgroundResource(R.drawable.bg_select_icon);
                }else if(icon.equals("maleProfesor")){
                    maleProfesorIcon.setBackgroundResource(R.drawable.bg_select_icon);
                }else if(icon.equals("femaleProfesor")){
                    femaleProfesorIcon.setBackgroundResource(R.drawable.bg_select_icon);
                }
                //Seccion para daltonismo
                String daltonism = dataSnapshot.child("sett").child("2").getValue().toString();
                if(daltonism.equals("no")){
                    noDaltonic.setChecked(true);
                }else{
                    daltonic.setChecked(true);
                }
                //Seccion tamaño
                String size = dataSnapshot.child("sett").child("1").getValue().toString();
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


        
        maleDoctorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maleDoctorIcon.setBackgroundResource(R.drawable.bg_select_icon);
                maleProfesorIcon.setBackground(null);
                femaleDoctorIcon.setBackground(null);
                femaleProfesorIcon.setBackground(null);
                icon = "maleDoctor";
            }
        });

        femaleDoctorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                femaleDoctorIcon.setBackgroundResource(R.drawable.bg_select_icon);
                maleDoctorIcon.setBackground(null);
                maleProfesorIcon.setBackground(null);
                femaleProfesorIcon.setBackground(null);
                icon = "femaleDoctor";

            }
        });

        maleProfesorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maleProfesorIcon.setBackgroundResource(R.drawable.bg_select_icon);
                femaleDoctorIcon.setBackground(null);
                maleDoctorIcon.setBackground(null);
                femaleProfesorIcon.setBackground(null);
                icon = "maleProfesor";

            }
        });

        femaleProfesorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                femaleProfesorIcon.setBackgroundResource(R.drawable.bg_select_icon);
                femaleDoctorIcon.setBackground(null);
                maleDoctorIcon.setBackground(null);
                maleProfesorIcon.setBackground(null);
                icon = "femaleProfesor";

            }
        });

        return icon;

    }

    private void getIcons(){

        maleDoctorIcon = findViewById(R.id.maleDoctorIconId);
        femaleDoctorIcon = findViewById(R.id.femaleDoctorIconId);
        maleProfesorIcon = findViewById(R.id.maleProfesorIconId);
        femaleProfesorIcon = findViewById(R.id.femaleProfesorIconId);

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

    public void help(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
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






}