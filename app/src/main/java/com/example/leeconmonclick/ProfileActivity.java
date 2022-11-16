package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.view.View;


public class ProfileActivity extends AppCompatActivity {

    private static final String STRING_PREFERENCES = "leeconmonclick.login";
    private static final String PREFERENCES_STATE_BUTTON = "leeconmonclick.login.button";

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        databaseReference = FirebaseDatabase.getInstance().getReference();
    

    }

    public void logOut(View v) {
        saveStateSession();
        FirebaseAuth.getInstance().signOut();
        goHome();
    }

    public void goCalendar (View v){
        Intent calendarIntent = new Intent(this, CalendarActivity.class);
        calendarIntent.putExtra("modeEdit",false);
        startActivity(calendarIntent);
    }

    public void goTaskView (View v){
        Intent taskIntent = new Intent(this, TaskListActivity.class);
        startActivity(taskIntent);
    }

    private void goHome() {
        startActivity(new Intent(getApplicationContext(),ProfilesActivity.class));
        finish();
    }

    public void goAddContent(View v) {
        Intent addContentIntent = new Intent(this, AddContentActivity.class);
        addContentIntent.putExtra("modeEdit",false);
        startActivity(addContentIntent);
    }

    public void goHelp(View v) {
        startActivity(new Intent(getApplicationContext(),HelpActivity.class));

    }

    public void goAddPatient(View v) {
        Intent addPatientIntent = new Intent(this, AddPatientsActivity.class);
        addPatientIntent.putExtra("modeEdit",false);
        startActivity(addPatientIntent);

    }


    public void goListConetnt(View v) {
        startActivity(new Intent(getApplicationContext(), ContentListActivity.class));

    }

    public void goHome(View v) {
        startActivity(new Intent(getApplicationContext(), ListPatientActivity.class));

    }

    public void saveStateSession(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCES_STATE_BUTTON,false).apply();
    }
}