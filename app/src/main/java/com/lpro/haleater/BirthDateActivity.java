package com.lpro.haleater;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class BirthDateActivity extends AppCompatActivity {

    String sex,complex; //Variables que se coge de la anterior actividad.

    Button  buttonNext, backButton;

    ImageButton buttonUp, buttonDown;

    TextView date;

    int year; //Año de nacimiento.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthdate);
        //Cogemos la variable que nos paso la anterior actividad.
        sex = getIntent().getStringExtra("sex");
        complex = getIntent().getStringExtra("complex");
        //Definimos los componentes de la pantalla.
        buttonDown = (ImageButton) findViewById(R.id.arrowDown);
        buttonUp = (ImageButton) findViewById(R.id.arrowUp);
        buttonNext = (Button) findViewById(R.id.next);
        backButton = (Button) findViewById(R.id.btn_back);
        date = (TextView) findViewById(R.id.dateNumber);
        year = Integer.parseInt(date.getText().toString());
        //Declaramos los listener de cada botón.
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearDown();
            }
        });
        buttonUp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                yearUp();
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchWeightActivity();
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
     * Resta 1 al año.
     */
    public void yearDown(){
        if(year > 0) {
            year -= 1;
            date.setText(String.valueOf(year));
        }
    }

    /**
     * Suma 1 al año.
     */
    public void yearUp(){
        year += 1;
        date.setText(String.valueOf(year));
    }

    /**
     * Lanza la actividad WeightActivity.
     */
    public void launchWeightActivity(){
        Intent intent = new Intent(this, WeightActivity.class);
        intent.putExtra("sex",sex);
        intent.putExtra("complex",complex);
        intent.putExtra("year",String.valueOf(year));
        startActivity(intent);
        finish();
    }

    /**
     * Vuelve a la actividad de Complex.
     */
    public void launchLastActivity(){
        Intent intent = new Intent(this, ComplexActivity.class);
        intent.putExtra("sex",sex);
        startActivity(intent);
        finish();
    }
}
