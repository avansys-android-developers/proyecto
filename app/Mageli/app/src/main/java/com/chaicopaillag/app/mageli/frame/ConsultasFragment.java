package com.chaicopaillag.app.mageli.frame;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaicopaillag.app.mageli.R;
import com.chaicopaillag.app.mageli.activity.CitaActivity;
import com.chaicopaillag.app.mageli.activity.PerfilActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultasFragment extends Fragment {

    FloatingActionButton fab_agregar_citas;
    public ConsultasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consultas, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
