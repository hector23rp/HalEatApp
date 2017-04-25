package com.lpro.haleater;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {

    Button buttonLogin,buttonRegister;  //Botón de Registro.

    EditText email, password;    //Textos que introduce el usuario.

    CheckBox remember;  //Botón para recordar la sesión del usuario.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Definimos los componentes de la pantalla.
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        remember = (CheckBox) findViewById(R.id.checkBox);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        //Definimos los listener de los botones.
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRegisterActivity();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFields()){
                    onLogin();
                }
            }
        });
    }

    /**
     * Coge los campos introducidos por el usuario y llama al método que envía dichos datos al servidor.
     */
    public void onLogin(){
        String userText = email.getText().toString();
        String passwordText = password.getText().toString();
        new RequestClass(this).execute(userText,passwordText);
    }

    /**
     * Comrueba que los campos no se encuentran vacío.
     * @return
     */
    public boolean checkFields(){
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        if(emailText.equals("") || passwordText.equals("")){
            new AlertDialog.Builder(this)
                    .setTitle("Campo Vacío")
                    .setMessage("Se deben rellenar todos los campos")
                    .show();
            return false;
        }
        if(emailText.contains(" ") || passwordText.contains(" ")){
            new AlertDialog.Builder(this)
                    .setTitle("Campo sin espacio")
                    .setMessage("Los campos no pueden contener espacios")
                    .show();
            return false;
        }
        return true;
    }

    /**
     * Clase encargada de realizar la comunicación con el servidor.
     */
    public class RequestClass extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;
        private final Context context;
        public RequestClass(Context c){
            this.context = c;
        }
        protected void onPreExecute(){
            dialog= new ProgressDialog(context);
            dialog.setMessage("Cargando...");
            dialog.show();
        }

        protected String doInBackground(String... params) {
            String result = "";
            URL url = null;
            try {
                url = new URL("http://haleat.com/api/login");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                //Creamos el objeto JSON con los parámetros que le vamos a pasar al servidor.
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", params[0]);
                postDataParams.put("password", params[1]);
                Log.e("params",postDataParams.toString());
                //Realizamos la conexión con el servidor.
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(95000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //Enviamos la información al servidor.
                DataOutputStream dStream = new DataOutputStream(urlConnection.getOutputStream());
                dStream.writeBytes(getPostDataString(postDataParams));
                dStream.flush();
                dStream.close();
                //Comprobamos si se ha recibido algo.
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    setToken(sb.toString());
                    result = "Logueado";
                }
                else {
                    if(responseCode == 403){
                        new AlertDialog.Builder(context)
                                .setTitle("Contraseña Incorrecta")
                                .setMessage("Contraseña introducida incorrecta")
                                .show();
                    }
                    Log.e("Login","HTTP Response Code: " + responseCode);
                    result = "No Logueado "+ responseCode;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            //Si se ha registrado correctamente, hacemos visible el botón de finalizar.
            if(result.equals("Logueado")){
                if(remember.isChecked()){
                    TokenSaver.setRemember(context,1);
                }
                launchCameraActivity();
            }
            dialog.dismiss();
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

    /**
     * Inicia la actividad de Registro.
     */
    public void launchRegisterActivity(){
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    /**
     * Inicia la actividad de la cámara.
     */
    public  void launchCameraActivity(){
        startActivity(new Intent(this, CameraActivity.class));
        finish();
    }
}
