package com.haleat.haleat;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class StadisticFragment extends Fragment{

    private Button buttonDay; //Botón que al pulsar aparaece las estadísticas del día.
    private Button buttonWeek;  //Botón que al pulsar aparaece las estadísticas de la semana.
    private Map<String, Integer> mapStadisticDay;   //Contiene los parámetros correspondientes al día.
    private Map<String, Integer> mapStadisticWeek;  //Contiene los parámetros correspondientes a la semana.
    private String selectedStadistic;
    public StadisticFragment() {
        // Required empty public constructor
    }

    private TextView titleView; //Titulo de la pantalla

    // TODO: Rename and change types and number of parameters
    public static StadisticFragment newInstance() {
        StadisticFragment fragment = new StadisticFragment();
        /*
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inicializamos los mapas que contienen los parámetros de las estadísticas.
        mapStadisticDay = new HashMap<String, Integer>();
        mapStadisticWeek = new HashMap<String, Integer>();
        titleView = (TextView) getActivity().findViewById(R.id.title);
        if(savedInstanceState == null){
            selectedStadistic = getResources().getString(R.string.day);
        }
        //Realizamos la petición al servidor.
        requestServer();
        //initMaps();

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stadistic, container, false);
        //Declaramos los botones de día y semana
        buttonDay = (Button) view.findViewById(R.id.button_day);
        buttonWeek = (Button) view.findViewById(R.id.button_week);
        //Añadimos un listener a cada botón.
        buttonDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStadistic(getResources().getString(R.string.day));
            }
        });
        buttonWeek.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                selectStadistic(getResources().getString(R.string.week));
            }
        });
        return view;
    }

    /**
     *  Dependiendo del parámetro title que se pasa, se realiza la transacción al fragmento día o semana.
     * @param title
     */
    public void selectStadistic(String title){
        titleView.setText(title);
        //Si el titulo es día, inicializamos el fragmento de las estadísticas del día.
        if(title.equals("Dia")){
            Fragment fragment = DayStadisticFragment.newInstance();
            Bundle arguments = setArgumentsFragment("Dia");
            fragment.setArguments(arguments);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.stadistic_content,fragment).commit();
        }
        if(title.equals("Semana")){
            Fragment fragment = WeekStadisticFragment.newInstance();
            Bundle arguments = setArgumentsFragment("Semana");
            fragment.setArguments(arguments);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.stadistic_content,fragment).commit();
        }
    }

    /*
    * Petición al servidor para conseguir las estadísticas del día y la semana.
     */
    public void requestServer(){
        new PostClass(getActivity()).execute();
    }

    public void initMaps(JSONObject jobj){
        String proteinas = null;
        String calorias = null;
        String hidratos = null;
        String grasas = null;
        String azucar = null;
        try {
            proteinas = jobj.getString("proteinas");
            calorias = jobj.getString("kcal");
            hidratos = jobj.getString("hidratosC");
            grasas = jobj.getString("grasas");
            calorias = jobj.getString("kcal");
            azucar = jobj.getString("azucar");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Double numberProteinas = Double.parseDouble(proteinas);
        Double numberCalorias = Double.parseDouble(calorias);
        Double numberGrasas = Double.parseDouble(grasas);
        Double numberHidratos = Double.parseDouble(hidratos);
        Double numberAzucar = Double.parseDouble(azucar);
        mapStadisticDay.put("proteinas",numberProteinas.intValue());
        mapStadisticDay.put("kilocalorias",numberCalorias.intValue());
        mapStadisticDay.put("grasas",numberGrasas.intValue());
        mapStadisticDay.put("hidratos",numberHidratos.intValue());
        mapStadisticDay.put("azucar",numberAzucar.intValue());
        mapStadisticWeek.put("proteinas",63);
        mapStadisticWeek.put("kilocalorias",15);
        mapStadisticWeek.put("grasas",76);
        mapStadisticWeek.put("hidratos",26);
        mapStadisticWeek.put("azucar",86);
    }

    /**
     *  Crea los argumentos que se le va a pasar al fragmento dia o semana.
     * @param title
     * @return Bundle
     */
    public Bundle setArgumentsFragment(String title){
        Bundle arguments = new Bundle();
        if(title.equals("Dia")){
            arguments.putInt("proteinas",mapStadisticDay.get("proteinas"));
            arguments.putInt("kilocalorias",mapStadisticDay.get("kilocalorias"));
            arguments.putInt("grasas",mapStadisticDay.get("grasas"));
            arguments.putInt("hidratos",mapStadisticDay.get("hidratos"));
            arguments.putInt("azucar",mapStadisticDay.get("azucar"));
        }
        if(title.equals("Semana")){
            arguments.putInt("proteinas",mapStadisticWeek.get("proteinas"));
            arguments.putInt("kilocalorias",mapStadisticWeek.get("kilocalorias"));
            arguments.putInt("grasas",mapStadisticWeek.get("grasas"));
            arguments.putInt("hidratos",mapStadisticWeek.get("hidratos"));
            arguments.putInt("azucar",mapStadisticWeek.get("azucar"));
        }
        return arguments;
    }

    /**
     * Clase encargada de realizar la comunicación con el servidor.
     */
    public class PostClass extends AsyncTask<String, Void, String> {
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

        protected String doInBackground(String... params) {
            String result = "";
            //Conseguimos la fecha actual con formato YYYY/MM/MM.
            Date cDate = new Date();
            String fDate = new SimpleDateFormat("yyyy/MM/dd").format(cDate);
            Ion.with(context)
                    .load("http://haleat.com/api/getStats")
                    .setHeader("authorization",TokenSaver.getToken(context))
                    .setBodyParameter("date", fDate)
                    .asString()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {
                            try {
                                if(result.getHeaders().code() == 200) {
                                    JSONObject jobj = new JSONObject(result.getResult());
                                    initMaps(jobj);
                                    selectStadistic(selectedStadistic);
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
            return result;
        }

        protected void onPostExecute(String result) {
            dialog.dismiss();
        }
    }
}
