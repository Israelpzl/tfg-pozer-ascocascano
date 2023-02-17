package com.example.leeconmonclick.professional.leeconmonclick.professional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.R;
import com.example.leeconmonclick.professional.leeconmonclick.professional.fragments.FirstFragment;
import com.example.leeconmonclick.professional.leeconmonclick.professional.fragments.FiveFragment;
import com.example.leeconmonclick.professional.leeconmonclick.professional.fragments.FourFragment;
import com.example.leeconmonclick.professional.leeconmonclick.professional.fragments.SecondFragment;
import com.example.leeconmonclick.professional.leeconmonclick.professional.fragments.ThirdFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class StadisticActivity extends AppCompatActivity {

    private Bundle data;

    private final FirstFragment firstFragment = new FirstFragment();
    private final SecondFragment secondFragment = new SecondFragment();
    private final ThirdFragment thirdFragment = new ThirdFragment();
    private final FourFragment fourFragment = new FourFragment();
    private final FiveFragment fiveFragment = new FiveFragment();

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

    }

    public void findElement(){
        data = getIntent().getExtras();
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