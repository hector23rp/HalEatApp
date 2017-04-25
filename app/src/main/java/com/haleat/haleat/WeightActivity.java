package com.haleat.haleat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class WeightActivity extends AppCompatActivity {

    String sex,complex, year; //Variables que se coge de la anterior actividad.

    Button  buttonNext, backButton;

    ImageButton buttonUp, buttonDown;

    TextView weightText;

    int weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        //Cogemos la variable que nos paso la anterior actividad.
        sex = getIntent().getStringExtra("sex");
        complex = getIntent().getStringExtra("complex");
        year = getIntent().getStringExtra("year");
        //Definimos los componentes.
        buttonDown = (ImageButton) findViewById(R.id.arrowWeightDown);
        buttonUp = (ImageButton) findViewById(R.id.arrowWeightUp);
        buttonNext = (Button) findViewById(R.id.nextWeight);
        backButton = (Button) findViewById(R.id.btn_back);
        weightText = (TextView) findViewById(R.id.weightNumber);
        //Cogemos el peso de la pantalla.
        weight = Integer.parseInt(weightText.getText().toString());
        //Declaramos los listener de cada botón.
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weightDown();
            }
        });
        buttonUp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                weightUp();
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNameActivity();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si se pulsa el botón de atrás se retrocederá a la anterior actividad.
                launchLastActivity();
            }
        });
        setWeight(sex);
    }

    public void setWeight(String sexUser){
        if(sexUser.equals("M")){
            weightText.setText("85");
        }
        if(sexUser.equals("F")){
            weightText.setText("55");
        }
    }

    /**
     * Resta 1 al peso.
     */
    public void weightDown(){
        weight -= 1;
        weightText.setText(String.valueOf(weight));
    }

    /**
     * Suma 1 al peso.
     */
    public void weightUp(){
        weight += 1;
        weightText.setText(String.valueOf(weight));
    }

    /**
     * Lanza la actividad NameActivity.
     */
    public void launchNameActivity(){
        Intent intent = new Intent(this, NameActivity.class);
        intent.putExtra("sex",sex);
        intent.putExtra("complex",complex);
        intent.putExtra("year",year);
        intent.putExtra("weight",String.valueOf(weight));
        startActivity(intent);
        finish();
    }

    /**
     * Vuelve a la actividad de BirthDate.
     */
    public void launchLastActivity(){
        Intent intent = new Intent(this, BirthDateActivity.class);
        intent.putExtra("sex",sex);
        intent.putExtra("complex",complex);
        startActivity(intent);
        finish();
    }
}
