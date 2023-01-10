package com.example.leeconmonclick.patient;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.ProfilesActivity;
import com.example.leeconmonclick.R;
import com.example.leeconmonclick.professional.leeconmonclick.professional.HomeProfesionalActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsPatientActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private static final String STRING_PREFERENCES = "leeconmonclick.login";
    private static final String PREFERENCES_STATE_BUTTON = "leeconmonclick.login.button";

    private String userCollection;
    private FirebaseUser user;
    private StorageReference storageReference;

    private TextView userName;
    private ToggleButton noDaltonic,daltonic,bigSize,midSize,smallSize;
    private Context context= this;
    private String icon;

    //Texts
    private TextView titleText;
    private TextView textName;
    private TextView daltoText;
    private TextView syzeText;
    private TextView saveText;
    private TextView logOutText;
    private String namePatient;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_patient);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.settings);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namePatient = preferences.getString("userPatient","null");

        userName = findViewById(R.id.editTextTextPersonNameEditPatient);
        noDaltonic = findViewById(R.id.toggleButtonNoDaltoPatient);
        daltonic = findViewById(R.id.toggleButtonDaltoPatient);
        bigSize = findViewById(R.id.toggleButtonBigPatient);
        midSize = findViewById(R.id.toggleButtonMidPatient);
        smallSize = findViewById(R.id.toggleButtonSmallPatient);

        titleText = findViewById(R.id.textViewTitlePatien);
        textName = findViewById(R.id.textViewNamePatient);
        daltoText = findViewById(R.id.textViewDaltonismPatient);
        syzeText = findViewById(R.id.textViewSizePatient);
        saveText = findViewById(R.id.buttonSaveChangesPatient);
        logOutText = findViewById(R.id.buttonLogOutPatient);

        getSettings();

        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                if(size.equals("grande")){
                    userName.setTextSize(30);
                    titleText.setTextSize(30);
                    textName.setTextSize(30);
                    daltoText.setTextSize(30);
                    syzeText.setTextSize(30);
                    saveText.setTextSize(30);
                    logOutText.setTextSize(30);
                    noDaltonic.setTextSize(30);
                    daltonic.setTextSize(30);
                    bigSize.setTextSize(30);
                    midSize.setTextSize(30);
                    smallSize.setTextSize(30);
                }else if(size.equals("normal")){
                    userName.setTextSize(20);
                    titleText.setTextSize(20);
                    textName.setTextSize(20);
                    daltoText.setTextSize(20);
                    syzeText.setTextSize(20);
                    saveText.setTextSize(20);
                    logOutText.setTextSize(20);
                    noDaltonic.setTextSize(20);
                    daltonic.setTextSize(20);
                    bigSize.setTextSize(20);
                    midSize.setTextSize(20);
                    smallSize.setTextSize(20);
                }else if(size.equals("peque")){
                    userName.setTextSize(10);
                    titleText.setTextSize(10);
                    textName.setTextSize(10);
                    daltoText.setTextSize(10);
                    syzeText.setTextSize(10);
                    saveText.setTextSize(10);
                    logOutText.setTextSize(10);
                    noDaltonic.setTextSize(10);
                    daltonic.setTextSize(10);
                    bigSize.setTextSize(10);
                    midSize.setTextSize(10);
                    smallSize.setTextSize(10);
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

            }
        });
    }



    public void saveChanges(View v){

        TextView userName = findViewById(R.id.editTextTextPersonNameEditPatient);
        ToggleButton noDaltonic = findViewById(R.id.toggleButtonNoDaltoPatient);
        ToggleButton daltonic = findViewById(R.id.toggleButtonDaltoPatient);
        ToggleButton bigSize = findViewById(R.id.toggleButtonBigPatient);
        ToggleButton midSize = findViewById(R.id.toggleButtonMidPatient);
        ToggleButton smallSize = findViewById(R.id.toggleButtonSmallPatient);

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

        databaseReference.child("userPatient").child(namePatient).child("namePatient").setValue(name);
        databaseReference.child("userPatient").child(namePatient).child("sett").child("1").setValue(dalto);
        databaseReference.child("userPatient").child(namePatient).child("sett").child("0").setValue(size);

        Toast.makeText(getApplicationContext(),"Datos guardados correctamente",Toast.LENGTH_LONG).show();
        //goHome();
        finish();

    }

    public void getSettings(){

        databaseReference.child("userPatient").child(namePatient).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                userName.setText(dataSnapshot.child("namePatient").getValue().toString());
                //Seccion para daltonismo
                String daltonism = dataSnapshot.child("sett").child("1").getValue().toString();
                if(daltonism.equals("no")){
                    noDaltonic.setChecked(true);
                }else{
                    daltonic.setChecked(true);
                }
                //Seccion tamaño
                String size = dataSnapshot.child("sett").child("0").getValue().toString();
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
        ToggleButton noDaltonic = findViewById(R.id.toggleButtonNoDaltoPatient);
        ToggleButton daltonic = findViewById(R.id.toggleButtonDaltoPatient);
        switch (v.getId()){
            case R.id.toggleButtonNoDaltoPatient:
                daltonic.setChecked(false);
                break;
            case R.id.toggleButtonDaltoPatient:
                noDaltonic.setChecked(false);
                break;
        }
    }

    public void changeSize(View v){
        ToggleButton bigSize = findViewById(R.id.toggleButtonBigPatient);
        ToggleButton midSize = findViewById(R.id.toggleButtonMidPatient);
        ToggleButton smallSize = findViewById(R.id.toggleButtonSmallPatient);
        switch (v.getId()){
            case R.id.toggleButtonBigPatient:
                midSize.setChecked(false);
                smallSize.setChecked(false);
                break;
            case R.id.toggleButtonMidPatient:
                bigSize.setChecked(false);
                smallSize.setChecked(false);
                break;
            case R.id.toggleButtonSmallPatient:
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

    public void logOut(View v) {
        Toast.makeText(getApplicationContext(),"LogOut",Toast.LENGTH_LONG).show();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.putString("user","null");
        editor.putString("userPatient","null");
        editor.apply();
        Intent profilesActivity = new Intent(this, ProfilesActivity.class);
        startActivity(profilesActivity);
        finish();
    }
}