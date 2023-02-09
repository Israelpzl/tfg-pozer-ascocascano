package com.example.leeconmonclick.professional.leeconmonclick.professional;

import androidx.activity.OnBackPressedCallback;
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
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.Vector;

import es.leerconmonclick.util.Content;
import es.leerconmonclick.util.Silabas;


public class AddContentActivity extends AppCompatActivity {

    private Spinner spinner;
    private ImageView imageView;
    private ActivityResultLauncher<Intent> someActivityResultCamera;
    private ActivityResultLauncher<String> someActivityResultGalery;
    private StorageReference storageReference;
    private Uri uri;
    private String uriStr;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private TextInputEditText word;
    private TextView title,imageText,saveButt,cancelButt;
    private Context context;
    private Bundle data;
    private ArrayAdapter<String> adapterSpinner;
    private StorageReference filePath;
    private String userCollection;
    private FirebaseUser user;
    private FirebaseAuth db = FirebaseAuth.getInstance();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        spinner = (Spinner) findViewById(R.id.spinnerId);
        imageView = (ImageView) findViewById(R.id.imgViewId);
        word = (TextInputEditText) findViewById(R.id.wordInputId);
        title = findViewById(R.id.textView11);
        saveButt = findViewById(R.id.btnSaveId);
        cancelButt = findViewById(R.id.btnBackId);

        user = db.getCurrentUser();
        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.addContentLayoutId);



        databaseReference.child("Users").child(userCollection).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                if(size.equals("grande")){
                    title.setTextSize(30);
                    word.setTextSize(30);
                    saveButt.setTextSize(30);
                    cancelButt.setTextSize(30);
                }else if(size.equals("normal")){
                    title.setTextSize(20);
                    word.setTextSize(20);
                    saveButt.setTextSize(20);
                    cancelButt.setTextSize(20);
                }else if(size.equals("peque")){
                    title.setTextSize(10);
                    word.setTextSize(10);
                    saveButt.setTextSize(10);
                    cancelButt.setTextSize(10);
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

        String[] opciones = { "FÁCIL", "NORMAL", "DIFÍCIL"};

        adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapterSpinner);
        context = getApplicationContext();

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
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bundle extras = result.getData().getExtras();
                            Bitmap img = (Bitmap) extras.get("data");
                            uri = getImageUri(context, img);
                            imageView.setImageBitmap(img);
                        }
                    }
                });

        data = getIntent().getExtras();
        if (data.getBoolean("modeEdit")){
                modeEditOn();
        }




    }
    public void cameraImageBtn(View v){ someActivityResultCamera.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)); }

    public void galeryImageBtn(View v){
        someActivityResultGalery.launch("image/*");
    }

    public void saveContent (View v){

        if (!data.getBoolean("modeEdit") || uri !=null){
            if(uri != null){

                if (data.getBoolean("modeEdit")){
                    filePath = storageReference.child("contenidos").child(userCollection).child(data.getString("word"));
                    filePath.delete();
                }

                filePath = storageReference.child("contenidos").child(userCollection).child(word.getText().toString());
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
                                String sylablle = Silabear();
                                Content content = new Content(word.getText().toString(), downloadUri2.toString(), sylablle, spinner.getSelectedItem().toString());
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

    public void goHelp(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

    public void goBack(View view){
        onBackPressed();
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
        String sylablle = Silabear();
        Content content = new Content(word.getText().toString(), uriStr ,sylablle , spinner.getSelectedItem().toString());
        databaseReference.child("content").child(word.getText().toString()).setValue(content).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"Se ha editado el contenido correctamente",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

  /*  public  List<String> dividirEnSilabas(String palabra) {
        List<String> silabas = new ArrayList<>();
        for (int i = 0; i < palabra.length(); i++) {
            if (esVocal(palabra.charAt(i))) {
                silabas.add(palabra.substring(i, i+1));
            }
            else {
                int j = i+1;
                while (j < palabra.length() && !esVocal(palabra.charAt(j))) {
                    j++;
                }
                silabas.add(palabra.substring(i, j));
                i = j-1;
            }
        }

        return silabas;
    }

    private static boolean esVocal(char c) {
        return "AEIOUaeiou".indexOf(c) >= 0;
    }*/

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

    private String Silabear() {
        Silabas s = new Silabas();
        s.setString(word.getText().toString());
        return s.silabear();
    }

}