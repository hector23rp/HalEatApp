package com.haleat.haleat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CameraResultUserActivity extends AppCompatActivity {

    EditText alimento,proteinas,hidratos,grasas,calorias;

    Button buttonBack;

    String alimentoText,proteinasText,hidratosText,grasasText,caloriasText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result_user);
        //Definimos los componentes de la pantalla.
        alimento = (EditText) findViewById(R.id.eat);
        proteinas = (EditText) findViewById(R.id.proteinas);
        hidratos = (EditText) findViewById(R.id.hidratos);
        grasas = (EditText) findViewById(R.id.grasas);
        calorias = (EditText) findViewById(R.id.calorias);
        buttonBack = (Button) findViewById(R.id.button_back);
        //Definimos el listener del botón.
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFields()){
                    sendParamsToServer();
                    launchCameraActivity();
                }
            }
        });
    }

    /**
     * Comprueba si los campos se encuentran en blanco.
     */
    public boolean checkFields(){
        alimentoText = alimento.getText().toString();
        proteinasText = proteinas.getText().toString();
        hidratosText = hidratos.getText().toString();
        grasasText = grasas.getText().toString();
        caloriasText = calorias.getText().toString();
        if(alimentoText.equals("")){
            new AlertDialog.Builder(this)
                    .setTitle("Campo Vacío")
                    .setMessage("Se deben especificar el alimento")
                    .show();
            return false;
        }
        return true;
    }
    /**
     * Coge los parámetros introducidos por el usuario y se los envía al servidor.
     */
    public void sendParamsToServer(){

    }

    /**
     * Inicia la actividad de la cámara.
     */
    private void launchCameraActivity(){
        startActivity(new Intent(this, CameraActivity.class));
        finish();
    }
}
