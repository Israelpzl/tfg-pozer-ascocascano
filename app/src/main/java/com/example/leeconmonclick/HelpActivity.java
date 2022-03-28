package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        spinner = findViewById(R.id.spinner);
        message = findViewById(R.id.textView_titleQuestion);

        ArrayList<String> options = new ArrayList<>();
        options.add("Pregunta 1");
        options.add("Pregunta 2");
        options.add("Pregunta 3");
        options.add("Pregunta 4");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, options);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text_selection =  adapterView.getItemAtPosition(i).toString();
        if(text_selection.equals("Pregunta 2")){
            message.setText("Mensaje referencial");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}