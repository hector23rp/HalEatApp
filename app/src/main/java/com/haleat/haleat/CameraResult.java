package com.haleat.haleat;

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

    private Button buttonPositivo, buttonNeutral, buttonNegativo;

    private TextView frase, proteinas, calorias, grasas, hidratos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result);
        //Definimos los componentes de la pantalla.
        frase = (TextView) findViewById(R.id.frase);
        proteinas = (TextView) findViewById(R.id.resultProteinas);
        calorias = (TextView) findViewById(R.id.resultCalorias);
        grasas = (TextView) findViewById(R.id.resultGrasas);
        hidratos = (TextView) findViewById(R.id.resultHidratos);
        buttonPositivo = (Button) findViewById(R.id.button_positivo);
        buttonNeutral = (Button) findViewById(R.id.button_neutral);
        buttonNegativo = (Button) findViewById(R.id.button_negativo);
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
        proteinas.setText(getIntent().getStringExtra("KEY_PROTEINAS"));
        calorias.setText(getIntent().getStringExtra("KEY_CALORIAS"));
        grasas.setText(getIntent().getStringExtra("KEY_GRASAS"));
        hidratos.setText(getIntent().getStringExtra("KEY_HIDRATOS"));
    }
}
