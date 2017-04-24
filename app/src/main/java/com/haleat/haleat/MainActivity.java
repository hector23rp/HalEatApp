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
import android.widget.Button;
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
    private ImageButton buttonCamera, buttonGallery;    //Botón que nos lleva a la actividad de la cámara
    private Button buttonLogOut;    //Botón para cerrar sesión.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Definimos los componentes de la pantalla
        titleView = (TextView) findViewById(R.id.title);
        buttonCamera = (ImageButton) findViewById(R.id.buttonCamera);
        buttonGallery = (ImageButton) findViewById(R.id.buttonGallery);
        buttonLogOut = (Button) findViewById(R.id.logOutButton);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Añadimos el listener aa los botones.
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem("Camera");
            }
        });
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem("LogOut");
            }
        });
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem("Gallery");
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
        switch (title){
            case "Stadistic":
                Fragment fragment = StadisticFragment.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_content,fragment).commit();
                drawerLayout.closeDrawers(); // Cerrar drawer
                break;
            case "Camera":
                startActivity(new Intent(this, CameraActivity.class));
                finish();
                break;
            case "LogOut":
                TokenSaver.setRemember(this,0);
                TokenSaver.setToken(this,"");
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case "Gallery":
                startActivity(new Intent(this, GalleryActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}
}
