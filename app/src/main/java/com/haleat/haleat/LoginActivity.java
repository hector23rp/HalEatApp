package com.haleat.haleat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    Button buttonRegister;  //Botón de Registro.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Definimos los componentes de la pantalla.
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        //Definimos los listener de los botones.
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRegisterActivity();
            }
        });
    }

    /**
     * Inicia la actividad de Registro.
     */
    public void launchRegisterActivity(){
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

}
