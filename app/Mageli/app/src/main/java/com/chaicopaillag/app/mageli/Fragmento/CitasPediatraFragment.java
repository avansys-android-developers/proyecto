package com.chaicopaillag.app.mageli.Fragmento;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.chaicopaillag.app.mageli.Activity.CitaActivity;
import com.chaicopaillag.app.mageli.Adapter.CitasAdapter;
import com.chaicopaillag.app.mageli.Adapter.CitasPediatraAdapter;
import com.chaicopaillag.app.mageli.Modelo.Citas;
import com.chaicopaillag.app.mageli.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class CitasPediatraFragment extends Fragment {
    private DatabaseReference firebase_bd;
    private RecyclerView Recyc_citas;
    FirebaseRecyclerAdapter<Citas,CitasPediatraAdapter.ViewHolder> adapter;
    FirebaseRecyclerOptions<Citas> citas_items;
    ProgressDialog progress_carga;
    FirebaseAuth auth;
    FirebaseUser user;
    public CitasPediatraFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_citas_pediatra, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inicializar_servicios();
        inicializar_controles();
    }

    private void inicializar_servicios() {
        firebase_bd= FirebaseDatabase.getInstance().getReference("Citas");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
    }
    private void inicializar_controles() {
        Recyc_citas=(RecyclerView)getView().findViewById(R.id.mis_citas_pediatra);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Recyc_citas.setLayoutManager(linearLayoutManager);
        progress_cargando_citass();
        Query query=firebase_bd.orderByChild("uid_pediatra").equalTo(user.getUid()).limitToFirst(100);
        citas_items= new FirebaseRecyclerOptions.Builder<Citas>().setQuery(query,Citas.class).build();
        adapter= new FirebaseRecyclerAdapter<Citas, CitasPediatraAdapter.ViewHolder>(citas_items) {
            @Override
            protected void onBindViewHolder(@NonNull CitasPediatraAdapter.ViewHolder holder, final int position, @NonNull final Citas model) {
                holder.setAsunto(model.getAsunto());
                holder.setNombre_paciente(model.getNombre_paciente()+" - "+getString(R.string.paciente));
                holder.setCantidad_personas(getString(R.string.para)+" "+model.getCantidad_personas()+" "+getString(R.string.personas));
                holder.setDescripcion(model.getDescripcion());
                holder.setFecha(model.getFecha());
                holder.setHora(model.getHora());
                if(model.isFlag_atendido()){
                    holder.setEstado(getString(R.string.atendido));
                }else if(model.isFlag_cancelado()) {
                    holder.setEstado(getString(R.string.cancelado));
                }else if(model.isFlag_postergado()){
                    holder.setEstado(getString(R.string.postergado));
                }else if(!model.isEstado()){
                    holder.setEstado(getString(R.string.no_atendido));
                }
                else {
                    holder.setEstado(getString(R.string.pendiente));
                }
                holder.btn_no_atendido.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!model.isEstado()){
                            Toast.makeText(getContext(),getString(R.string.marcar_no_atendido_marcado_no_antendido), Toast.LENGTH_SHORT).show();
                        }else if(model.isFlag_atendido()){
                            Toast.makeText(getContext(),getString(R.string.marcar_no_atendido_ya_antendido), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            marcar_como_no_atendido(model.getId());
                        }
                    }
                });
                holder.btn_atendido.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(model.isFlag_atendido()){
                            Toast.makeText(getContext(),getString(R.string.marcar_atendido_ya_marcado_antendido), Toast.LENGTH_SHORT).show();
                        }
                        else if(!model.isEstado()){
                            Toast.makeText(getContext(),getString(R.string.marcar_atendido_marcado_no_antendido), Toast.LENGTH_SHORT).show();
                        }else {
                            marcar_como_atendido(model.getId());
                        }
                    }
                });
            }
            @NonNull
            @Override
            public CitasPediatraAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.citas_pediatra_item, parent, false);
                return new CitasPediatraAdapter.ViewHolder(view);
            }
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                progress_carga.dismiss();
            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                super.onError(error);
            }

        };
        Recyc_citas.setAdapter(adapter);
    }

    private void marcar_como_atendido(final String id) {
        final AlertDialog.Builder alert_marcar_atendido = new AlertDialog.Builder(getContext(),R.style.progrescolor);
        alert_marcar_atendido.setTitle(R.string.app_name);
        alert_marcar_atendido.setMessage(R.string.marcar_como_atendido);
        alert_marcar_atendido.setPositiveButton(R.string.si,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebase_bd.child(id).child("flag_atendido").setValue(true);
            }
        });
        alert_marcar_atendido.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert_marcar_atendido.show();
    }

    private void marcar_como_no_atendido(final String id) {
        final AlertDialog.Builder alert_marcar_no_atendido = new AlertDialog.Builder(getContext(),R.style.progrescolor);
        alert_marcar_no_atendido.setTitle(R.string.app_name);
        alert_marcar_no_atendido.setMessage(R.string.marcar_como_no_atendido);
        alert_marcar_no_atendido.setPositiveButton(R.string.si,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebase_bd.child(id).child("estado").setValue(false);
            }
        });
        alert_marcar_no_atendido.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert_marcar_no_atendido.show();
    }

    private void progress_cargando_citass() {
        progress_carga=new ProgressDialog(getContext(),R.style.progrescolor);
        progress_carga.setTitle(R.string.app_name);
        progress_carga.setMessage(getString(R.string.cargando_citas));
        progress_carga.setIndeterminate(true);
        progress_carga.setCancelable(false);
        progress_carga.show();
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
}
