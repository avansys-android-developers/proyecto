package com.chaicopaillag.app.mageli.Fragmento;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chaicopaillag.app.mageli.Activity.CitaActivity;
import com.chaicopaillag.app.mageli.Activity.ConsultaActivity;
import com.chaicopaillag.app.mageli.Activity.PerfilActivity;
import com.chaicopaillag.app.mageli.Adapter.ConsultasAdapter;
import com.chaicopaillag.app.mageli.Modelo.Consulta;
import com.chaicopaillag.app.mageli.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsultasFragment extends Fragment {
    private RecyclerView recyclerViewconsulta;
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
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewconsulta.setLayoutManager(linearLayoutManager);
        progres_carga_datos();
        Query query=firebase.orderByChild("uid_paciente").equalTo(firebaseUser.getUid()).limitToFirst(100);
         item_consulta=new FirebaseRecyclerOptions.Builder<Consulta>().setQuery(query,Consulta.class).build();
         adapter= new FirebaseRecyclerAdapter<Consulta, ConsultasAdapter.ViewHolder>(item_consulta) {
            @Override
            protected void onBindViewHolder(@NonNull ConsultasAdapter.ViewHolder holder, final int position, @NonNull final Consulta model) {
                holder.setAsunto(model.getAsunto());
                holder.setDescripcion(model.getDescripcion());
                holder.setNombre_pediatra(model.getNombre_pediatra());
                holder.setFecha_consulta(model.getFecha_registro());
                if (model.isFlag_respuesta()){
                    holder.setRespuesta(getString(R.string.si_respuesta));
                }else {
                    holder.setRespuesta(getString(R.string.no_respuesta));
                }
                holder.btn_respuesta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(model.isFlag_respuesta()){
                            cargar_respuestas(model);
                        }
                    }
                });
                holder.btn_editar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.isFlag_respuesta()){
                            Toast.makeText(getContext(),getString(R.string.no_editar_consulta), Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent= new Intent(getContext(),ConsultaActivity.class);
                            intent.putExtra("editar_consulta",true);
                            intent.putExtra("uid_consulta",model.getId());
                            startActivity(intent);
                        }
                    }
                });
                holder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder alert_consulta = new AlertDialog.Builder(getContext(),R.style.progrescolor);
                        alert_consulta.setTitle(R.string.app_name);
                        alert_consulta.setMessage(R.string.eliminar_consulta);
                        alert_consulta.setPositiveButton(R.string.si,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.getRef(position).removeValue();
                            }
                        });
                        alert_consulta.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alert_consulta.show();
                    }
                });
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
         recyclerViewconsulta.setAdapter(adapter);
        recyclerViewconsulta.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState== recyclerViewconsulta.SCROLL_STATE_IDLE){
                    fab_agregar_conculta.show();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0 || dy<0 && fab_agregar_conculta.isShown()){
                    fab_agregar_conculta.hide();
                }
            }
        });
    }

    private void cargar_respuestas(Consulta model) {
    firebase.child(model.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Consulta consult=dataSnapshot.getValue(Consulta.class);
            if (consult!=null){
                Dialog dialogRespuestas= new Dialog(getContext());

                Toast.makeText(getContext(), consult.getAsunto(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
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
        fab_agregar_conculta=(FloatingActionButton) getView().findViewById(R.id.fab_agregar_consultas);
        recyclerViewconsulta=(RecyclerView)getView().findViewById(R.id.recy_consultas);
        fab_agregar_conculta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ConsultaActivity.class);
                startActivity(intent);
            }
        });
    }

}
