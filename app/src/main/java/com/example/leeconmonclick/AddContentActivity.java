package com.example.leeconmonclick;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
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

import com.example.leeconmonclick.databinding.ActivityMainBinding;

public class AddContentActivity extends AppCompatActivity {

    private Spinner spinner;
    private ImageView imageView;
    private ActivityResultLauncher<Intent> someActivityResultCamera;
    private ActivityResultLauncher<String> someActivityResultGalery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);


        spinner = (Spinner) findViewById(R.id.spinnerId);
        imageView = (ImageView) findViewById(R.id.imgViewId);

        String [] opciones = {"-","El","La","Los","Las","Un","Una","Unos","Unas"};

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,opciones);
        spinner.setAdapter(adapterSpinner);

        someActivityResultGalery = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
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


    public void cameraImageBtn(View v){
        someActivityResultCamera.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
    }

    public void galeryImageBtn(View v){
        someActivityResultGalery.launch("image/*");
    }



}