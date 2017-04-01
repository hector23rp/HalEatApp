package com.haleat.haleat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class PlaceholderFragment extends Fragment {
    public static final String ARG_SECTION_TITLE = "section_number";

    public static PlaceholderFragment newInstance(String sectionTitle){
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE,sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.section_fragment, container, false);
        String title = getArguments().getString(ARG_SECTION_TITLE);
        TextView titulo = (TextView) view.findViewById(R.id.title);
        titulo.setText(title);
        return view;
    }
}
