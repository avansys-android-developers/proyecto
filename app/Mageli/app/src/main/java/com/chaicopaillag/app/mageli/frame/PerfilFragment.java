package com.chaicopaillag.app.mageli.frame;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chaicopaillag.app.mageli.R;
import com.chaicopaillag.app.mageli.activity.MenuActivity;
import com.google.firebase.database.FirebaseDatabase;

public class PerfilFragment extends Fragment {

    AppCompatButton btn;
    public PerfilFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn=(AppCompatButton)getView().findViewById(R.id.btn_perfil);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Usuario registrado",Toast.LENGTH_LONG).show();
            }
        });
    }
}
