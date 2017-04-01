package com.haleat.haleat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class StadisticFragment extends Fragment{

    private Button buttonDay; //Botón que al pulsar aparaece las estadísticas del día.
    private Button buttonWeek;  //Botón que al pulsar aparaece las estadísticas de la semana.

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

    public void selectStadistic(String title){
        if(title.equals("Dia")){
            Fragment fragment = DayStadisticFragment.newInstance();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.stadistic_content,fragment).commit();
        }
        if(title.equals("Semana")){
            Fragment fragment = WeekStadisticFragment.newInstance();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.stadistic_content,fragment).commit();
        }
    }

}
