package com.example.leeconmonclick;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.leerconmonclick.util.Content;


public class AddContentActivity extends AppCompatActivity {

    private Spinner spinner;
    private ImageView imageView;
    private ActivityResultLauncher<Intent> someActivityResultCamera;
    private ActivityResultLauncher<String> someActivityResultGalery;
    private StorageReference storageReference;
    private Uri uri;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private TextInputEditText word;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);


        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        spinner = (Spinner) findViewById(R.id.spinnerId);
        imageView = (ImageView) findViewById(R.id.imgViewId);
        word = (TextInputEditText) findViewById(R.id.wordInputId);

        String [] opciones = {"-","El","La","Los","Las","Un","Una","Unos","Unas"};

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,opciones);
        spinner.setAdapter(adapterSpinner);

        someActivityResultGalery = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        uri = result;
                        imageView.setImageURI(result);
                    }
                });


        someActivityResultCamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Bundle extras = result.getData().getExtras();
                            Bitmap img = (Bitmap) extras.get("data");
                            imageView.setImageBitmap(img);
                        }
                    }
                });
    }


    public void cameraImageBtn(View v){ someActivityResultCamera.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)); }

    public void galeryImageBtn(View v){
        someActivityResultGalery.launch("image/*");
    }

    public void saveContent (View v){

        if (uri != null){
            StorageReference filePath = storageReference.child("contenidos").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"Se ha subido la imagen",Toast.LENGTH_LONG).show();
                }
            });
        }else{ Toast.makeText(getApplicationContext(),"Debe elegir una imagen",Toast.LENGTH_LONG).show(); }

        List<String> sylablle = new ArrayList<>();
        Content content = new Content(word.getText().toString(), uri.toString(),sylablle , spinner.getSelectedItem().toString());

        databaseReference.child("content").child(word.getText().toString()).setValue(content).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"Se ha creado el contenido correctamente",Toast.LENGTH_LONG).show();
                goProfile();
            }
        });



    }

    private void goProfile () {
        Intent taskIntent = new Intent(this, ProfileActivity.class);
        startActivity(taskIntent);
        finish();
    }

}