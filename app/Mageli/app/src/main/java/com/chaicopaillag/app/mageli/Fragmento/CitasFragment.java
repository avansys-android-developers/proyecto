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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.chaicopaillag.app.mageli.Activity.CitaActivity;
import com.chaicopaillag.app.mageli.Activity.ConsultaActivity;
import com.chaicopaillag.app.mageli.Activity.Utiles;
import com.chaicopaillag.app.mageli.Adapter.CitasAdapter;
import com.chaicopaillag.app.mageli.Modelo.Citas;
import com.chaicopaillag.app.mageli.Modelo.Persona;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        firebase_bd=FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        Utiles.REQUEST= Volley.newRequestQueue(getActivity().getApplicationContext());
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
        Query query=firebase_bd.child("Citas").orderByChild("uid_paciente").equalTo(user.getUid()).limitToFirst(100);
        citas_items= new FirebaseRecyclerOptions.Builder<Citas>().setQuery(query,Citas.class).build();
        adapter= new FirebaseRecyclerAdapter<Citas, CitasAdapter.ViewHolder>(citas_items) {
            @Override
            protected void onBindViewHolder(@NonNull CitasAdapter.ViewHolder holder, final int position, @NonNull final Citas model) {
                holder.setAsunto(model.getAsunto());
                holder.setNombre_peditra(model.getNombre_pediatra()+" - "+getString(R.string.pediatra));
                holder.setDescripcion(model.getDescripcion());
                holder.setFecha(model.getFecha());
                holder.setHora(model.getHora());
                Glide.with(getActivity().getApplicationContext()).load(model.getUrl_img_pediatra()).into(holder.img_pediatra);
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
                            cancelar_cita(model);
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
                                try {
                                    adapter.getRef(position).removeValue();
                                } catch (Exception e){
                                    Log.e("Error: ",e.getMessage());
                                }
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

    private void cancelar_cita(final Citas cita) {
        final AlertDialog.Builder alert_cancelar = new AlertDialog.Builder(getContext(),R.style.progrescolor);
        alert_cancelar.setTitle(R.string.app_name);
        alert_cancelar.setMessage(R.string.desea_cancelar_cita);
        alert_cancelar.setPositiveButton(R.string.si,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebase_bd.child("Citas").child(cita.getId()).child("estado").setValue(3);
                CargarTokenPediatra(cita.getUid_pediatra());
                EnviarNotificacionCancelacionCita(cita,"NotificarCitaCancelado");
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
    private  void CargarTokenPediatra(String uid_pediatra){
        firebase_bd.child("Persona").child(uid_pediatra).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              Persona  persona= dataSnapshot.getValue(Persona.class);
                if (persona!=null){
                    Utiles.TOKEN_PEDIATRA =persona.getToken();
                }else {
                    Utiles.TOKEN_PEDIATRA="";
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void EnviarNotificacionCancelacionCita(final Citas citas, final String accion) {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Utiles.MAGELI_URl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            int resultado=Integer.parseInt(jsonObject.getString("success"));
                            if (resultado>0){
                                Toast.makeText(getActivity().getApplicationContext(),getString(R.string.notificacion_cita_cancelacion), Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.notificacion_cita_no_enviada), Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException jex){
                            Log.e("Error: ",jex.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_enviar_notificacion), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String ,String> getParams(){
                Map<String,String>parametros_citas=new HashMap<>();
                parametros_citas.put("accion",accion);
                parametros_citas.put("asunto",citas.getAsunto());
                parametros_citas.put("paciente",citas.getNombre_paciente());
                parametros_citas.put("descripcion",citas.getDescripcion());
                parametros_citas.put("fecha",citas.getFecha());
                parametros_citas.put("hora",citas.getHora());
                parametros_citas.put("token",Utiles.TOKEN_PEDIATRA);
                return parametros_citas;
            }
        };
        Utiles.REQUEST.add(stringRequest);

    }
}
