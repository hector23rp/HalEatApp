package com.haleat.haleat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class NameActivity extends AppCompatActivity {

    ProgressDialog dialog;

    String sex, complex, year, weight; //Variables que se coge de la anterior actividad.

    EditText user, password, confirmPassword, email;

    Button finish, backButton, buttonRegister;

    TextView resultText, frase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        //Cogemos la variable que nos paso la anterior actividad.
        sex = getIntent().getStringExtra("sex");
        complex = getIntent().getStringExtra("complex");
        year = getIntent().getStringExtra("year");
        weight = getIntent().getStringExtra("weight");
        //Definimos los componentes de la pantalla.
        resultText = (TextView) findViewById(R.id.resultText);
        frase = (TextView) findViewById(R.id.textFrase);
        user = (EditText) findViewById(R.id.editTextUsuarioRegister);
        password = (EditText) findViewById(R.id.editTextPasswordRegister);
        confirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        email = (EditText) findViewById(R.id.editTextEmailRegister);
        finish = (Button) findViewById(R.id.buttonFinish);
        backButton = (Button) findViewById(R.id.btn_back);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        //Dejamos invisible el botón de FINISH.
        finish.setVisibility(View.GONE);
        //Declaramos el listener del botón finish.
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginActivity();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si se pulsa el botón de atrás se retrocederá a la anterior actividad.
                launchLastActivity();
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprobamos que los campos introducidos son correctos, y si es así, se los enviamos al servidor para que nos registre.
                boolean check = checkFields();
                if(check){
                    sendToServer();
                }
            }
        });
    }

    /**
     * Comrueba que los campos no se encuentran vacío y si las contraseñas introducidas coinciden.
     * @return
     */
    public boolean checkFields(){
        String userText = user.getText().toString();
        String passwordText = password.getText().toString();
        String confirmPasswordText = confirmPassword.getText().toString();
        String emailText = email.getText().toString();
        if(userText.equals("") || passwordText.equals("") || confirmPasswordText.equals("") || emailText.equals("")){
            new AlertDialog.Builder(this)
                    .setTitle("Campo Vacío")
                    .setMessage("Se deben rellenar todos los campos")
                    .show();
            return false;
        }
        if(userText.contains(" ") || passwordText.contains(" ") || confirmPasswordText.contains(" ") || emailText.contains(" ")){
            new AlertDialog.Builder(this)
                    .setTitle("Campo sin espacio")
                    .setMessage("Los campos no pueden contener espacios")
                    .show();
            return false;
        }
        if(!passwordText.equals(confirmPasswordText)){
            new AlertDialog.Builder(this)
                    .setTitle("Contraseñas diferentes")
                    .setMessage("Las contraseñas introducidas no coinciden")
                    .show();
            return false;
        }
        return true;
    }

    /**
     * Envía la información del usuario al servidor para registrarse.
     */
    public void sendToServer(){
        new PostClass(this).execute(user.getText().toString(),password.getText().toString(),email.getText().toString(),sex,complex,year,weight);
    }

    /**
     * Clase encargada de realizar la comunicación con el servidor.
     */
    public class PostClass extends AsyncTask<String, Void, String> {

        private final Context context;
        public PostClass(Context c){
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
                url = new URL("http://haleat.com/api/signup");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                //Realizamos la conexión con el servidor.
                Ion.with(context)
                        .load("http://haleat.com/api/signup")
                        .setBodyParameter("name", params[0])
                        .setBodyParameter("email", params[2])
                        .setBodyParameter("password", params[1])
                        .setBodyParameter("peso", params[6])
                        .setBodyParameter("edad", params[5])
                        .setBodyParameter("sexo", params[3])
                        .setBodyParameter("objetivo", params[4])
                        .asString()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<String>>() {
                            @Override
                            public void onCompleted(Exception e, Response<String> result) {
                                try {
                                    if(result.getHeaders().code() == 200) {
                                        JSONObject jobj = new JSONObject(result.getResult());
                                        if(jobj.length() >= 2 ){
                                            TokenSaver.setToken(context,jobj.getString("token"));
                                            JSONObject json = jobj.getJSONObject("user");
                                            JSONObject jsonTemp = json.getJSONObject("diet");
                                            JSONObject jsonDay = jsonTemp.getJSONObject("day");
                                            jsonDay.put("result","Registrado");
                                            setMessage(jsonDay);
                                        }
                                        else{
                                            JSONObject json = new JSONObject();
                                            json.put("result","No Registrado");
                                            json.put("text","El usuario introducido ya esta registrado");
                                        }
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
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

    public void setMessage(JSONObject json) throws JSONException {
        dialog.dismiss();
        if(json.getString("result").equals("Registrado")){
            resultText.setText("¡Registrado!");
            frase.setText("Puedes tomar "+json.getString("kcal")+" calorías al día");
            finish.setVisibility(View.VISIBLE);
            buttonRegister.setVisibility(View.GONE);
        }else{
            resultText.setText(json.getString("result"));
            frase.setText(json.getString("text"));
        }
    }
    /**
     * Inicia la actividad Login.
     */
    public void launchLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Vuelve a la actividad de Weight.
     */
    public void launchLastActivity(){
        Intent intent = new Intent(this, WeightActivity.class);
        intent.putExtra("sex",sex);
        intent.putExtra("complex",complex);
        intent.putExtra("year",year);
        startActivity(intent);
        finish();
    }
}
