package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.view.View;
import android.widget.TextView;


public class ProfileActivity extends AppCompatActivity {

    Bundle datos;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        datos = getIntent().getExtras();
        String user = datos.getString("email");

        TextView ususario = findViewById(R.id.textView8);

        databaseReference.child("Users").child(user).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ususario.setText(dataSnapshot.child("email").getValue().toString());
            }
        });
    }

    public void logOut(View v) {
        FirebaseAuth.getInstance().signOut();
        goHome();
    }

    private void goHome() {

        startActivity(new Intent(getApplicationContext(),ProfilesActivity.class));
        finish();
    }
}