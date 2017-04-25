package com.haleat.haleat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ComplexActivity extends AppCompatActivity {

    Button buttonGordo, buttonDeportista, buttonDelgado;

    String sex; //Sexo del usuario que se coge de la anterior actividad.

    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complex);
        //Cogemos la variable que nos paso la anterior actividad.
        sex = getIntent().getStringExtra("sex");
        //Definimos los componentes de la pantalla.
        buttonGordo = (Button) findViewById(R.id.buttonGordo);
        buttonDeportista = (Button) findViewById(R.id.buttonDeportista);
        buttonDelgado = (Button) findViewById(R.id.buttonDelgado);
        backButton = (Button) findViewById(R.id.btn_back);
        //Definimos los listener de los botones.
        buttonGordo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String complexSelect = "engordar";
                launchBirthDateActivity(complexSelect);
            }
        });
        buttonDeportista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String complexSelect = "sano";
                launchBirthDateActivity(complexSelect);
            }
        });
        buttonDelgado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String complexSelect = "adelgazar";
                launchBirthDateActivity(complexSelect);
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
     * Lanza la actividad de fecha de nacimiento pasando el complejo del usuario y su sexo.
     * @param complexSelect
     */
    public void launchBirthDateActivity(String complexSelect){
        Intent intent = new Intent(this, BirthDateActivity.class);
        intent.putExtra("sex",sex);
        intent.putExtra("complex",complexSelect);
        startActivity(intent);
        finish();
    }

    /**
     * Vuelve a la actividad de Register.
     */
    public void launchLastActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
