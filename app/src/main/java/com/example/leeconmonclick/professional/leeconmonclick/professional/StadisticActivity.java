package com.example.leeconmonclick.professional.leeconmonclick.professional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.ErrorActivity;
import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.R;
import com.example.leeconmonclick.patient.CategorySelecctionActivity;
import com.example.leeconmonclick.professional.leeconmonclick.professional.fragments.FirstFragment;
import com.example.leeconmonclick.professional.leeconmonclick.professional.fragments.FiveFragment;
import com.example.leeconmonclick.professional.leeconmonclick.professional.fragments.FourFragment;
import com.example.leeconmonclick.professional.leeconmonclick.professional.fragments.SecondFragment;
import com.example.leeconmonclick.professional.leeconmonclick.professional.fragments.ThirdFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class StadisticActivity extends AppCompatActivity {

    private Bundle data;

    private final FirstFragment firstFragment = new FirstFragment();
    private final SecondFragment secondFragment = new SecondFragment();
    private final ThirdFragment thirdFragment = new ThirdFragment();
    private final FourFragment fourFragment = new FourFragment();
    private final FiveFragment fiveFragment = new FiveFragment();
    private DatabaseReference databaseReference;
    private String userCollection;
    private FirebaseAuth firebaseAuth;
    private ConstraintLayout constraintLayout;
    private TextView titlePage;

    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadistic);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        findElement();
        getSettings();

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Bundle bd = new Bundle();
        bd.putString("namePatient",data.getString("userPatient"));
        firstFragment.setArguments(bd);
        secondFragment.setArguments(bd);
        thirdFragment.setArguments(bd);
        fourFragment.setArguments(bd);
        fiveFragment.setArguments(bd);

        loadFragment(firstFragment);


        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(StadisticActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });

    }

    private  final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
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
                case R.id.fiveFragment:
                    loadFragment(fiveFragment);
                    return true;
            }
            return false;
        }
    };


    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }

    private void getSettings(){
        databaseReference.child("Users").child(userCollection).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                String size = snapshot.child("sett").child("0").getValue().toString();
                switch (size) {
                    case "grande":
                        titlePage.setTextSize(30);
                        break;
                    case "normal":
                        titlePage.setTextSize(20);
                        break;
                    case "peque":
                        titlePage.setTextSize(10);
                        break;
                }
                String dalto = snapshot.child("sett").child("1").getValue().toString();
                if(dalto.equals("tritanopia")){
                    constraintLayout.setBackgroundResource(R.color.background_tritano);
                }else {
                    constraintLayout.setBackgroundResource(R.color.background);
                }
            }
        });
    }

    public void findElement(){
        firebaseAuth = FirebaseAuth.getInstance();
        data = getIntent().getExtras();
        navigationView = findViewById(R.id.bottom_navigation_stadistic);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();

        constraintLayout = findViewById(R.id.stadistincMain);
        titlePage = findViewById(R.id.tittleContentListId3);
    }


    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }
    public void goBack(View v){
        finish();
    }
}