package com.haleat.haleat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class ImageActivity extends AppCompatActivity {

    private String nameImage;   //Nombre de la imagen.

    private ImageView image;

    private Button buttonBack;

    private ProgressDialog dialog;

    FutureCallback<Response<String>> callback = new FutureCallback<Response<String>>(){
        @Override
        public void onCompleted(Exception e, Response<String> result) {
            dialog.dismiss();
            if(result.getHeaders().code() == 200) {
                byte[] decodedString = Base64.decode(result.getResult(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image.setImageBitmap(decodedByte);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        //Cogemos la variable que nos paso la anterior actividad.
        nameImage = getIntent().getStringExtra("name");
        //Definimos los componentes de la pantalla.
        buttonBack = (Button) findViewById(R.id.buttonBack);
        image = (ImageView) findViewById(R.id.image);
        requestToServer();
        //Definimos el listener del botón.
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGalleryActivity();
            }
        });
    }

    public void requestToServer(){
        new PostClass(this).execute(nameImage);
    }
    /**
     * Carga la imagen guardada en la pantalla.
     */
    public void setImageFile(){
        Bitmap bmImg = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Haleat/food.jpeg");
        image.setImageBitmap(bmImg);
    }

    /**
     * Inicia la actividad GalleryActivity.
     */
    public void launchGalleryActivity(){
        startActivity(new Intent(this, GalleryActivity.class));
        finish();
    }

    public class PostClass extends AsyncTask<String, Void, Bitmap> {
        ProgressDialog dialog;
        private final Context context;
        public PostClass(Context c){
            this.context = c;
        }
        protected void onPreExecute(){
            dialog= new ProgressDialog(context);
            dialog.setMessage("Cargando...");
            dialog.show();
        }

        protected Bitmap doInBackground(String... params) {
            Bitmap bitm = null;
            URL url = null;
            try {
                url = new URL("http://haleat.com/api/getImage/"+params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                //Realizamos la conexión con el servidor.
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(95000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestProperty("authorization", TokenSaver.getToken(context));
                //Comprobamos si se ha recibido algo.
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
                    bitm = BitmapFactory.decodeStream(urlConnection.getInputStream());
                }
                else {
                    Log.e("Register","HTTP Response Code: " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitm;
        }

        protected void onPostExecute(Bitmap result) {
            dialog.dismiss();
            image.setImageBitmap(result);
        }

        /**
         * Se encarga de coger el valor del token a partir de la respuesta que devuelve el servidor.
         * @param text
         * @return
         */
        public void setToken(String text){
            String[] text1 = text.split("\"");
            TokenSaver.setToken(context,text1[3]);
        }

        public String getPostDataString(JSONObject params) throws Exception {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            Iterator<String> itr = params.keys();
            while(itr.hasNext()){
                String key= itr.next();
                Object value = params.get(key);
                if (first)
                    first = false;
                else
                    result.append("&");
                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            }
            return result.toString();
        }
    }
}
