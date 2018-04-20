package com.chaicopaillag.app.mageli.Fragmento;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chaicopaillag.app.mageli.Activity.MenuActivity;
import com.chaicopaillag.app.mageli.Activity.PerfilActivity;
import com.chaicopaillag.app.mageli.Modelo.Persona;
import com.chaicopaillag.app.mageli.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PerfilFragment extends Fragment {
    private FloatingActionButton fab_editar_perf;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference firebase_bd;
    private ImageView imgUsuario;
    private TextView txtperfil_nombre,
            txtperfil_correo,
            txtperfil_telefono,
            txtperfil_direccion,
            txtperfil_numero_doc,
            txtperfil_nhc,
            txtperfil_fecha_nac,
            txtperfil_genero;
    private  String uid;
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
        inicializar_servicios();
        inicializar_controles();
    }

    private void inicializar_servicios() {
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        uid=user.getUid();
        firebase_bd= FirebaseDatabase.getInstance().getReference("Persona");
        firebase_bd.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Persona persona= dataSnapshot.getValue(Persona.class);
                if (persona!=null){
                    llenar_datos(persona);
                    fab_editar_perf.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(getContext(), getString(R.string.actualiza_perfil), Toast.LENGTH_LONG).show();
                    ir_perfil();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Persona persona= dataSnapshot.getValue(Persona.class);
                if (persona!=null){
                    llenar_datos(persona);
                }else {
                    ir_perfil();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), getString(R.string.cancelar_leer_datos_perfil), Toast.LENGTH_LONG).show();
            }
        });
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
        imgUsuario=(ImageView)getView().findViewById(R.id.img_perfil);
        fab_editar_perf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PerfilActivity.class);
                intent.putExtra("editar",true);
                startActivity(intent);
            }
        });

    }

    private void llenar_datos(Persona persona) {
        txtperfil_nombre.setText(persona.getNombre()+" "+persona.getApellidos());
        txtperfil_correo.setText(persona.getCorreo());
        txtperfil_telefono.setText(persona.getTelefono());
        txtperfil_numero_doc.setText(persona.getNumero_documento());
        txtperfil_nhc.setText(persona.getNumero_hc());
        txtperfil_fecha_nac.setText(persona.getFecha_nacimiento());
        txtperfil_direccion.setText(persona.getDireccion());
        if (persona.isGenero()){
            txtperfil_genero.setText("Masculino");
        }else {
            txtperfil_genero.setText("Femenino");
        }
        if (user.getPhotoUrl()!=null){
            Glide.with(getContext()).load(user.getPhotoUrl()).into(imgUsuario);
        }
    }
    private void ir_perfil(){
        Intent intent = new Intent(getContext(), PerfilActivity.class);
        startActivity(intent);
    }
}
