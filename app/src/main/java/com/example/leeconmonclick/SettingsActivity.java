package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        TextView userName = findViewById(R.id.editTextTextPersonNameEdit);
        ToggleButton noDaltonic = findViewById(R.id.toggleButtonNoDalto);
        ToggleButton daltonic = findViewById(R.id.toggleButtonDalto);
        ToggleButton bigSize = findViewById(R.id.toggleButtonBig);
        ToggleButton midSize = findViewById(R.id.toggleButtonMid);
        ToggleButton smallSize = findViewById(R.id.toggleButtonSmall);


        getIcons();
        setIconProfesional();


        user = db.getCurrentUser();
        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();



        databaseReference.child("Users").child(userCollection).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                userName.setText(dataSnapshot.child("nombre").getValue().toString());
                //String icon = dataSnapshot.child("icono").getValue().toString();   Pendiente de como se programa los iconos
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

        databaseReference.child("Users").child(userCollection).child("nombre").setValue(name);
        databaseReference.child("Users").child(userCollection).child("daltonismo").setValue(dalto);
        databaseReference.child("Users").child(userCollection).child("tamanio").setValue(size);

        Toast.makeText(getApplicationContext(),"Datos guardados correctamente",Toast.LENGTH_LONG).show();
        finish();
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

    private void setIconProfesional(){

        maleDoctorIcon = findViewById(R.id.maleDoctorIconId);
        femaleDoctorIcon = findViewById(R.id.femaleDoctorIconId);
        maleProfesorIcon = findViewById(R.id.maleProfesorIconId);
        femaleProfesorIcon = findViewById(R.id.femaleProfesorIconId);
        
        maleDoctorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Users").child(userCollection).child("setting").child("icon").setValue("https://firebasestorage.googleapis.com/v0/b/leerconmonclick.appspot.com/o/iconos%2Fdoctor.png?alt=media&token=7ce24f3a-e556-4cc7-88be-8ee92e4f4416");
            }
        });

        femaleDoctorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Users").child(userCollection).child("setting").child("icon").setValue("https://firebasestorage.googleapis.com/v0/b/leerconmonclick.appspot.com/o/iconos%2Fdoctora.png?alt=media&token=22fc8752-0568-4bbb-a891-60e47358d9c0");
            }
        });

        maleProfesorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Users").child(userCollection).child("setting").child("icon").setValue("https://firebasestorage.googleapis.com/v0/b/leerconmonclick.appspot.com/o/iconos%2Fprofesor.png?alt=media&token=3122b844-f470-46b8-88bd-52ddeda87c28");
            }
        });

        femaleProfesorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Users").child(userCollection).child("setting").child("icon").setValue(femaleProfesorImg);
            }
        });

    }

    private void getIcons(){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recuperando Iconos");
        progressDialog.show();

        storageReference.child("iconos/doctor"+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                maleDoctorImg = uri.toString();
            }
        });


        storageReference.child("iconos/doctora"+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                femaleDoctorImg = uri.toString();
            }
        });

        storageReference.child("iconos/profesor"+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                maleProfesorImg = uri.toString();
            }
        });

        storageReference.child("iconos/profesora"+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                femaleProfesorImg = uri.toString();
            }
        });

        progressDialog.dismiss();
    }





}