package com.chaicopaillag.app.mageli.frame;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chaicopaillag.app.mageli.R;
import com.chaicopaillag.app.mageli.activity.PerfilActivity;
import com.chaicopaillag.app.mageli.modelo.Persona;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilFragment extends Fragment {

    FloatingActionButton fab_editar_perf;
    FirebaseAuth auth;
    FirebaseUser user;
    private DatabaseReference firebase_bd;
    TextView txtperfil_nombre,
    txtperfil_correo,
    txtperfil_telefono,
    txtperfil_direccion,
    txtperfil_numero_doc,
    txtperfil_nhc,
    txtperfil_fecha_nac,
    txtperfil_genero;

    String correo;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        inicializar_controles();

//        firebase_bd=FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        correo=user.getEmail();

//        firebase_bd.child("Persona").orderByChild(correo);
//        firebase_bd.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Persona persona = dataSnapshot.getValue(Persona.class);
//                llenar_datos(persona);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        fab_editar_perf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ir_perfil();
            }
        });
        txtperfil_correo.setText(correo);
    }

    private void inicializar_controles() {
        fab_editar_perf=(FloatingActionButton) getView().findViewById(R.id.fab_editar_perfil);
        txtperfil_nombre=(TextView)getView().findViewById(R.id.perfil_nombre);
        txtperfil_correo=(TextView)getView().findViewById(R.id.perfil_correo);
        txtperfil_telefono=(TextView)getView().findViewById(R.id.pefil_telefono);
        txtperfil_direccion=(TextView)getView().findViewById(R.id.perfil_direccion);
        txtperfil_numero_doc=(TextView)getView().findViewById(R.id.perfil_numero_doc);
        txtperfil_nhc=(TextView)getView().findViewById(R.id.perfil_nhc);
        txtperfil_fecha_nac=(TextView)getView().findViewById(R.id.perfil_fecha_nac);
        txtperfil_genero=(TextView)getView().findViewById(R.id.perfil_genero);
    }

    private void llenar_datos(Persona persona) {
        txtperfil_nombre.setText(persona.getNombre());
        txtperfil_correo.setText(persona.getCorreo());
        txtperfil_telefono.setText(persona.getTelefono());
        txtperfil_numero_doc.setText(persona.getNumero_documento());
        txtperfil_nhc.setText(persona.getNumero_hc());
        txtperfil_fecha_nac.setText(persona.getFecha_nacimiento().toString());
        txtperfil_direccion.setText(persona.getDireccion());
        if (persona.isGenero()){
            txtperfil_genero.setText("Masculino");
        }else {
            txtperfil_genero.setText("Femenino");
        }


    }

    public PerfilFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }
   private void ir_perfil(){
        Intent intent = new Intent(getContext(), PerfilActivity.class);
        startActivity(intent);
    }

}
