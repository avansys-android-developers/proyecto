package com.chaicopaillag.app.mageli.Fragmento;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chaicopaillag.app.mageli.Adapter.ConsultaPediatraAdapter;
import com.chaicopaillag.app.mageli.Modelo.Consulta;
import com.chaicopaillag.app.mageli.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Objects;

public class ConsultaPediatraFragment extends Fragment {
    private RecyclerView recyclerViewconsulta;
    private DatabaseReference firebase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressDialog progress_carga;
    private FirebaseRecyclerOptions item_consulta;
    FirebaseRecyclerAdapter<Consulta,ConsultaPediatraAdapter.ViewHolder>adapter;
    public ConsultaPediatraFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consulta_pediatra, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inicializar_controles();
        inicializar_servicio();
    }

    private void inicializar_servicio() {
        firebase= FirebaseDatabase.getInstance().getReference("Consultas");
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewconsulta.setLayoutManager(linearLayoutManager);
        progres_carga_datos();
        Query query=firebase.orderByChild("uid_pediatra").equalTo(firebaseUser.getUid()).limitToFirst(100);
        item_consulta=new FirebaseRecyclerOptions.Builder<Consulta>().setQuery(query,Consulta.class).build();
        adapter= new FirebaseRecyclerAdapter<Consulta, ConsultaPediatraAdapter.ViewHolder>(item_consulta) {
            @Override
            protected void onBindViewHolder(@NonNull ConsultaPediatraAdapter.ViewHolder holder, final int position, @NonNull final Consulta model) {
                holder.setAsunto(model.getAsunto());
                holder.setNombre_paciente(model.getNombre_paciente()+" - "+getString(R.string.paciente));
                holder.setDescripcion(model.getDescripcion());
                holder.setFecha_consulta(model.getFecha_registro());
                if (model.isFlag_respuesta()){
                    holder.setRespuesta(getString(R.string.respondido));
                }else {
                    holder.setRespuesta(getString(R.string.no_respondido));
                }
                holder.btn_responder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resonder_consulta(model);
                    }
                });
            }
            @NonNull
            @Override
            public ConsultaPediatraAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.consultas_pediatra_item, parent, false);
                return new ConsultaPediatraAdapter.ViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                progress_carga.dismiss();
            }
        };
        recyclerViewconsulta.setAdapter(adapter);
    }

    private void resonder_consulta(final Consulta model) {
        Button btn_responder;
        TextView asunto;
        final EditText respuesta;
        LayoutInflater inflater=getLayoutInflater();
        View popap= inflater.inflate(R.layout.responder_consulta_pediatra,null);
        btn_responder=(Button)popap.findViewById(R.id.btn_responder_consulta);
        asunto=(TextView) popap.findViewById(R.id.respuesta_asunto_titulo);
        respuesta=(EditText) popap.findViewById(R.id.respuesta_consulta);
        btn_responder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //firebase.child("Respuestas").child(model.getId()).child("descripcion").setValue(respuesta.getText().toString());
                Toast.makeText(getContext(), getString(R.string.respuesta_ok), Toast.LENGTH_SHORT).show();
            }
        });
        asunto.setText(model.getAsunto());
        AlertDialog.Builder popap_respuesta_consulta= new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        popap_respuesta_consulta.setView(popap);
        popap_respuesta_consulta.show();

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
        recyclerViewconsulta=(RecyclerView)getView().findViewById(R.id.mis_consultas_pediatra);
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
    }}
