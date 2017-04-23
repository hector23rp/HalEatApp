package com.haleat.haleat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DayStadisticFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DayStadisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayStadisticFragment extends Fragment {
    //Claves para coger los parámetros que recibe el fragmento.
    private static final String ARG_PROTEINAS= "proteinas";
    private static final String ARG_KILO = "kilocalorias";
    private static final String ARG_GRASAS = "grasas";
    private static final String ARG_HIDRATOS = "hidratos";
    private static final String ARG_AZUCAR = "azucar";

    private Integer proteinas;  //Proteinas del dia.
    private Integer kilocalorias;   //Kilocalorias del dia.
    private Integer grasas;     //Grasas del dia.
    private Integer hidratos;   //Hidratos del dia.
    private Integer azucar;     //Azucar del día.

    private RectView rectProteinas;
    private RectView rectCalorias;
    private RectView rectGrasas;
    private RectView rectHidratos;
    private RectView rectAzucar;

    private TextView textProteinas;
    private TextView textCalorias;
    private TextView textGrasas;
    private TextView textHidratos;
    private TextView textAzucar;

    private OnFragmentInteractionListener mListener;

    public DayStadisticFragment() {
        // Required empty public constructor
    }

    public static DayStadisticFragment newInstance() {
        DayStadisticFragment fragment = new DayStadisticFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Si se pasan argumentos. Los cogemos.
        if (getArguments() != null) {
            proteinas = getArguments().getInt(ARG_PROTEINAS);
            kilocalorias = getArguments().getInt(ARG_KILO);
            grasas = getArguments().getInt(ARG_GRASAS);
            hidratos = getArguments().getInt(ARG_HIDRATOS);
            azucar = getArguments().getInt(ARG_AZUCAR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day_stadistic, container, false);
        //Declaramos los progressBar de cada compoenente
        rectProteinas = (RectView) view.findViewById(R.id.rect_proteins_day);
        rectCalorias = (RectView) view.findViewById(R.id.rect_calories_day);
        rectGrasas = (RectView) view.findViewById(R.id.rect_grasas_day);
        rectHidratos = (RectView) view.findViewById(R.id.rect_hidratos_day);
        rectAzucar = (RectView) view.findViewById(R.id.rect_azucar_day);
        //Declaramos los textView
        textProteinas = (TextView) view.findViewById(R.id.text_proteinas);
        textCalorias = (TextView) view.findViewById(R.id.text_calorias);
        textGrasas = (TextView) view.findViewById(R.id.text_grasas);
        textHidratos = (TextView) view.findViewById(R.id.text_hidratos);
        textAzucar = (TextView) view.findViewById(R.id.text_azucar);
        //Agregamos el ancho de cada cuadrado a partir de los valores de cada componente.
        setParamsWidth();
        return view;
    }

    /**
     * Configura el ancho del cuadrado de cada componente de la pantalla a partir de sus respectivos valores.
     */
    public void setParamsWidth(){
        ViewGroup.LayoutParams paramsCalorias = rectCalorias.getLayoutParams();
        paramsCalorias.width = kilocalorias/50;
        textCalorias.setText(String.valueOf(kilocalorias));
        rectCalorias.setLayoutParams(paramsCalorias);
        ViewGroup.LayoutParams paramsProteinas = rectProteinas.getLayoutParams();
        paramsProteinas.width = proteinas;
        textProteinas.setText(String.valueOf(proteinas));
        rectProteinas.setLayoutParams(paramsProteinas);
        ViewGroup.LayoutParams paramsGrasas = rectGrasas.getLayoutParams();
        paramsGrasas.width = grasas;
        textGrasas.setText(String.valueOf(grasas));
        rectGrasas.setLayoutParams(paramsGrasas);
        ViewGroup.LayoutParams paramsHidratos = rectHidratos.getLayoutParams();
        paramsHidratos.width = hidratos;
        textHidratos.setText(String.valueOf(hidratos));
        rectHidratos.setLayoutParams(paramsHidratos);
        ViewGroup.LayoutParams paramsAzucar = rectAzucar.getLayoutParams();
        paramsAzucar.width = azucar;
        textAzucar.setText(String.valueOf(azucar));
        rectAzucar.setLayoutParams(paramsAzucar);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
