package com.chaicopaillag.app.mageli.frame;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chaicopaillag.app.mageli.R;
public class PerfilFragment extends Fragment {

    FloatingActionButton fab_editar_perfil;
    Fragment mi_fragmento=null;
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
        fab_editar_perfil=(FloatingActionButton) getView().findViewById(R.id.fab_editar_perfil);
        
        fab_editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mi_fragmento= new EditarPerfilFragment();
//                ponerFragmento(mi_fragmento);
//                getSupportActionBar().setTitle(item.getTitle());
            }
        });
    }
//    private void ponerFragmento(Fragment mi_fragmento) {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.contenedor,mi_fragmento)
//                .commit();
//    }
}
