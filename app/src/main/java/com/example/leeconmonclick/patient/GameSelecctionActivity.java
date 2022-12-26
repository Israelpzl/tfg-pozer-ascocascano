package com.example.leeconmonclick.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.leeconmonclick.R;

public class GameSelecctionActivity extends AppCompatActivity {

    private Spinner spinner;
    private ArrayAdapter<String> adapterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selecction);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        spinner = (Spinner) findViewById(R.id.spinnerId);
        String[] opciones = {"PRÁCTICA", "FÁCIL", "NORMAL", "DIFÍCIL"};

        adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapterSpinner);
        // spinner.getSelectedItem().toString()
    }

    public void goBack(View view){
        onBackPressed();
    }

}