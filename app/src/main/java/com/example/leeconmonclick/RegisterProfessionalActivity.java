package com.example.leeconmonclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.leerconmonclick.util.User;

public class RegisterProfessionalActivity extends AppCompatActivity {

    AwesomeValidation awesomeValidation;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_professional);
    }

    public void help(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

    public void back(View v){
        finish();
    }

    public void createUser(View v){
        EditText email = findViewById(R.id.editTextTextPersonName4);
        EditText pass = findViewById(R.id.editTextTextPassword4);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.editTextTextPersonName4, Patterns.EMAIL_ADDRESS, R.string.error_mail);
        awesomeValidation.addValidation(this,R.id.editTextTextPassword4, ".{6,}", R.string.error_pass);
        awesomeValidation.addValidation(this, R.id.editTextTextPassword5, R.id.editTextTextPassword4, R.string.error_pass_confirmation);


        if(awesomeValidation.validate()){
            FirebaseAuth db = FirebaseAuth.getInstance();

            db.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        databaseReference = FirebaseDatabase.getInstance().getReference();

                        String userCollection = email.getText().toString();
                        String[] parts = userCollection.split("@");
                        userCollection = parts[0];

                        ArrayList<String> settings = new ArrayList<>();
                        settings.add("0");
                        settings.add("normal");
                        settings.add("no");

                        ArrayList<Note> notas = new ArrayList<Note>();
                        Note generateNote = new Note();
                        generateNote.setTitle("Bienvenido");
                        generateNote.setDescription("Aqui podras guardar tus notas personales");
                        long createdTime = System.currentTimeMillis();
                        generateNote.setTime(createdTime);

                        Note aux = new Note();
                        aux.setTitle("Bienvenido");
                        aux.setDescription("Aqui podras guardar tus notas personales");
                        long createdTime2 = System.currentTimeMillis();
                        aux.setTime(createdTime2);

                        notas.add(generateNote);
                        notas.add(aux);

/*                        ArrayList<ArrayList<String>> test = new ArrayList<>();
                        test.add(notas);
                        test.add(notas2);*/


                        //User usuario = new User(email.getText().toString(),userCollection,"0","normal","no",notas);
                        User usuario = new User(email.getText().toString(),userCollection,settings,notas); //Faltaria crear los pacientes

                        databaseReference.child("Users").child(userCollection).setValue(usuario);

                        goHome(userCollection);
                        finish();

                        succesCreation();
                    }else {
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        dameToastdeerror(errorCode);
                    }
                }
            });
        }else{
            necesaryInfo();
        }

    }

    private void goHome(String email){
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("email",email);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


    public void succesCreation(){
        Toast.makeText(getApplicationContext(),"Usuario creado correctamente",Toast.LENGTH_LONG).show();
    }

    public void necesaryInfo(){
        Toast.makeText(getApplicationContext(),"Complete todos los campos",Toast.LENGTH_LONG).show();
    }

    private void dameToastdeerror(String error) {
        EditText email = findViewById(R.id.editTextTextPersonName4);
        EditText pass = findViewById(R.id.editTextTextPassword4);
        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                email.setError("La dirección de correo electrónico está mal formateada.");
                email.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                pass.setError("la contraseña es incorrecta ");
                pass.requestFocus();
                pass.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(this,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                email.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                email.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                pass.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                pass.requestFocus();
                break;

        }

    }
}