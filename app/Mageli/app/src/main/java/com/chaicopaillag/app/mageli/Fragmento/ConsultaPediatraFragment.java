package com.chaicopaillag.app.mageli.Fragmento;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.chaicopaillag.app.mageli.Activity.Utiles;
import com.chaicopaillag.app.mageli.Adapter.ConsultaPediatraAdapter;
import com.chaicopaillag.app.mageli.Modelo.Consulta;
import com.chaicopaillag.app.mageli.Modelo.Persona;
import com.chaicopaillag.app.mageli.Modelo.RespuestaConsulta;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConsultaPediatraFragment extends Fragment {
    private RecyclerView recyclerViewconsulta;
    private DatabaseReference firebase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressDialog progress_carga;
    private FirebaseRecyclerOptions item_consulta;
    private FirebaseRecyclerAdapter<Consulta,ConsultaPediatraAdapter.ViewHolder>adapter;
    private LinearLayout layautSinConsultaPed;
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
        firebase= FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        Utiles.REQUEST= Volley.newRequestQueue(getActivity().getApplicationContext());
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewconsulta.setLayoutManager(linearLayoutManager);
        progres_carga_datos();
        Query query=firebase.child("Consultas").orderByChild("uid_pediatra").equalTo(firebaseUser.getUid()).limitToFirst(100);
        item_consulta=new FirebaseRecyclerOptions.Builder<Consulta>().setQuery(query,Consulta.class).build();
        adapter= new FirebaseRecyclerAdapter<Consulta, ConsultaPediatraAdapter.ViewHolder>(item_consulta) {
            @Override
            protected void onBindViewHolder(@NonNull ConsultaPediatraAdapter.ViewHolder holder, final int position, @NonNull final Consulta model) {
                holder.setAsunto(model.getAsunto());
                holder.setNombre_paciente(model.getNombre_paciente()+" - "+getString(R.string.paciente));
                holder.setDescripcion(model.getDescripcion());
                holder.setFecha_consulta(model.getFecha_registro());
                Glide.with(getActivity().getApplicationContext()).load(model.getUrl_img_paciente()).into(holder.img_paciente_consulta);
                if (model.isFlag_respuesta()){
                    holder.setRespuesta(getString(R.string.respondido));
                }else {
                    holder.setRespuesta(getString(R.string.no_respondido));
                }
                holder.btn_responder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(model.isFlag_respuesta()){
                            Toast.makeText(getContext(),getString(R.string.consulta_ya_respondido), Toast.LENGTH_LONG).show();
                        }else{
                            resonder_consulta(model);
                        }
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
                if (adapter.getItemCount()>0){
                    recyclerViewconsulta.setVisibility(View.VISIBLE);
                    layautSinConsultaPed.setVisibility(View.GONE);
                }else {
                    recyclerViewconsulta.setVisibility(View.GONE);
                    layautSinConsultaPed.setVisibility(View.VISIBLE);
                }
                if (progress_carga.isShowing()){
                    progress_carga.dismiss();
                }
            }
        };
        recyclerViewconsulta.setAdapter(adapter);
    }

    private void resonder_consulta(final Consulta model) {
        TextView asunto,text_descripcion;
        final EditText respuesta;
        LayoutInflater inflater=getLayoutInflater();
        View popap= inflater.inflate(R.layout.responder_consulta_pediatra,null);
        asunto=(TextView) popap.findViewById(R.id.respuesta_asunto_titulo);
        respuesta=(EditText) popap.findViewById(R.id.respuesta_consulta);
        text_descripcion=(TextView)popap.findViewById(R.id.text_pregunta);
        asunto.setText(model.getAsunto());
        text_descripcion.setText(model.getDescripcion());
        final AlertDialog.Builder popap_respuesta_consulta= new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.progrescolor);
        popap_respuesta_consulta.setView(popap);
        popap_respuesta_consulta.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String respuest=respuesta.getText().toString();
                Date fecha_reg=new Date();
                DateFormat formatofechahora;
                formatofechahora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String fecha_espuesta=formatofechahora.format(fecha_reg);
                if(respuest.length()<15 || respuest.equals("")){
                    Toast.makeText(getContext(), getString(R.string.respuesta_no_valida), Toast.LENGTH_LONG).show();
                }else{
                    firebase.child("Consultas").child(model.getId()).child("flag_respuesta").setValue(true);
                    RespuestaConsulta respuestaConsulta= new RespuestaConsulta(model.getId(),respuest,fecha_espuesta);
                    firebase.child("RespuestasConsulta").child(model.getId()).setValue(respuestaConsulta);
                    CargarTokenPaciente(model.getUid_paciente());
                    EnviarNotificacionConsultaRespuesta(model,respuestaConsulta,"NotificarConsultaRespuesta");
                    Toast.makeText(getContext(), getString(R.string.respuesta_ok), Toast.LENGTH_LONG).show();
                }
            }
        });
        popap_respuesta_consulta.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
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
    private  void CargarTokenPaciente(String uid_paciente){
        firebase.child("Persona").child(uid_paciente).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Persona persona= dataSnapshot.getValue(Persona.class);
                if (persona!=null){
                    Utiles.TOKEN_PACIENTE =persona.getToken();
                }else {
                    Utiles.TOKEN_PACIENTE="";
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void EnviarNotificacionConsultaRespuesta(final Consulta consulta,final RespuestaConsulta respuesta, final String accion) {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Utiles.MAGELI_URl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            int resultado=Integer.parseInt(jsonObject.getString("success"));
                            if (resultado>0){
                                Toast.makeText(getActivity().getApplicationContext(),getString(R.string.notificacion_consulta_respuesta), Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.notificacion_no_enviada), Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException jex){
                            Log.e("Error: ",jex.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_enviar_notificacion), Toast.LENGTH_SHORT).show();
                        Log.e("Error: ",error.getMessage());
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String ,String> getParams(){
                Map<String,String>parametros_citas=new HashMap<>();
                parametros_citas.put("accion",accion);
                parametros_citas.put("asunto",consulta.getAsunto());
                parametros_citas.put("pediatra",consulta.getNombre_pediatra());
                parametros_citas.put("descripcion",respuesta.getDescripcion());
                parametros_citas.put("fecha",respuesta.getFecha());
                parametros_citas.put("token",Utiles.TOKEN_PACIENTE);
                return parametros_citas;
            }
        };
        Utiles.REQUEST.add(stringRequest);

    }
    private void inicializar_controles() {
        recyclerViewconsulta=(RecyclerView)getView().findViewById(R.id.mis_consultas_pediatra);
        layautSinConsultaPed=(LinearLayout)getView().findViewById(R.id.layautSinConsultaPediatra);
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
