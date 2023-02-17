package com.example.leeconmonclick.professional.leeconmonclick.professional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginProfesionalActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch remeberSession;
    private EditText email,pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_professional);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findElements();

    }


    public void login (View v){

        AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.editTextTextPersonName4, Patterns.EMAIL_ADDRESS, R.string.error_mail);
        awesomeValidation.addValidation(this,R.id.editTextTextPassword, ".{6,}", R.string.error_pass);

        if (awesomeValidation.validate()){

            firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(),pass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        if (user.isEmailVerified()){
                            String userCollection = email.getText().toString().trim();
                            String[] parts = userCollection.split("@");
                            userCollection = parts[0];
                            userCollection = userCollection.toLowerCase();
                            goHomeProfesional(userCollection);
                        }else{
                            Toast.makeText(getApplicationContext(), "Verifica el correo", Toast.LENGTH_LONG).show();
                        }


                    }else{
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        dameToastdeerror(errorCode);
                    }
                }
            });
        }
    }

    public void saveStateSession(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", remeberSession.isChecked());
        editor.putString("user","professional");
        editor.apply();
    }

    public void goRememberPass(View v){
        Intent rememberIntent = new Intent(this, RemeberPassActivity.class);
        startActivity(rememberIntent);

    }

    public void help(View v){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

    public void register(View v){
        Intent helpIntent = new Intent(this, RegisterProfessionalActivity.class);
        startActivity(helpIntent);
    }


    private void goHomeProfesional(String name){
        Intent i = new Intent(this, HomeProfesionalActivity.class);
        saveStateSession();
        startActivity(i);
        finish();
    }

    private void findElements(){

        firebaseAuth = FirebaseAuth.getInstance();

        remeberSession = (Switch) findViewById(R.id.switch_remember);
        email = findViewById(R.id.editTextTextPersonName);
        pass = findViewById(R.id.editTextTextPassword);

    }


    private void dameToastdeerror(String error) {
        EditText email = findViewById(R.id.editTextTextPersonName);
        EditText pass = findViewById(R.id.editTextTextPassword);
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