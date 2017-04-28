package com.lpro.haleater;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeekStadisticFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeekStadisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekStadisticFragment extends Fragment {
    //Claves para coger los parámetros que recibe el fragmento.
    private static final String ARG_PROTEINAS= "proteinas";
    private static final String ARG_KILO = "kilocalorias";
    private static final String ARG_GRASAS = "grasas";
    private static final String ARG_HIDRATOS = "hidratos";
    private static final String ARG_AZUCAR = "azucar";
    private static final String ARG_PROTEINAS_MAX= "proteinasMax";
    private static final String ARG_KILO_MAX = "kilocaloriasMax";
    private static final String ARG_GRASAS_MAX = "grasasMax";
    private static final String ARG_HIDRATOS_MAX = "hidratosMax";
    private static final String ARG_AZUCAR_MAX = "azucarMax";

    private Integer proteinas;  //Proteinas de la semana.
    private Integer kilocalorias;   //Kilocalorias de la semana.
    private Integer grasas;     //Grasas de la semana.
    private Integer hidratos;   //Hidratos de la semana.
    private Integer azucar;     //Azucar de la semana.
    private Integer proteinasMax;
    private Integer kilocaloriasMax;
    private Integer grasasMax;
    private Integer hidratosMax;
    private Integer azucarMax;
    private Integer proteinasNorm;
    private Integer kilocaloriasNorm;
    private Integer grasasNorm;
    private Integer hidratosNorm;
    private Integer azucarNorm;

    private RelativeLayout layoutProteinas;
    private RelativeLayout layoutCalorias;
    private RelativeLayout layoutGrasas;
    private RelativeLayout layoutHidratos;
    private RelativeLayout layoutAzucar;

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

    public WeekStadisticFragment() {
        // Required empty public constructor
    }

    public static WeekStadisticFragment newInstance() {
        WeekStadisticFragment fragment = new WeekStadisticFragment();
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
            proteinasMax = getArguments().getInt(ARG_PROTEINAS_MAX);
            kilocaloriasMax = getArguments().getInt(ARG_KILO_MAX);
            grasasMax = getArguments().getInt(ARG_GRASAS_MAX);
            hidratosMax = getArguments().getInt(ARG_HIDRATOS_MAX);
            azucarMax = getArguments().getInt(ARG_AZUCAR_MAX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_week_stadistic, container, false);
        //Definimos los RelativeLayout de cada componente.
        layoutProteinas = (RelativeLayout) view.findViewById(R.id.relativelayoutProteinasWeek);
        layoutCalorias = (RelativeLayout) view.findViewById(R.id.relativelayoutCaloriesWeek);
        layoutGrasas = (RelativeLayout) view.findViewById(R.id.relativelayoutGrasasWeek);
        layoutHidratos = (RelativeLayout) view.findViewById(R.id.relativelayoutHidratosWeek);
        layoutAzucar = (RelativeLayout) view.findViewById(R.id.relativelayoutAzucarWeek);
        //Declaramos los progressBar de cada compoenente
        rectProteinas = (RectView) view.findViewById(R.id.rect_proteins_week);
        rectCalorias = (RectView) view.findViewById(R.id.rect_calories_week);
        rectGrasas = (RectView) view.findViewById(R.id.rect_grasas_week);
        rectHidratos = (RectView) view.findViewById(R.id.rect_hidratos_week);
        rectAzucar = (RectView) view.findViewById(R.id.rect_azucar_week);
        //Declaramos los textView
        textProteinas = (TextView) view.findViewById(R.id.text_proteinas_week);
        textCalorias = (TextView) view.findViewById(R.id.text_calorias_week);
        textGrasas = (TextView) view.findViewById(R.id.text_grasas_week);
        textHidratos = (TextView) view.findViewById(R.id.text_hidratos_week);
        textAzucar = (TextView) view.findViewById(R.id.text_azucar_week);
        //Agregamos el ancho de cada cuadrado a partir de los valores de cada componente.
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutProteinas.post(new Runnable() {
                    public void run() {
                        normalizar();
                        setParamsWidth();
                    }
                });
            }
        });
        return view;
    }

    public void normalizar(){
        proteinasNorm = (proteinas * layoutProteinas.getWidth())/proteinasMax;
        kilocaloriasNorm = (kilocalorias * layoutCalorias.getWidth())/kilocaloriasMax;
        grasasNorm = (grasas * layoutGrasas.getWidth())/grasasMax;
        hidratosNorm = (hidratos * layoutHidratos.getWidth())/hidratosMax;
        azucarNorm = (azucar * layoutAzucar.getWidth())/azucarMax;
    }

    /**
     * Configura el ancho del cuadrado de cada componente de la pantalla a partir de sus respectivos valores.
     */
    public void setParamsWidth(){
        ViewGroup.LayoutParams paramsCalorias = rectCalorias.getLayoutParams();
        paramsCalorias.width = kilocaloriasNorm;
        double dk = 1.0 * kilocalorias;
        double dkM = 1.0 * kilocaloriasMax;
        double dkR = (dk/dkM)*100;
        textCalorias.setText(String.format("%.2f", dkR)+"%");
        rectCalorias.setLayoutParams(paramsCalorias);
        ViewGroup.LayoutParams paramsProteinas = rectProteinas.getLayoutParams();
        paramsProteinas.width = proteinasNorm;
        dk = 1.0 * proteinas;
        dkM = 1.0 * proteinasMax;
        dkR = (dk/dkM)*100;
        textProteinas.setText(String.format("%.2f", dkR)+"%");
        rectProteinas.setLayoutParams(paramsProteinas);
        ViewGroup.LayoutParams paramsGrasas = rectGrasas.getLayoutParams();
        paramsGrasas.width = grasasNorm;
        dk = 1.0 * grasas;
        dkM = 1.0 * grasasMax;
        dkR = (dk/dkM)*100;
        textGrasas.setText(String.format("%.2f", dkR)+"%");
        rectGrasas.setLayoutParams(paramsGrasas);
        ViewGroup.LayoutParams paramsHidratos = rectHidratos.getLayoutParams();
        paramsHidratos.width = hidratosNorm;
        dk = 1.0 * hidratos;
        dkM = 1.0 * hidratosMax;
        dkR = (dk/dkM)*100;
        textHidratos.setText(String.format("%.2f", dkR)+"%");
        rectHidratos.setLayoutParams(paramsHidratos);
        ViewGroup.LayoutParams paramsAzucar = rectAzucar.getLayoutParams();
        paramsAzucar.width = azucarNorm;
        dk = 1.0 * azucar;
        dkM = 1.0 * azucarMax;
        dkR = (dk/dkM)*100;
        textAzucar.setText(String.format("%.2f", dkR)+"%");
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
