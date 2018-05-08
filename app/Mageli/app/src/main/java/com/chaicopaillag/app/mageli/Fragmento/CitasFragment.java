package com.chaicopaillag.app.mageli.Fragmento;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chaicopaillag.app.mageli.Activity.CitaActivity;
import com.chaicopaillag.app.mageli.Activity.ConsultaActivity;
import com.chaicopaillag.app.mageli.Adapter.CitasAdapter;
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

public class CitasFragment extends Fragment {
    private FloatingActionButton fab_agregar_cita;
    private DatabaseReference firebase_bd;
    private RecyclerView Recyc_citas;
    FirebaseRecyclerAdapter<Citas,CitasAdapter.ViewHolder>adapter;
    FirebaseRecyclerOptions<Citas>citas_items;
    ProgressDialog progress_carga;
    FirebaseAuth auth;
    FirebaseUser user;
    public CitasFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_citas, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inicializar_servicios();
        inicializar_controles();
    }
    private void inicializar_servicios() {
        firebase_bd=FirebaseDatabase.getInstance().getReference("Citas");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
    }
    private void inicializar_controles() {
        Recyc_citas=(RecyclerView)getView().findViewById(R.id.mis_citas);
        fab_agregar_cita=(FloatingActionButton) getView().findViewById(R.id.fab_agregar_citas);
        fab_agregar_cita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CitaActivity.class);
                startActivity(intent);
            }
        });
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Recyc_citas.setLayoutManager(linearLayoutManager);
        progress_cargando_citass();
        Query query=firebase_bd.orderByChild("uid_paciente").equalTo(user.getUid()).limitToFirst(100);
        citas_items= new FirebaseRecyclerOptions.Builder<Citas>().setQuery(query,Citas.class).build();
        adapter= new FirebaseRecyclerAdapter<Citas, CitasAdapter.ViewHolder>(citas_items) {
            @Override
            protected void onBindViewHolder(@NonNull CitasAdapter.ViewHolder holder, final int position, @NonNull final Citas model) {
                holder.setAsunto(model.getAsunto());
                holder.setNombre_peditra(model.getNombre_pediatra()+" - "+getString(R.string.pediatra));
                holder.setDescripcion(model.getDescripcion());
                holder.setFecha(model.getFecha());
                holder.setHora(model.getHora());
                if(model.getEstado()==1){
                    holder.setEstado(getString(R.string.pendiente));
                }else if(model.getEstado()==2) {
                    holder.setEstado(getString(R.string.postergado));
                }else if(model.getEstado()==3){
                    holder.setEstado(getString(R.string.cancelado));
                }else if(model.getEstado()==4){
                    holder.setEstado(getString(R.string.no_atendido));
                }
                else {
                    holder.setEstado(getString(R.string.atendido));
                }
                holder.btn_posponer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if (model.getEstado()==5){
                           Toast.makeText(getContext(),getString(R.string.no_editar_cita_atendido), Toast.LENGTH_LONG).show();
                       }else if (model.getEstado()==3){
                           Toast.makeText(getContext(),getString(R.string.no_editar_cita_cancelado_editar), Toast.LENGTH_LONG).show();
                       }
                       else if (model.getEstado()==4){
                           Toast.makeText(getContext(),getString(R.string.no_editar_cita_no_atendido), Toast.LENGTH_LONG).show();
                       }
                       else {
                           Intent intent= new Intent(getContext(),CitaActivity.class);
                           intent.putExtra("editar_cita",true);
                           intent.putExtra("uid_cita",model.getId());
                           startActivity(intent);
                       }
                    }
                });
                holder.btn_cancelar_cita.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getEstado()==3){
                            Toast.makeText(getContext(),getString(R.string.no_editar_cita_cancelado), Toast.LENGTH_LONG).show();
                        }else if (model.getEstado()==4){
                            Toast.makeText(getContext(),getString(R.string.no_cancelar_cita_no_atendido), Toast.LENGTH_LONG).show();
                        }
                        else if (model.getEstado()==5){
                            Toast.makeText(getContext(),getString(R.string.no_cancelar_cita_atendido), Toast.LENGTH_LONG).show();
                        }
                        else {
                            cancelar_cita(model.getId());
                        }
                    }
                });
                holder.btn_eliminar_cita.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder alert_cita = new AlertDialog.Builder(getContext(),R.style.progrescolor);
                        alert_cita.setTitle(R.string.app_name);
                        alert_cita.setMessage(R.string.eliminar_cita);
                        alert_cita.setPositiveButton(R.string.si,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.getRef(position).removeValue();
                            }
                        });
                        alert_cita.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alert_cita.show();
                    }
                });
            }
            @NonNull
            @Override
            public CitasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.citas_item, parent, false);
                return new CitasAdapter.ViewHolder(view);
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
        Recyc_citas.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState== Recyc_citas.SCROLL_STATE_IDLE){
                    fab_agregar_cita.show();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0 || dy<0 && fab_agregar_cita.isShown()){
                    fab_agregar_cita.hide();
                }
            }
        });
    }

    private void cancelar_cita(final String uid_cita) {
        final AlertDialog.Builder alert_cancelar = new AlertDialog.Builder(getContext(),R.style.progrescolor);
        alert_cancelar.setTitle(R.string.app_name);
        alert_cancelar.setMessage(R.string.desea_cancelar_cita);
        alert_cancelar.setPositiveButton(R.string.si,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebase_bd.child(uid_cita).child("estado").setValue(3);
            }
        });
        alert_cancelar.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert_cancelar.show();

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
    private void progress_cargando_citass() {
        progress_carga=new ProgressDialog(getContext(),R.style.progrescolor);
        progress_carga.setTitle(R.string.app_name);
        progress_carga.setMessage(getString(R.string.cargando_citas));
        progress_carga.setIndeterminate(true);
        progress_carga.setCancelable(false);
        progress_carga.show();
    }
}
