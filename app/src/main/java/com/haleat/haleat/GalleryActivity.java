package com.haleat.haleat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Future;

public class GalleryActivity extends AppCompatActivity {

    private ListView list;

    private Button butonLogOut;

    private ImageButton buttonCamera, buttonStadistic;

    private ArrayAdapter<String> adaptador;

    private ArrayList<String> listItems=new ArrayList<String>();

    private ProgressDialog dialog;

    FutureCallback<Response<String>> callback = new FutureCallback<Response<String>>(){
        @Override
        public void onCompleted(Exception e, Response<String> result) {
            dialog.dismiss();
            try {
                if(result.getHeaders().code() == 200) {
                    if(result.getResult().length() > 2) {   //Si se ha devuelto alguna información de imagen.
                        String[] message = result.getResult().split("\\[");
                        String[] jsonArray = message[1].split("\\]");
                        String[] jsonText = jsonArray[0].split("\\},");
                        for (int i = 0; i < jsonText.length; i++) {
                            JSONObject json = null;
                            if (i == jsonText.length - 1) {
                                json = new JSONObject(jsonText[i]);
                            } else {
                                json = new JSONObject(jsonText[i] + "}");
                            }

                            listItems.add(getMessage(json));
                        }
                        adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listItems) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                // Get the Item from ListView
                                View view = super.getView(position, convertView, parent);

                                // Initialize a TextView for ListView each Item
                                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                                // Set the text color of TextView (ListView Item)
                                tv.setTextColor(Color.BLACK);

                                // Generate ListView Item using TextView
                                return view;
                            }
                        };
                        list.setAdapter(adaptador);
                        //Definimos los listener de la lista.
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                                    long arg3) {
                                String value = adapter.getItemAtPosition(position).toString();
                                launchImageActivity(value);
                            }
                        });
                    }
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        //Definimos los componentes de la pantalla.
        list =  (ListView) findViewById(R.id.list_gallery);
        buttonCamera = (ImageButton) findViewById(R.id.buttonCamera);
        butonLogOut = (Button) findViewById(R.id.logOutButton);
        buttonStadistic = (ImageButton) findViewById(R.id.buttonStadistic);
        //Realizamos la petición al servidor para conseguir los nombres de las imagenes.
        requestToServer();
        //Definimos los listener de los botones.
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCameraActivity();
            }
        });
        butonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginActivity();
            }
        });
        buttonStadistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchStadisticActivity();
            }
        });
    }

    /**
     * Inicia la actividad de la cámara.
     */
    public void launchCameraActivity(){
        startActivity(new Intent(this, CameraActivity.class));
        finish();
    }

    /**
     * Inicia la actividad de Login.
     */
    public void launchLoginActivity(){
        TokenSaver.setRemember(this,0);
        TokenSaver.setToken(this,"");
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /**
     * Inicia la actividad de Estadísticas.
     */
    public void launchStadisticActivity(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * Llama a la función que realiza la petición al servidor.
     */
    public void requestToServer(){
        new PostClass(this).execute();
    }

    /**
     * Coge el nombre de la imagen del objeto json.
     * @param json
     */
    public String getMessage(JSONObject json) throws JSONException {
       String message = json.getString("image");
        return message;
    }

    /**
     * Lanza la actividad ImageActivity.
     */
    public void launchImageActivity(String name){
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra("name",name);
        startActivity(intent);
        finish();
    }

    /**
     * Clase encargada de realizar la comunicación con el servidor.
     */
    public class PostClass extends AsyncTask<String, Void, String> {

        private final Context context;

        public PostClass(Context c) {
            this.context = c;
        }

        protected void onPreExecute() {
            dialog= new ProgressDialog(context);
            dialog.setMessage("Cargando...");
            dialog.show();
        }

        protected String doInBackground(String... params){
            String result = "";
            Future uploading = Ion.with(GalleryActivity.this)
                    .load("http://haleat.com/api/getFoodData")
                    .setHeader("authorization",TokenSaver.getToken(context))
                    .setBodyParameter("","")
                    .asString()
                    .withResponse()
                    .setCallback(callback);
            return result;
        }

        protected void onPostExecute(String result) {
        }

    }
}
