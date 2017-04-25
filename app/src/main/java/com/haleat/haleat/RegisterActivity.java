package com.haleat.haleat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class RegisterActivity extends AppCompatActivity {

    ImageButton buttonMan, buttonFemale;

    Button backButton;

    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Definimos los componentes de la pantalla.
        buttonMan = (ImageButton) findViewById(R.id.buttonMan);
        buttonFemale = (ImageButton) findViewById(R.id.buttonFemale);
        backButton = (Button) findViewById(R.id.btn_back);
        //Definimos los listener de cada botón.
        buttonMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sexSelect = "M";   //Sexo del usuario.
                launchComplexActivity(sexSelect);
            }
        });
        buttonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sexSelect = "F";   //Sexo del usuario.
                launchComplexActivity(sexSelect);
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
     * Lanza la actividad Complex pasando el sexo del usuario.
     * @param sexSelect
     */
    public void launchComplexActivity(String sexSelect){
        Intent intent = new Intent(this, ComplexActivity.class);
        intent.putExtra("sex",sexSelect);
        startActivity(intent);
        finish();
    }

    /**
     * Vuelve a la actividad de Login.
     */
    public void launchLastActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
