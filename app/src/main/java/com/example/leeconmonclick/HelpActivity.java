package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    TextView message;
    String url = "https://www.youtube.com/watch?v=s-aph61W300";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        spinner = findViewById(R.id.spinner);
        message = findViewById(R.id.textView_message);

        ArrayList<String> options = new ArrayList<>();
        options.add("¿Como puede un niño desbloquear mas imagenes?");
        options.add("¿Como subo contenido propio?");
        options.add("¿Que puedo hacer si pierdo mi usuario?");
        options.add("¿Como funcionan los ajustes de accesibilidad?");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_itemms, options);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }
    public void home(View v){
        Intent helpIntent = new Intent(this, MainActivity.class);//Esta tiene que llevar a la de ayuda
        startActivity(helpIntent);
    }

    public void youtube(View v){
        Intent video = new Intent(Intent.ACTION_VIEW);
        video.setData(Uri.parse(url));
        startActivity(video);
    }

    public void email(View v){
        Intent video = new Intent(Intent.ACTION_SENDTO);
        video.setData(Uri.parse("mailto:theyecoca@gmail.com"));
        startActivity(video);
    }

    public void back(View v){
        finish();
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text_selection =  adapterView.getItemAtPosition(i).toString();

        switch (text_selection) {
            case "¿Como puede un niño desbloquear mas imagenes?":
                message.setText("Simplemente juagando, a medida que suban de nivel lo desbloquearan");
                break;
            case "¿Como subo contenido propio?":
                message.setText("Desde la seccion de contenido los profesionales podran subir el contendio que quieran para luego usarse en sus terapias");
                break;
            case "¿Que puedo hacer si pierdo mi usuario?":
                message.setText("Para los niños tendremos que crear un nuevo usuario, pero para los profesionales desde la pantalla de recuperar contraseña y siguiendo los pasos podremos recuperarla");
                break;
            case "¿Como funcionan los ajustes de accesibilidad?":
                message.setText("Cada usuario podra activarlo en la seccion de ajustes y se aplicara los ajustes para la tritanopia ya que es la mas comun a dia de hoy");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void goBack(View view){
        onBackPressed();
    }
}