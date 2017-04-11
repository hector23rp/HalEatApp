package com.haleat.haleat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NameActivity extends AppCompatActivity {

    String sex,complex, year, weight; //Variables que se coge de la anterior actividad.

    EditText user, password, confirmPassword;

    Button finish, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        //Cogemos la variable que nos paso la anterior actividad.
        sex = getIntent().getStringExtra("sex");
        complex = getIntent().getStringExtra("complex");
        year = getIntent().getStringExtra("year");
        weight = getIntent().getStringExtra("weight");
        //Definimos los componentes de la pantalla.
        user = (EditText) findViewById(R.id.editTextUsuarioRegister);
        password = (EditText) findViewById(R.id.editTextPasswordRegister);
        confirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        finish = (Button) findViewById(R.id.buttonFinish);
        backButton = (Button) findViewById(R.id.btn_back);
        //Declaramos el listener del botón finish.
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprobamos que los campos introducidos son correctos, y si es así, se los enviamos al servidor para que nos registre.
                boolean check = checkFields();
                if(check){
                    sendToServer();
                    launchLoginActivity();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si se pulsa el botón de atrás se retrocederá a la anterior actividad.
                launchLastActivity();
            }
        });
    }

    /**
     * Comrueba que los campos no se encuentran vacío y si las contraseñas introducidas coinciden.
     * @return
     */
    public boolean checkFields(){
        boolean check = true;
        String userText = user.getText().toString();
        String passwordText = password.getText().toString();
        String confirmPasswordText = confirmPassword.getText().toString();
        if(userText.equals("") || passwordText.equals("") || confirmPasswordText.equals("")){
            new AlertDialog.Builder(this)
                    .setTitle("Campo Vacío")
                    .setMessage("Se deben rellenar todos los campos")
                    .show();
            return false;
        }
        if(userText.contains(" ") || passwordText.contains(" ") || confirmPasswordText.contains(" ")){
            new AlertDialog.Builder(this)
                    .setTitle("Campo sin espacio")
                    .setMessage("Los campos no pueden contener espacios")
                    .show();
            return false;
        }
        if(!passwordText.equals(confirmPasswordText)){
            new AlertDialog.Builder(this)
                    .setTitle("Contraseñas diferentes")
                    .setMessage("Las contraseñas introducidas no coinciden")
                    .show();
            return false;
        }
        return check;
    }

    /**
     * Envía la información del usuario al servidor para registrarse.
     */
    public void sendToServer(){

    }

    /**
     * Inicia la actividad Login.
     */
    public void launchLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Vuelve a la actividad de Weight.
     */
    public void launchLastActivity(){
        Intent intent = new Intent(this, BirthDateActivity.class);
        intent.putExtra("sex",sex);
        intent.putExtra("complex",complex);
        intent.putExtra("year",year);
        startActivity(intent);
        finish();
    }
}
