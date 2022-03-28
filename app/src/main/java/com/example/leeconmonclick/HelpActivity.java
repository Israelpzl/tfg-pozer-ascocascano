package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
    String url = "www.youtube.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        spinner = findViewById(R.id.spinner);
        message = findViewById(R.id.textView_message);

        ArrayList<String> options = new ArrayList<>();
        options.add("Pregunta 1");
        options.add("Pregunta 2");
        options.add("Pregunta 3");
        options.add("Pregunta 4");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_itemms, options);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }
    public void home(View v){
        Intent helpIntent = new Intent(this, MainActivity.class);//Esta tiene que llevar a la de ayuda
        startActivity(helpIntent);
    }

    public void youtube(View v){
        Uri link = Uri.parse(url);
        Intent video = new Intent(Intent.ACTION_VIEW, link);
        startActivity(video);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text_selection =  adapterView.getItemAtPosition(i).toString();

        switch (text_selection) {
            case "Pregunta 1":
                message.setText("Ayuda a mensaje 1");
                break;
            case "Pregunta 2":
                message.setText("Ayuda a mensaje 2");
                break;
            case "Pregunta 3":
                message.setText("Ayuda a mensaje 3");
                break;
            case "Pregunta 4":
                message.setText("Ayuda a mensaje 4");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}