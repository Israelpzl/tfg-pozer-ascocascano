package com.example.leeconmonclick.professional.leeconmonclick.professional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;




public class StadisticActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView namePatient;
    private Bundle data;
    private CircleImageView iconPatient;
    private Context context= this;

    private FirstFragment firstFragment = new FirstFragment();
    private SecondFragment secondFragment = new SecondFragment();
    private ThirdFragment thirdFragment = new ThirdFragment();
    private FourFragment fourFragment = new FourFragment();

    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadistic);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        data = getIntent().getExtras();
        findElement();

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getUserPatient();




    }

    private  final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.firstFragment:
                    loadFragment(firstFragment);
                    return true;
                case R.id.secondFragment:
                    loadFragment(secondFragment);
                    return true;
                case R.id.thirdFragment:
                    loadFragment(thirdFragment);
                    return true;
                case R.id.fourFragment:
                    loadFragment(fourFragment);
                    return true;
            }
            return false;
        }
    };

    public void getUserPatient(){

        namePatient.setText(data.getString("userPatient"));

       databaseReference.child("userPatient").child(data.getString("userPatient")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                namePatient.setText(snapshot.child("namePatient").getValue().toString());
                String icon = snapshot.child("icon").getValue().toString();
                databaseReference.child("iconPatient").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Glide.with(context).load(snapshot.child(icon).getValue().toString()).into(iconPatient);
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


    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }

    public void findElement(){
        namePatient = findViewById(R.id.namePatient);
        iconPatient = findViewById(R.id.iconPatientId);
        navigationView = findViewById(R.id.bottom_navigation_stadistic);
    }



    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }
    public void goBack(View v){
        finish();
    }
}