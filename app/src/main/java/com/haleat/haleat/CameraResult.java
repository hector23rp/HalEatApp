package com.haleat.haleat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CameraResult extends AppCompatActivity {

    private static String KEY_RESULT = "KEY_RESULT";    //Nos permite coger de la anterior actividad el parámetro que indica si comes bien o no.
    private static String KEY_NAME = "KEY_NAME";    //Nos permite coger de la anterior actividad el parámetro que indica el nombre del alimento.
    private static String KEY_PROTEINAS = "KEY_PROTEINAS";  //Nos permite coger de la anterior actividad el parámetro proteinas.
    private static String KEY_CALORIAS = "KEY_CALORIAS";  //Nos permite coger de la anterior actividad el parámetro calorias.
    private static String KEY_HIDRATOS = "KEY_HIDRATOS";  //Nos permite coger de la anterior actividad el parámetro hidratos.
    private static String KEY_GRASAS = "KEY_GRASAS";  //Nos permite coger de la anterior actividad el parámetro grasas.
    private static String KEY_AZUCAR = "KEY_AZUCAR";  //Clave del parámetro azucar.

    private Button buttonPositivo, buttonNeutral, buttonNegativo, buttonBack;

    private TextView frase, proteinas, calorias, grasas, hidratos, azucar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result);
        //Definimos los componentes de la pantalla.
        frase = (TextView) findViewById(R.id.frase);
        proteinas = (TextView) findViewById(R.id.proteinasResult);
        calorias = (TextView) findViewById(R.id.caloriasResult);
        grasas = (TextView) findViewById(R.id.grasasResult);
        hidratos = (TextView) findViewById(R.id.hidratosResult);
        azucar = (TextView) findViewById(R.id.azucarResult);
        buttonPositivo = (Button) findViewById(R.id.button_positivo);
        buttonNeutral = (Button) findViewById(R.id.button_neutral);
        buttonNegativo = (Button) findViewById(R.id.button_negativo);
        buttonBack = (Button) findViewById(R.id.button_back);
        //Agregamos los parametros al texto.
        setParams();
        //Definimos los listener de cada botón.
        buttonPositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buttonNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buttonNegativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
        boolean check = getIntent().getBooleanExtra("KEY_RESULT",false);
        if(check){
            frase.setText("Alimento: "+getIntent().getStringExtra("KEY_NAME")+". Comida saludable. ¡Bien hecho!");
        }
        else{
            frase.setText("Alimento: "+getIntent().getStringExtra("KEY_NAME")+". Comida no saludable. Eso te irá a las caderas.");
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
