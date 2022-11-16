package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.leerconmonclick.util.User;

public class Settings extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseAuth db = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        TextView userName = findViewById(R.id.editTextTextPersonNameEdit);
        ToggleButton noDaltonic = findViewById(R.id.toggleButtonNoDalto);
        ToggleButton daltonic = findViewById(R.id.toggleButtonDalto);
        ToggleButton bigSize = findViewById(R.id.toggleButtonBig);
        ToggleButton midSize = findViewById(R.id.toggleButtonMid);
        ToggleButton smallSize = findViewById(R.id.toggleButtonSmall);

        FirebaseUser user = db.getCurrentUser();
        String userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];

        databaseReference.child("Users").child(userCollection).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                userName.setText(dataSnapshot.child("nombre").getValue().toString());
                //String icon = dataSnapshot.child("icono").getValue().toString();   Pendiente de como se programa los iconos
                //Seccion para daltonismo
                String daltonism = dataSnapshot.child("daltonismo").getValue().toString();
                if(daltonism.equals("no")){
                    noDaltonic.setChecked(true);
                }else{
                    daltonic.setChecked(true);
                }
                //Seccion tamaño
                String size = dataSnapshot.child("tamanio").getValue().toString();
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
        FirebaseAuth.getInstance().signOut();
        goHome();
    }

    private void goHome() {
        startActivity(new Intent(getApplicationContext(),ProfilesActivity.class));
        finish();
    }

    public void saveChanges(View v){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        TextView userName = findViewById(R.id.editTextTextPersonNameEdit);
        ToggleButton noDaltonic = findViewById(R.id.toggleButtonNoDalto);
        ToggleButton daltonic = findViewById(R.id.toggleButtonDalto);
        ToggleButton bigSize = findViewById(R.id.toggleButtonBig);
        ToggleButton midSize = findViewById(R.id.toggleButtonMid);
        ToggleButton smallSize = findViewById(R.id.toggleButtonSmall);

        FirebaseUser user = db.getCurrentUser();
        String userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];

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
}