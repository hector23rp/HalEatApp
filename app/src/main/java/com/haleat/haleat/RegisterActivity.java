package com.haleat.haleat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class RegisterActivity extends AppCompatActivity {

    ImageButton buttonMan;
    ImageButton buttonFemale;

    String layoutSelect;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Definimos los componentes de la pantalla.
        buttonMan = (ImageButton) findViewById(R.id.buttonMan);
        buttonFemale = (ImageButton) findViewById(R.id.buttonFemale);
        buttonMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchComplexActivity();
            }
        });
        buttonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchComplexActivity();
            }
        });
    }

    public void launchComplexActivity(){
        startActivity(new Intent(this, ComplexActivity.class));
        finish();
    }
}
