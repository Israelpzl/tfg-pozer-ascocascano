package com.example.leeconmonclick.professional.leeconmonclick.professional;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.ErrorActivity;
import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.R;
import com.example.leeconmonclick.patient.CategorySelecctionActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.Vector;

import es.leerconmonclick.util.utils.Content;


public class AddContentActivity extends AppCompatActivity {

    private Spinner spinner;
    private ImageView imageView;
    private ActivityResultLauncher<Intent> someActivityResultCamera;
    private ActivityResultLauncher<String> someActivityResultGalery;
    private StorageReference storageReference;
    private Uri uri;
    private String uriStr;
    private DatabaseReference databaseReference;
    private TextInputEditText word;
    private TextView title,saveButt,cancelButt;
    private Context context;
    private Bundle data;
    private ArrayAdapter<String> adapterSpinner;
    private StorageReference filePath;
    private String userCollection;
    private final String[] DIFFICULTIES = { "FÁCIL", "NORMAL", "DIFÍCIL"};


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Encontrar e inicializar los elementos de base de datos y de la actividad
        findElements();
        //Recuperar los setting del usuario
        getSettings();
        //Abrir la galeria del dispositivo
        getGalery();
        //Abir la cámra del dispositivo
        getCamera();

        //Comprobar si nos encontramos en modo edición de un contenido y si es así, recupera los valores del contenido a modificar
        if (data.getBoolean("modeEdit")){modeEditOn();}

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(AddContentActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });


    }


    public void cameraImageBtn(View v){ someActivityResultCamera.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)); }

    public void galeryImageBtn(View v){
        someActivityResultGalery.launch("image/*");
    }

    private void getGalery(){
        someActivityResultGalery = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        uri = result;
                        imageView.setImageURI(result);
                    }
                });
    }

    private void getCamera(){
        someActivityResultCamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bundle extras = result.getData().getExtras();
                            Bitmap img = (Bitmap) extras.get("data");
                            uri = getImageUri(context, img);
                            imageView.setImageBitmap(img);
                        }
                    }
                });


    }

    //Método que guarda en base de datos el contenido generado, este método se enlaza con el botón en la actividad a través del atributo onClick
    public void saveContent (View v){

        if (!data.getBoolean("modeEdit") || uri !=null){
            if(uri != null){

                if (data.getBoolean("modeEdit")){
                    filePath = storageReference.child("contenidos").child(userCollection).child(data.getString("word"));
                    filePath.delete();
                }

                filePath = storageReference.child("contenidos").child(userCollection).child(Objects.requireNonNull(word.getText()).toString());
                filePath.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw new Exception();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Vector palabras = getPalabras(word.getText().toString());
                            if(Verificar(word.getText().toString().trim().toLowerCase()) && palabras.size() == 1){
                                Uri downloadUri2 = task.getResult();
                                Content content = new Content(word.getText().toString(), downloadUri2.toString(), "", spinner.getSelectedItem().toString(),false);
                                databaseReference.child("content").child(userCollection).child(word.getText().toString()).setValue(content).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "Se ha creado el contenido correctamente", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(), "Tiene que ser una palabra real", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
            }else{ Toast.makeText(getApplicationContext(),"Debe elegir una imagen",Toast.LENGTH_LONG).show(); }


            }else{
                editContent();
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    // Comprueba si la palabra añadida no contiene carácteres raros ni espacios
    private boolean Verificar(String cadena){
        String s;
        char c[],x;
        int i,j,k;
        int error = 0;
        s = " abcdefghijklmnñopqrstuvwxyzáéíóúü";
        c = s.toCharArray();
        for( i=0 ;  i < cadena.length() && error == 0;i++){
            x = cadena.charAt(i);
            k = 0;
            for(j = 0 ;  j < s.length() && k == 0;j++){
                if(x==c[j])
                    k++;
            }
            if( k == 0)
                error++;
        }
        if(error == 0)
            return true;
        else
            return false;
    }

    //Transforma la palabra añadida a vector
    private Vector getPalabras(String cadena) {
        Vector palabras = new Vector();
        String palabra = "";
        cadena = cadena.trim().toLowerCase() + " ";
        char[] c = cadena.toCharArray();
        int i;
        for(i = 0; i < cadena.length(); i++){
            if ( c[i] == ' '){
                palabras.add(palabra);
                palabra = "";
            }
            else
                palabra = palabra + String.valueOf(c[i]);
        }
        return palabras;
    }

    private void modeEditOn() {

        Glide.with(this).load(data.getString("image")).into(imageView);
        word.setText(data.getString("word"));
        uriStr = data.getString("image");
        int selectionPosition= adapterSpinner.getPosition(data.getString("determinant"));
        spinner.setSelection(selectionPosition);
        title.setText("EDITAR CONTENIDO");
    }

    private void editContent(){
        databaseReference.child("content").child(data.getString("word")).removeValue();
        Content content = new Content(Objects.requireNonNull(word.getText()).toString(), uriStr ,"" , spinner.getSelectedItem().toString(),false);
        databaseReference.child("content").child(word.getText().toString()).setValue(content).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"Se ha editado el contenido correctamente",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


    private void getSettings(){


        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.addContentLayoutId);

        databaseReference.child("Users").child(userCollection).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                switch (size) {
                    case "grande":
                        title.setTextSize(30);
                        word.setTextSize(30);
                        saveButt.setTextSize(30);
                        cancelButt.setTextSize(30);
                        break;
                    case "normal":
                        title.setTextSize(20);
                        word.setTextSize(20);
                        saveButt.setTextSize(20);
                        cancelButt.setTextSize(20);
                        break;
                    case "peque":
                        title.setTextSize(10);
                        word.setTextSize(10);
                        saveButt.setTextSize(10);
                        cancelButt.setTextSize(10);
                        break;
                }
                String dalto = snapshot.child("sett").child("1").getValue().toString();
                if(dalto.equals("tritanopia")){
                    constraintLayout.setBackgroundResource(R.color.background_tritano);
                    cancelButt.setBackgroundResource(R.drawable.button_style_red_tritano);
                    saveButt.setBackgroundResource(R.drawable.button_style_tritano);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setContentView(R.layout.activity_error2);
            }
        });

    }

    private void findElements(){

        storageReference = FirebaseStorage.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        context = getApplicationContext();
        data = getIntent().getExtras();

        spinner = (Spinner) findViewById(R.id.spinnerId);
        imageView = (ImageView) findViewById(R.id.imgViewId);
        word = (TextInputEditText) findViewById(R.id.wordInputId);
        title = findViewById(R.id.textView11);
        saveButt = findViewById(R.id.btnSaveId);
        cancelButt = findViewById(R.id.btnBackId);


        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        userCollection = user.getEmail();
        assert userCollection != null;
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();


        adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DIFFICULTIES);
        spinner.setAdapter(adapterSpinner);

    }

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

    public void goBack(View view){
        onBackPressed();
    }


}