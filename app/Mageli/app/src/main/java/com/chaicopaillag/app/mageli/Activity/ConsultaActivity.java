package com.chaicopaillag.app.mageli.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.chaicopaillag.app.mageli.Modelo.Consulta;
import com.chaicopaillag.app.mageli.Modelo.Persona;
import com.chaicopaillag.app.mageli.R;
import com.google.android.flexbox.FlexboxLayout;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConsultaActivity extends AppCompatActivity {
    private DatabaseReference fire_base;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser User;
    private ArrayList<Persona>lista_pediatras;
    private Persona persona;
    private Consulta consulta;
    private Toolbar toolbar;
    private FlexboxLayout flexbox_pediatra;
    private EditText asunto,descripcion;
    private  TextView nombre_pediatra,uid_pediatra,correo_ped,cel_pediatra;
    private SwitchCompat sw_elegir_pediatra;
    private Button btn_consulta;
    private AlertDialog.Builder PopapPediatras;
    private CircleImageView img_perfil_pediatra;
    private ProgressDialog progress_carga;
    private String UID_P="lpo35Ph7LsN3lJEkNSxaQ60Lx8u1";
    private String NOMBRE_P="";
    private String CORREO_PED="";
    private String CEL_PED="";
    private String URL_IMG_DEFAULT="https://firebasestorage.googleapis.com/v0/b/appmageli.appspot.com/o/perfil.png?alt=media&token=c2c2e8f2-9777-4829-9e23-69a66dcedd06";
    private String URL_IMG_PEDIATRA="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        NOMBRE_P=getString(R.string.anonimo);
        inicializar_servicio();
        inicializar_controles();
    }

    private void inicializar_servicio() {
        fire_base = FirebaseDatabase.getInstance().getReference();
        firebaseAuth= FirebaseAuth.getInstance();
        User=firebaseAuth.getCurrentUser();
        Utiles.REQUEST= Volley.newRequestQueue(this);
    }

    private void inicializar_controles() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_cita);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.regresar_atras));
        getSupportActionBar().setTitle(getString(R.string.consulta));
        asunto=(EditText)findViewById(R.id.asunto_consulta);
        descripcion=(EditText)findViewById(R.id.descipcion_consulta);
        nombre_pediatra=(TextView) findViewById(R.id.nombre_pediatra_consulta);
        uid_pediatra=(TextView) findViewById(R.id.uid_pediatra_consulta);
        correo_ped=(TextView) findViewById(R.id.correo_pediatra_consulta);
        cel_pediatra=(TextView) findViewById(R.id.cel_pediatra_consulta);
        flexbox_pediatra=(FlexboxLayout)findViewById(R.id.fila_pediatra_privada);
        btn_consulta=(Button)findViewById(R.id.btn_consulta);
        sw_elegir_pediatra=(SwitchCompat)findViewById(R.id.sw_consulta_privada);
        img_perfil_pediatra=(CircleImageView)findViewById(R.id.img_perfil_pediatra_consulta);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sw_elegir_pediatra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cargar_pediatras();
                }else {
                    flexbox_pediatra.setVisibility(View.GONE);
                }
            }
        });
        btn_consulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar_campos();
            }
        });
        Intent intento= getIntent();
        if (intento.getBooleanExtra("editar_consulta",true)&&intento.getStringExtra("uid_consulta")!=null){
            String uid_consulta=intento.getStringExtra("uid_consulta");
            fire_base.child("Consultas").child(uid_consulta).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    consulta=dataSnapshot.getValue(Consulta.class);
                    if(consulta!=null){
                        asunto.setText(consulta.getAsunto());
                        descripcion.setText(consulta.getDescripcion());
                        if (!consulta.getNombre_pediatra().equals(getString(R.string.anonimo))){
                            uid_pediatra.setText(consulta.getUid_pediatra());
                            nombre_pediatra.setText(consulta.getNombre_pediatra());
                            cel_pediatra.setText(consulta.getCel_pediatra());
                            correo_ped.setText(consulta.getCorreo_pediatra());
                            flexbox_pediatra.setVisibility(View.VISIBLE);
                            UID_P=consulta.getUid_pediatra();
                            NOMBRE_P=consulta.getNombre_pediatra();
                            CEL_PED=consulta.getCel_pediatra();
                            CORREO_PED=consulta.getCorreo_pediatra();
                            URL_IMG_PEDIATRA=consulta.getUrl_img_padiatra();
                            Glide.with(getApplicationContext()).load(URL_IMG_PEDIATRA).into(img_perfil_pediatra);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    private  void CargarTokenPediatra(String uid_pediatra){
        fire_base.child("Persona").child(uid_pediatra).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                persona= dataSnapshot.getValue(Persona.class);
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
    private void cargar_pediatras() {
        lista_pediatras= new ArrayList<Persona>();
        final List<String> lista = new ArrayList<>();
        PopapPediatras=new AlertDialog.Builder(ConsultaActivity.this);
        PopapPediatras.setTitle(R.string.popap_titulo_pediatras);
        final Query consulta= fire_base.child("Persona").orderByChild("tipo_persona").equalTo(2);
        progress_cargando_pediatras();
        consulta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot person : dataSnapshot.getChildren()){
                        persona=person.getValue(Persona.class);
                        lista_pediatras.add(persona);
                        lista.add(persona.getNombre()+" "+persona.getApellidos()+" - "+persona.getEspecialidad());
                    }
                    PopapPediatras.setAdapter(new ArrayAdapter<>(
                                    ConsultaActivity.this,android.R.layout.simple_list_item_1,lista),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    persona=new Persona();
                                    persona=lista_pediatras.get(item);
                                    flexbox_pediatra.setVisibility(View.VISIBLE);
                                    nombre_pediatra.setText(persona.getNombre()+" "+persona.getApellidos());
                                    uid_pediatra.setText(persona.getId());
                                    correo_ped.setText(persona.getCorreo());
                                    cel_pediatra.setText(persona.getTelefono());
                                    Utiles.TOKEN_PEDIATRA=persona.getToken();
                                    if (persona.getFoto_url()!=null){
                                        Glide.with(getApplicationContext()).load(persona.getFoto_url()).into(img_perfil_pediatra);
                                        URL_IMG_PEDIATRA=persona.getFoto_url();
                                    }else {
                                        Glide.with(getApplicationContext()).load(URL_IMG_DEFAULT).into(img_perfil_pediatra);
                                        URL_IMG_PEDIATRA=URL_IMG_DEFAULT;
                                    }
                                }
                            });
                    PopapPediatras.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            sw_elegir_pediatra.setChecked(false);
                        }
                    });
                    PopapPediatras.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sw_elegir_pediatra.setChecked(false);
                        }
                    });
                    if (progress_carga.isShowing()){
                        progress_carga.dismiss();
                    }
                    PopapPediatras.show();
            }catch (Exception e){
                    Log.e("Error:",e.getMessage());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void progress_cargando_pediatras() {
        progress_carga=new ProgressDialog(ConsultaActivity.this,R.style.progrescolor);
        progress_carga.setTitle(R.string.app_name);
        progress_carga.setMessage(getString(R.string.cargando_pediatras));
        progress_carga.setIndeterminate(true);
        progress_carga.setCancelable(false);
        progress_carga.show();
    }
    private void validar_campos() {
        String _asunto,_descripcion;
        _asunto=asunto.getText().toString();
        _descripcion=descripcion.getText().toString();
        if (TextUtils.isEmpty(_asunto) || _asunto.length()<2){
            asunto.setError(getString(R.string.error_asunto_cita));
            return;
        }else  if (TextUtils.isEmpty(_descripcion)||_descripcion.length()<10 ||_descripcion.length()>1000){
            descripcion.setError(getString(R.string.error_descripcion_cita));
            return;
        }
        else {
            Intent intent= getIntent();
            if (intent.getBooleanExtra("editar_consulta",true)&&intent.getStringExtra("uid_consulta")!=null){
                actualizar_consulta();
            }else {
                guardar_cita();
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void actualizar_consulta() {
        String _uid_consulta,_asunto,_descripcion,_ui_paciente,_nombre_paciente,_url_img_paciente,
                _correo_paciente,_ui_pediatra,_nombre_pediatra,_correo_pediatra,_cel_pediatra,_url_img_pediatra,_fecha_registro;
        boolean flag_respuesta,estado,privacidad;
        Date fecha_hora= new Date();
        Intent intent= getIntent();
        _uid_consulta= intent.getStringExtra("uid_consulta");
        _asunto=asunto.getText().toString();
        _descripcion=descripcion.getText().toString();
        _ui_paciente=User.getUid();
        _nombre_paciente=User.getDisplayName() !=null ? User.getDisplayName() : getString(R.string.anonimo);
        _correo_paciente=User.getEmail();
        _url_img_paciente=User.getPhotoUrl()!=null ? User.getPhotoUrl().toString() :URL_IMG_DEFAULT;
        _ui_pediatra=UID_P;
        _nombre_pediatra = NOMBRE_P.equals("") ? getString(R.string.anonimo) : NOMBRE_P;
        _correo_pediatra=CORREO_PED;
        _cel_pediatra=CEL_PED;
        _url_img_pediatra=URL_IMG_PEDIATRA;
        if (sw_elegir_pediatra.isChecked()){
            _nombre_pediatra=nombre_pediatra.getText().toString();
            _correo_pediatra=correo_ped.getText().toString();
            _cel_pediatra=cel_pediatra.getText().toString();
            _ui_pediatra=uid_pediatra.getText().toString();
            _url_img_pediatra=URL_IMG_PEDIATRA;
        }
        if (sw_elegir_pediatra.isChecked()){
            _nombre_pediatra=nombre_pediatra.getText().toString();
        }
        flag_respuesta=false;
        estado=true;
        privacidad=false;
        if (sw_elegir_pediatra.isChecked()){
            privacidad=true;
        }
        try{
            DateFormat formatofechahora;
            formatofechahora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            _fecha_registro=formatofechahora.format(fecha_hora);
            consulta=new Consulta();
            consulta.setId(_uid_consulta);
            consulta.setAsunto(_asunto);
            consulta.setDescripcion(_descripcion);
            consulta.setUid_paciente(_ui_paciente);
            consulta.setNombre_paciente(_nombre_paciente);
            consulta.setCorreo_paciente(_correo_paciente);
            consulta.setUrl_img_paciente(_url_img_paciente);
            consulta.setUid_pediatra(_ui_pediatra);
            consulta.setNombre_pediatra(_nombre_pediatra);
            consulta.setCorreo_pediatra(_correo_pediatra);
            consulta.setCel_pediatra(_cel_pediatra);
            consulta.setUrl_img_padiatra(_url_img_pediatra);
            consulta.setFecha_registro(_fecha_registro.toString());
            consulta.setFlag_respuesta(flag_respuesta);
            consulta.setEstado(estado);
            consulta.setFlag_privacidad(privacidad);
            Map<String, Object> actualizacion_cita = new HashMap<>();
            actualizacion_cita.put("/id",consulta.getId() );
            actualizacion_cita.put("/asunto",consulta.getAsunto());
            actualizacion_cita.put("/descripcion",consulta.getDescripcion());
            actualizacion_cita.put("/uid_paciente",consulta.getUid_paciente());
            actualizacion_cita.put("/nombre_paciente",consulta.getNombre_paciente());
            actualizacion_cita.put("/correo_paciente",consulta.getCorreo_paciente());
            actualizacion_cita.put("/url_img_paciente",consulta.getUrl_img_paciente());
            actualizacion_cita.put("/uid_pediatra",consulta.getUid_pediatra());
            actualizacion_cita.put("/nombre_pediatra",consulta.getNombre_pediatra());
            actualizacion_cita.put("/correo_pediatra",consulta.getCorreo_pediatra());
            actualizacion_cita.put("/cel_pediatra",consulta.getCel_pediatra());
            actualizacion_cita.put("/url_img_padiatra",consulta.getUrl_img_padiatra());
            actualizacion_cita.put("/fecha_registro",consulta.getFecha_registro());
            actualizacion_cita.put("/flag_respuesta",consulta.isFlag_respuesta());
            actualizacion_cita.put("/flag_privacidad",consulta.isFlag_privacidad());
            actualizacion_cita.put("/estado",consulta.isEstado());
            fire_base.child("Consultas").child(_uid_consulta).updateChildren(actualizacion_cita);
            Toast.makeText(this, R.string.consulta_actualizado_ok, Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void guardar_cita() {
        String _uid_consulta,_asunto,_descripcion,_ui_paciente,_nombre_paciente,_url_img_paciente,
                _correo_paciente,_ui_pediatra,_nombre_pediatra,_correo_pediatra,_cel_pediatra,_url_img_pediatra,_fecha_registro;
        boolean flag_respuesta,estado,privacidad;
        Date fecha_hora= new Date();
        _uid_consulta= UUID.randomUUID().toString();
        _asunto=asunto.getText().toString();
        _descripcion=descripcion.getText().toString();
        _ui_paciente=User.getUid();
        _nombre_paciente=User.getDisplayName() !=null ? User.getDisplayName() : getString(R.string.anonimo);
        _correo_paciente=User.getEmail();
        _url_img_paciente=User.getPhotoUrl()!=null ? User.getPhotoUrl().toString() :URL_IMG_DEFAULT;
        _ui_pediatra=UID_P;
        _nombre_pediatra=getString(R.string.anonimo);
        _correo_pediatra=CORREO_PED;
        _cel_pediatra=CEL_PED;
        _url_img_pediatra=URL_IMG_DEFAULT;
        if (sw_elegir_pediatra.isChecked()){
            _nombre_pediatra=nombre_pediatra.getText().toString();
            _correo_pediatra=correo_ped.getText().toString();
            _cel_pediatra=cel_pediatra.getText().toString();
            _ui_pediatra=uid_pediatra.getText().toString();
            _url_img_pediatra=URL_IMG_PEDIATRA;
        }
        flag_respuesta=false;
        estado=true;
        privacidad=false;
        if (sw_elegir_pediatra.isChecked()){
            privacidad=true;
        }
        try{
            DateFormat formatofechahora;
            formatofechahora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            _fecha_registro=formatofechahora.format(fecha_hora);
            consulta=new Consulta();
            consulta.setId(_uid_consulta);
            consulta.setAsunto(_asunto);
            consulta.setDescripcion(_descripcion);
            consulta.setUid_paciente(_ui_paciente);
            consulta.setNombre_paciente(_nombre_paciente);
            consulta.setCorreo_paciente(_correo_paciente);
            consulta.setUrl_img_paciente(_url_img_paciente);
            consulta.setUid_pediatra(_ui_pediatra);
            consulta.setNombre_pediatra(_nombre_pediatra);
            consulta.setCorreo_pediatra(_correo_pediatra);
            consulta.setCel_pediatra(_cel_pediatra);
            consulta.setUrl_img_padiatra(_url_img_pediatra);
            consulta.setFecha_registro(_fecha_registro.toString());
            consulta.setFlag_respuesta(flag_respuesta);
            consulta.setEstado(estado);
            consulta.setFlag_privacidad(privacidad);
            fire_base.child("Consultas").child(_uid_consulta).setValue(consulta);
            EnviarNotificacionConsulta(consulta,"NotificarNuevaConsulta");
            Toast.makeText(this, R.string.consulta_registrado_ok, Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void EnviarNotificacionConsulta(final Consulta consulta , final String accion) {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Utiles.MAGELI_URl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            int resultado=Integer.parseInt(jsonObject.getString("success"));
                            if (resultado>0){
                                Toast.makeText(ConsultaActivity.this, getString(R.string.notificacion_consulta_enviada), Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(ConsultaActivity.this, getString(R.string.notificacion_consulta_no_enviada), Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException jex){
                            Log.e("Error: ",jex.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ConsultaActivity.this, getString(R.string.error_enviar_notificacion), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String ,String>getParams(){
                Map<String,String>parametros_citas=new HashMap<>();
                parametros_citas.put("accion",accion);
                parametros_citas.put("asunto",consulta.getAsunto());
                parametros_citas.put("paciente",consulta.getNombre_paciente());
                parametros_citas.put("descripcion",consulta.getDescripcion());
                parametros_citas.put("fecha",consulta.getFecha_registro());
                parametros_citas.put("token",Utiles.TOKEN_PEDIATRA);
                return parametros_citas;
            }
        };
        Utiles.REQUEST.add(stringRequest);
    }
}
