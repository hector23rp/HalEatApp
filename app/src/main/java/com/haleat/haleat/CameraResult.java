package com.haleat.haleat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CameraResult extends AppCompatActivity {

    private static String KEY_RESULT = "KEY_RESULT";    //Nos permite coger de la anterior actividad el parámetro que indica si comes bien o no.
    private static String KEY_RESULT_WEEK = "KEY_RESULT_WEEK";    //Clave de el parámetro que indica si comes bien o no en lo que llevas de semana.
    private static String KEY_NAME = "KEY_NAME";    //Nos permite coger de la anterior actividad el parámetro que indica el nombre del alimento.
    private static String KEY_PROTEINAS = "KEY_PROTEINAS";  //Nos permite coger de la anterior actividad el parámetro proteinas.
    private static String KEY_CALORIAS = "KEY_CALORIAS";  //Nos permite coger de la anterior actividad el parámetro calorias.
    private static String KEY_HIDRATOS = "KEY_HIDRATOS";  //Nos permite coger de la anterior actividad el parámetro hidratos.
    private static String KEY_GRASAS = "KEY_GRASAS";  //Nos permite coger de la anterior actividad el parámetro grasas.
    private static String KEY_AZUCAR = "KEY_AZUCAR";  //Clave del parámetro azucar.

    private ImageButton buttonBack;

    private ImageView img;

    private TextView nameEat,frase,fraseSemana,proteinas, calorias, grasas, hidratos, azucar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result);
        //Definimos los componentes de la pantalla.
        nameEat = (TextView) findViewById(R.id.nameEat);
        frase = (TextView) findViewById(R.id.frase);
        fraseSemana = (TextView) findViewById(R.id.fraseSemana);
        proteinas = (TextView) findViewById(R.id.proteinasResult);
        calorias = (TextView) findViewById(R.id.caloriasResult);
        grasas = (TextView) findViewById(R.id.grasasResult);
        hidratos = (TextView) findViewById(R.id.hidratosResult);
        azucar = (TextView) findViewById(R.id.azucarResult);
        buttonBack = (ImageButton) findViewById(R.id.button_back);
        img = (ImageView) findViewById(R.id.imageResult);
        //Agregamos los parametros al texto.
        setParams();
        //Definimos los listener de cada botón.
        buttonBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                launchCameraActivity();
            }
        });
    }

    /**
     * Agrega los parámetros recibidos de la actividad anterior al texto correspondiente.
     */
    public void setParams(){
        boolean checkDay = getIntent().getBooleanExtra(KEY_RESULT,false);
        boolean checkWeek = getIntent().getBooleanExtra(KEY_RESULT_WEEK,false);
        if(checkDay){
            nameEat.setText(getIntent().getStringExtra(KEY_NAME));
            frase.setText("Comida saludable. ¡Bien hecho!");
            img.setImageResource(R.drawable.like);
        }
        else{
            nameEat.setText(getIntent().getStringExtra(KEY_NAME));
            frase.setText("Comida no saludable. Eso te irá a las caderas.");
            img.setImageResource(R.drawable.dislike);
        }
        if(checkWeek){
            fraseSemana.setText("Vas bien con la dieta de la semana");
        }
        else{
            fraseSemana.setText("Vas mal con la dieta de la semana");
        }
        proteinas.setText(getIntent().getStringExtra(KEY_PROTEINAS));
        calorias.setText(getIntent().getStringExtra(KEY_CALORIAS));
        grasas.setText(getIntent().getStringExtra(KEY_GRASAS));
        hidratos.setText(getIntent().getStringExtra(KEY_HIDRATOS));
        azucar.setText(getIntent().getStringExtra(KEY_AZUCAR));
    }

    /**
     * Inicia la actividad de la cámara.
     */
    private void launchCameraActivity(){
        startActivity(new Intent(this, CameraActivity.class));
        finish();
    }
}
