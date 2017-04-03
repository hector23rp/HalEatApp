package com.haleat.haleat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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

    private Integer proteinas;  //Proteinas de la semana.
    private Integer kilocalorias;   //Kilocalorias de la semana.
    private Integer grasas;     //Grasas de la semana.
    private Integer hidratos;   //Hidratos de la semana.

    private OnFragmentInteractionListener mListener;

    public WeekStadisticFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WeekStadisticFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_week_stadistic, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
