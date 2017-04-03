package com.haleat.haleat;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;


public class StadisticFragment extends Fragment{

    private Button buttonDay; //Botón que al pulsar aparaece las estadísticas del día.
    private Button buttonWeek;  //Botón que al pulsar aparaece las estadísticas de la semana.
    private Map<String, Integer> mapStadisticDay;   //Contiene los parámetros correspondientes al día.
    private Map<String, Integer> mapStadisticWeek;  //Contiene los parámetros correspondientes a la semana.
    public StadisticFragment() {
        // Required empty public constructor
    }

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
        //Realizamos la petición al servidor.
        //requestServer();
        initMaps();
        if(savedInstanceState == null){
            selectStadistic(getResources().getString(R.string.day));
        }
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
        Activity activity = getActivity();
        activity.setTitle(title);
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

    }

    public void initMaps(){
        mapStadisticDay.put("proteinas",33);
        mapStadisticDay.put("kilocalorias",45);
        mapStadisticDay.put("grasas",66);
        mapStadisticDay.put("hidratos",99);
        mapStadisticWeek.put("proteinas",2333);
        mapStadisticWeek.put("kilocalorias",5345);
        mapStadisticWeek.put("grasas",6666);
        mapStadisticWeek.put("hidratos",7766);
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
        }
        if(title.equals("Semana")){
            arguments.putInt("proteinas",mapStadisticWeek.get("proteinas"));
            arguments.putInt("kilocalorias",mapStadisticWeek.get("kilocalorias"));
            arguments.putInt("grasas",mapStadisticWeek.get("grasas"));
            arguments.putInt("hidratos",mapStadisticWeek.get("hidratos"));
        }
        return arguments;
    }

}
