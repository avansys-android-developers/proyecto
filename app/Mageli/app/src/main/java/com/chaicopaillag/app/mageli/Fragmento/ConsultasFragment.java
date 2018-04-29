package com.chaicopaillag.app.mageli.Fragmento;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaicopaillag.app.mageli.Activity.CitaActivity;
import com.chaicopaillag.app.mageli.Adapter.ConsultasAdapter;
import com.chaicopaillag.app.mageli.Modelo.Consulta;
import com.chaicopaillag.app.mageli.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ConsultasFragment extends Fragment {
    private FloatingActionButton fab_agregar_conculta;
    private DatabaseReference firebase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressDialog progress_carga;
    private FirebaseRecyclerOptions item_consulta;
    FirebaseRecyclerAdapter<Consulta,ConsultasAdapter.ViewHolder>adapter;
    public ConsultasFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consultas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializar_controles();
        inicializar_servicio();
    }

    private void inicializar_servicio() {
        firebase= FirebaseDatabase.getInstance().getReference("Consultas");
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        Query query=firebase.limitToFirst(100);
        progres_carga_datos();
         item_consulta=new FirebaseRecyclerOptions.Builder<Consulta>().setQuery(query,Consulta.class).build();
         adapter= new FirebaseRecyclerAdapter<Consulta, ConsultasAdapter.ViewHolder>(item_consulta) {
            @Override
            protected void onBindViewHolder(@NonNull ConsultasAdapter.ViewHolder holder, int position, @NonNull Consulta model) {
                holder.setAsunto(model.getAsunto());
                holder.setDescripcion(model.getDescripcion());
                if (model.isFlag_respuesta()){
                    holder.setRespuesta("1 Respuesta");
                }else {
                    holder.setRespuesta("Sin Respuesta");
                }
            }
            @NonNull
            @Override
            public ConsultasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.consultas_item, parent, false);
                return new ConsultasAdapter.ViewHolder(view);
            }

             @Override
             public void onDataChanged() {
                 super.onDataChanged();
                 progress_carga.dismiss();
             }
         };
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void progres_carga_datos() {
        progress_carga=new ProgressDialog(getContext(),R.style.progrescolor);
        progress_carga.setTitle(R.string.app_name);
        progress_carga.setMessage(getString(R.string.cargando_cosultas));
        progress_carga.setIndeterminate(true);
        progress_carga.setCancelable(false);
        progress_carga.show();
    }
    private void inicializar_controles() {
        fab_agregar_conculta=(FloatingActionButton) getView().findViewById(R.id.fab_agregar_citas);

        fab_agregar_conculta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CitaActivity.class);
                startActivity(intent);
            }
        });
    }

}
