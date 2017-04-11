package com.haleat.haleat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WeightActivity extends AppCompatActivity {

    String sex,complex, year; //Variables que se coge de la anterior actividad.

    Button buttonUp, buttonDown, buttonNext, backButton;

    TextView weightText;

    int weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        //Cogemos la variable que nos paso la anterior actividad.
        sex = getIntent().getStringExtra("sex");
        complex = getIntent().getStringExtra("complex");
        year = getIntent().getStringExtra("year");
        //Definimos los componentes.
        buttonDown = (Button) findViewById(R.id.arrowWeightDown);
        buttonUp = (Button) findViewById(R.id.arrowWeightUp);
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
        if(sexUser.equals("Man")){
            weightText.setText("85");
        }
        if(sexUser.equals("Female")){
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