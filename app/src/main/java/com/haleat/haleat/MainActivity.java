package com.haleat.haleat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements DayStadisticFragment.OnFragmentInteractionListener, WeekStadisticFragment.OnFragmentInteractionListener{
    /**
     * Instancia del drawer
     */
    private DrawerLayout drawerLayout;

    /**
     * Titulo inicial del drawer
     */
    private String drawerTitle;

    private TextView titleView; //Titulo de la pantalla
    private ImageButton buttonCamera;    //Botón que nos lleva a la actividad de la cámara

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Definimos los componentes de la pantalla
        titleView = (TextView) findViewById(R.id.title);
        buttonCamera = (ImageButton) findViewById(R.id.buttonCamera);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Añadimos el listener al botón de la cámara.
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem("Camera");
            }
        });
        //Iniciamos la pantalla de estadísticas.
        drawerTitle = getResources().getString(R.string.stadistic);
        if (savedInstanceState == null) {
            selectItem(drawerTitle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.menu_navview, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectItem(String title) {
        // Enviar título como arguemento del fragmento
        Bundle args = new Bundle();
        args.putString(PlaceholderFragment.ARG_SECTION_TITLE, title);
        if(title.equals("Stadistic")){
            Fragment fragment = StadisticFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_content,fragment).commit();
            drawerLayout.closeDrawers(); // Cerrar drawer

        }
        else {
            if (title.equals("Camera")) {
                startActivity(new Intent(this, CameraActivity.class));
                finish();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}
}
