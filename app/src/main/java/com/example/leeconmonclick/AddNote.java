package com.example.leeconmonclick;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import io.realm.Realm;

public class AddNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        EditText titleNote = findViewById(R.id.editTextTextPersonName5);
        EditText descriptionNote = findViewById(R.id.editTextTextPersonName6);
        Button saveNote = findViewById(R.id.savenoteBtn);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleNote.getText().toString();
                String description = descriptionNote.getText().toString();
                long createdTime = System.currentTimeMillis();

                realm.beginTransaction();
                Note note = realm.createObject(Note.class);
                note.setTitle(title);
                note.setDescription(description);
                note.setTime(createdTime);
                realm.commitTransaction();
                Toast.makeText(getApplicationContext(),"Nota guardada",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}