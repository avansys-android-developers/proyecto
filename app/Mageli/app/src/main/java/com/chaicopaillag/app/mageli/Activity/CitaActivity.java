package com.chaicopaillag.app.mageli.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chaicopaillag.app.mageli.Modelo.Citas;
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

import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class CitaActivity extends AppCompatActivity {
    private FlexboxLayout flexbox_pediatra;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser User;
    private Persona persona;
    private Citas citas;
    private ArrayList<Persona>lista_pediatras;
    private DatabaseReference fire_base;
    private Toolbar toolbar;
    private Calendar calenda;
    private TimePickerDialog reloj;
    private DatePickerDialog calendario;
    private int anio,mes,dia,hora,minuto;
    private EditText asunto,descripcion,fecha,mihora,numero_personas;
    private CircleImageView img_perfil_pediatra;
    private  TextView nombre_pediatra,uid_pediatra,correo_ped,cel_pediatra;
    private SwitchCompat sw_elegir_pediatra;
    private Button btn_cita;
    private AlertDialog.Builder PopapPediatras;
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
        setContentView(R.layout.activity_cita);
        NOMBRE_P=getString(R.string.anonimo);
        inicializar_servicio();
        inicializar_controles();
    }

    private void inicializar_servicio() {
        fire_base=FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        User=firebaseAuth.getCurrentUser();
    }

    private void inicializar_controles() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_cita);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.regresar_atras));
        getSupportActionBar().setTitle(getString(R.string.citas));
        asunto=(EditText)findViewById(R.id.asunto_cita);
        descripcion=(EditText)findViewById(R.id.descipcion_cita);
        fecha=(EditText)findViewById(R.id.fecha_cita);
        mihora=(EditText)findViewById(R.id.hora_cita);
        numero_personas=(EditText)findViewById(R.id.n_personas);
        img_perfil_pediatra=(CircleImageView)findViewById(R.id.img_perfil_pediatra_cita);
        nombre_pediatra=(TextView) findViewById(R.id.nombre_pediatra_cita);
        uid_pediatra=(TextView) findViewById(R.id.uid_pediatra);
        correo_ped=(TextView) findViewById(R.id.correo_pediatra_cita);
        cel_pediatra=(TextView) findViewById(R.id.cel_pediatra_cita);
        flexbox_pediatra=(FlexboxLayout)findViewById(R.id.fila_pediatra);
        btn_cita=(Button)findViewById(R.id.btn_cita);
        sw_elegir_pediatra=(SwitchCompat)findViewById(R.id.sw_elegir_pediatra);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        fecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    cargar_calendario();
                }
            }
        });
        mihora.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    cargar_hora();
                }
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
        btn_cita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar_campos();
            }
        });
        Intent intent= getIntent();
        if (intent.getBooleanExtra("editar_cita",true) &&intent.getStringExtra("uid_cita")!=null){
            String uid_cita=intent.getStringExtra("uid_cita");
            fire_base.child("Citas").child(uid_cita).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                citas= dataSnapshot.getValue(Citas.class);
                if (citas!=null){
                    asunto.setText(citas.getAsunto());
                    descripcion.setText(citas.getDescripcion());
                    fecha.setText(citas.getFecha());
                    mihora.setText(citas.getHora());
                    numero_personas.setText(Integer.toString(citas.getCantidad_personas()));
                    if (!citas.getNombre_pediatra().equals(getString(R.string.anonimo))){
                        nombre_pediatra.setText(citas.getNombre_pediatra());
                        uid_pediatra.setText(citas.getUid_pediatra());
                        cel_pediatra.setText(citas.getCel_pediatra());
                        correo_ped.setText(citas.getCorreo_pediatra());
                        flexbox_pediatra.setVisibility(View.VISIBLE);
                        UID_P=citas.getUid_pediatra();
                        NOMBRE_P=citas.getNombre_pediatra();
                        CORREO_PED=citas.getCorreo_pediatra();
                        CEL_PED=citas.getCel_pediatra();
                        URL_IMG_PEDIATRA=citas.getUrl_img_pediatra();
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
    private void cargar_pediatras() {
        lista_pediatras= new ArrayList<Persona>();
        final List<String> lista = new ArrayList<>();
        PopapPediatras=new AlertDialog.Builder(CitaActivity.this);
        PopapPediatras.setTitle(R.string.popap_titulo_pediatras);
        final Query consulta= fire_base.child("Persona").orderByChild("tipo_persona").equalTo(2);
        progress_cargando_pediatras();
        consulta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot person : dataSnapshot.getChildren()){
                    persona=person.getValue(Persona.class);
                    lista_pediatras.add(persona);
                    lista.add(persona.getNombre()+" "+persona.getApellidos()+" - "+persona.getEspecialidad());
                }
                PopapPediatras.setAdapter(new ArrayAdapter<>(
                        CitaActivity.this,android.R.layout.simple_list_item_1,lista),
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
                progress_carga.dismiss();
                PopapPediatras.show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void progress_cargando_pediatras() {
        progress_carga=new ProgressDialog(CitaActivity.this,R.style.progrescolor);
        progress_carga.setTitle(R.string.app_name);
        progress_carga.setMessage(getString(R.string.cargando_pediatras));
        progress_carga.setIndeterminate(true);
        progress_carga.setCancelable(false);
        progress_carga.show();
    }
    private void validar_campos() {
        String _asunto,_descripcion,_fecha,_hora,_n_personas;
        _asunto=asunto.getText().toString();
        _descripcion=descripcion.getText().toString();
        _fecha=fecha.getText().toString();
        _hora=mihora.getText().toString();
        _n_personas=numero_personas.getText().toString();
        if (TextUtils.isEmpty(_asunto) || _asunto.length()<2||_asunto.length()>100){
            asunto.setError(getString(R.string.error_asunto_cita));
            return;
        }else  if (TextUtils.isEmpty(_descripcion)||_descripcion.length()<10 ||_descripcion.length()>1000){
            descripcion.setError(getString(R.string.error_descripcion_cita));
            return;
        }else  if (TextUtils.isEmpty(_fecha)|| _fecha.length()!=10|| !Utiles.validarFecha(_fecha)){
            fecha.setError(getString(R.string.error_fecha_cita));
            return;
        }else  if (TextUtils.isEmpty(_hora)||_hora.length()!=5 ||!Utiles.validarHora(_hora)){
            mihora.setError(getString(R.string.error_hora_cita));
            return;
        }else  if (TextUtils.isEmpty(_n_personas)||!Utiles.validarNumero(_n_personas)){
            numero_personas.setError(getString(R.string.error_numero_persona_cita));
            return;
        }else if (Integer.parseInt(_n_personas)>3){
            numero_personas.setError(getString(R.string.error_numero_persona_permitida));
            return;
        }
        else {
            Intent intent = getIntent();
            if (intent.getBooleanExtra("editar_cita",true) &&intent.getStringExtra("uid_cita")!=null){
                modificar_cita();
            }else {
                guardar_cita();
            }

        }
    }

    @SuppressLint("SimpleDateFormat")
    private void modificar_cita() {
        String _uid_cita,_asunto,_descripcion,_fecha,_hora,_ui_paciente,_nombre_paciente,
                _correo_paciente,_url_img_paciente,_ui_pediatra,_nombre_pediatra,_correo_pediatra,
                _cel_pediatra,_url_img_pediatra,_fecha_registro;
        int _n_personas,estado;
        Date fecha_reg=new Date();
        Intent inten= getIntent();
        _uid_cita= inten.getStringExtra("uid_cita");
        _asunto=asunto.getText().toString();
        _descripcion=descripcion.getText().toString();
        _fecha=fecha.getText().toString();
        _hora=mihora.getText().toString();
        _n_personas=Integer.parseInt(numero_personas.getText().toString());
        _ui_paciente=User.getUid();
        _nombre_paciente=User.getDisplayName()!=null ? User.getDisplayName() : getString(R.string.anonimo);
        _url_img_paciente=User.getPhotoUrl()!=null ? User.getPhotoUrl().toString() :URL_IMG_DEFAULT;
        _correo_pediatra=CORREO_PED;
        _correo_paciente=User.getEmail();
        _ui_pediatra=UID_P;
        _nombre_pediatra=NOMBRE_P;
        _cel_pediatra=CEL_PED;
        _url_img_pediatra=URL_IMG_PEDIATRA;
        if (sw_elegir_pediatra.isChecked()){
            _nombre_pediatra=nombre_pediatra.getText().toString();
            _cel_pediatra=cel_pediatra.getText().toString();
            _correo_pediatra=correo_ped.getText().toString();
            _ui_pediatra=uid_pediatra.getText().toString();
            _url_img_pediatra=URL_IMG_PEDIATRA;
        }
        estado=2;
        try{
            DateFormat formatofechahora;
            formatofechahora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            _fecha_registro=formatofechahora.format(fecha_reg);
            citas=new Citas();
            citas.setId(_uid_cita);
            citas.setAsunto(_asunto);
            citas.setDescripcion(_descripcion);
            citas.setFecha(_fecha);
            citas.setHora(_hora);
            citas.setCantidad_personas(_n_personas);
            citas.setUid_paciente(_ui_paciente);
            citas.setNombre_paciente(_nombre_paciente);
            citas.setCorreo_paciente(_correo_paciente);
            citas.setUrl_img_paciente(_url_img_paciente);
            citas.setUid_pediatra(_ui_pediatra);
            citas.setNombre_pediatra(_nombre_pediatra);
            citas.setCorreo_pediatra(_correo_pediatra);
            citas.setCel_pediatra(_cel_pediatra);
            citas.setUrl_img_pediatra(_url_img_pediatra);
            citas.setFecha_registro(_fecha_registro);
            citas.setEstado(estado);

            Map<String, Object> actualizacion_cita = new HashMap<>();
            actualizacion_cita.put("/id",citas.getId() );
            actualizacion_cita.put("/asunto",citas.getAsunto());
            actualizacion_cita.put("/descripcion",citas.getDescripcion());
            actualizacion_cita.put("/fecha",citas.getFecha());
            actualizacion_cita.put("/hora",citas.getHora());
            actualizacion_cita.put("/cantidad_personas",citas.getCantidad_personas());
            actualizacion_cita.put("/uid_paciente",citas.getUid_paciente());
            actualizacion_cita.put("/nombre_paciente",citas.getNombre_paciente());
            actualizacion_cita.put("/correo_paciente",citas.getCorreo_paciente());
            actualizacion_cita.put("/url_img_paciente",citas.getUrl_img_paciente());
            actualizacion_cita.put("/uid_pediatra",citas.getUid_pediatra());
            actualizacion_cita.put("/nombre_pediatra",citas.getNombre_pediatra());
            actualizacion_cita.put("/correo_pediatra",citas.getCorreo_pediatra());
            actualizacion_cita.put("/cel_pediatra",citas.getCel_pediatra());
            actualizacion_cita.put("/url_img_pediatra",citas.getUrl_img_pediatra());
            actualizacion_cita.put("/fecha_registro",citas.getFecha_registro());
            actualizacion_cita.put("/estado",citas.getEstado());
            fire_base.child("Citas").child(_uid_cita).updateChildren(actualizacion_cita);
            Toast.makeText(this, R.string.cita_modificado_ok, Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("SimpleDateFormat")
    private void guardar_cita() {
        String _uid_cita,_asunto,_descripcion,_fecha,_hora,_ui_paciente,_nombre_paciente,
                _correo_paciente,_url_img_paciente,_ui_pediatra,_nombre_pediatra,_correo_pediatra,
                _cel_pediatra,_url_img_pediatra,_fecha_registro;
        int _n_personas,estado;
        Date fecha_reg=new Date();
        _uid_cita= UUID.randomUUID().toString();
        _asunto=asunto.getText().toString();
        _descripcion=descripcion.getText().toString();
        _fecha=fecha.getText().toString();
        _hora=mihora.getText().toString();
        _n_personas=Integer.parseInt(numero_personas.getText().toString());
        _ui_paciente=User.getUid();
        _nombre_paciente=User.getDisplayName()!=null ? User.getDisplayName() : getString(R.string.anonimo);
        _correo_paciente=User.getEmail();
        _url_img_paciente=User.getPhotoUrl()!=null ? User.getPhotoUrl().toString() : URL_IMG_DEFAULT;
        _ui_pediatra=UID_P;
        _nombre_pediatra=NOMBRE_P;
        _cel_pediatra=CEL_PED;
        _correo_pediatra=CORREO_PED;
        _url_img_pediatra=URL_IMG_DEFAULT;
        if (sw_elegir_pediatra.isChecked()){
            _nombre_pediatra=nombre_pediatra.getText().toString();
            _cel_pediatra=cel_pediatra.getText().toString();
            _correo_pediatra=correo_ped.getText().toString();
            _ui_pediatra=uid_pediatra.getText().toString();
            _url_img_pediatra=URL_IMG_PEDIATRA;
        }
        estado=1;
        try{
        DateFormat formatofechahora;
            formatofechahora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            _fecha_registro=formatofechahora.format(fecha_reg);
        citas=new Citas();
        citas.setId(_uid_cita);
        citas.setAsunto(_asunto);
        citas.setDescripcion(_descripcion);
        citas.setFecha(_fecha);
        citas.setHora(_hora);
        citas.setCantidad_personas(_n_personas);
        citas.setUid_paciente(_ui_paciente);
        citas.setNombre_paciente(_nombre_paciente);
        citas.setCorreo_paciente(_correo_paciente);
        citas.setUrl_img_paciente(_url_img_paciente);
        citas.setUid_pediatra(_ui_pediatra);
        citas.setNombre_pediatra(_nombre_pediatra);
        citas.setCorreo_pediatra(_correo_pediatra);
        citas.setCel_pediatra(_cel_pediatra);
        citas.setUrl_img_pediatra(_url_img_pediatra);
        citas.setFecha_registro(_fecha_registro);
        citas.setEstado(estado);
        fire_base.child("Citas").child(_uid_cita).setValue(citas);
        Toast.makeText(this, R.string.cita_registrado_ok, Toast.LENGTH_SHORT).show();
        finish();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    private void cargar_calendario() {
        calenda= Calendar.getInstance();
        anio=calenda.get(Calendar.YEAR);
        mes=calenda.get(Calendar.MONTH);
        dia=calenda.get(Calendar.DAY_OF_MONTH);
        calendario= new DatePickerDialog(CitaActivity.this,R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String _dia=dayOfMonth<10 ? "0"+dayOfMonth : dayOfMonth+"";
                String _mes =(month+1)<10 ? "0"+(month+1) : (month+1)+"";
                fecha.setText(_dia+"/"+_mes+"/"+year);
            }
        },anio,mes,dia);
        calendario.show();
    }
    private void cargar_hora() {
        calenda= Calendar.getInstance();
        hora=calenda.get(Calendar.HOUR_OF_DAY);
        minuto=calenda.get(Calendar.MINUTE);
        reloj= new TimePickerDialog(CitaActivity.this,R.style.datepicker, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String _hora=hourOfDay<10 ? "0"+hourOfDay : hourOfDay+"";
                String _minuto =minute<10 ? "0"+minute : minute+"";
                mihora.setText(_hora+":"+_minuto);
            }
        },hora,minuto,true);
        reloj.show();
    }
}
