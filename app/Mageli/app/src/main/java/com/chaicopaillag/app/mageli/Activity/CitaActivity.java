package com.chaicopaillag.app.mageli.Activity;

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
    private  TextView nombre_pediatra,uid_pediatra,correo_ped,esp_ped;
    private SwitchCompat sw_elegir_pediatra;
    private Button btn_cita;
    private AlertDialog.Builder PopapPediatras;
    private ProgressDialog progress_carga;
    private String UID_P="AUnfN3zdsHevqcOGwfz29lB09Y33";
    private String NOMBRE_P="";
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
        esp_ped=(TextView) findViewById(R.id.especialidad_pediatra_cita);
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
                    flexbox_pediatra.setVisibility(View.INVISIBLE);
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
                        esp_ped.setText("");
                        correo_ped.setText("");
                        flexbox_pediatra.setVisibility(View.VISIBLE);
                        UID_P=citas.getUid_pediatra();
                        NOMBRE_P=citas.getNombre_pediatra();
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
                        esp_ped.setText(persona.getEspecialidad());
                        if (persona.getFoto_url()!=null){
                            Glide.with(getApplicationContext()).load(persona.getFoto_url()).into(img_perfil_pediatra);
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

    private void modificar_cita() {
        String _uid_cita,_asunto,_descripcion,_fecha,_hora,_ui_paciente,_nombre_paciente,_ui_pediatra,_nombre_pediatra;
        boolean flag_atendido,flag_cancelado,flag_postergado,estado;
        int _n_personas;
        Date _fecha_registro;
        Intent inten= getIntent();
        _uid_cita= inten.getStringExtra("uid_cita");
        _asunto=asunto.getText().toString();
        _descripcion=descripcion.getText().toString();
        _fecha=fecha.getText().toString();
        _hora=mihora.getText().toString();
        _n_personas=Integer.parseInt(numero_personas.getText().toString());
        _ui_paciente=User.getUid();
        _nombre_paciente=User.getDisplayName()!=null ? User.getDisplayName() : getString(R.string.anonimo);
        _ui_pediatra=Obtener_Uid_pediatra();
        _nombre_pediatra=NOMBRE_P;
        if (sw_elegir_pediatra.isChecked()){
            _nombre_pediatra=nombre_pediatra.getText().toString();
        }
        flag_atendido=false;
        flag_cancelado=false;
        flag_postergado=true;
        estado=true;
        try{
            _fecha_registro= new Date();
            citas=new Citas();
            citas.setId(_uid_cita);
            citas.setAsunto(_asunto);
            citas.setDescripcion(_descripcion);
            citas.setFecha(_fecha);
            citas.setHora(_hora);
            citas.setCantidad_personas(_n_personas);
            citas.setUid_paciente(_ui_paciente);
            citas.setNombre_paciente(_nombre_paciente);
            citas.setUid_pediatra(_ui_pediatra);
            citas.setNombre_pediatra(_nombre_pediatra);
            citas.setFecha_registro(_fecha_registro.toString());
            citas.setFlag_atendido(flag_atendido);
            citas.setFlag_cancelado(flag_cancelado);
            citas.setFlag_postergado(flag_postergado);
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
            actualizacion_cita.put("/uid_pediatra",citas.getUid_pediatra());
            actualizacion_cita.put("/nombre_pediatra",citas.getNombre_pediatra());
            actualizacion_cita.put("/fecha_registro",citas.getFecha_registro());
            actualizacion_cita.put("/flag_atendido",citas.isFlag_atendido());
            actualizacion_cita.put("/flag_cancelado",citas.isFlag_cancelado());
            actualizacion_cita.put("/flag_postergado",citas.isFlag_postergado());
            actualizacion_cita.put("/estado",citas.isEstado());
            fire_base.child("Citas").child(_uid_cita).updateChildren(actualizacion_cita);
            Toast.makeText(this, R.string.cita_modificado_ok, Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void guardar_cita() {
        String _uid_cita,_asunto,_descripcion,_fecha,_hora,_ui_paciente,_nombre_paciente,_ui_pediatra,_nombre_pediatra;
        boolean flag_atendido,flag_cancelado,flag_postergado,estado;
        int _n_personas;
        Date _fecha_registro;
        _uid_cita= UUID.randomUUID().toString();
        _asunto=asunto.getText().toString();
        _descripcion=descripcion.getText().toString();
        _fecha=fecha.getText().toString();
        _hora=mihora.getText().toString();
        _n_personas=Integer.parseInt(numero_personas.getText().toString());
        _ui_paciente=User.getUid();
        _nombre_paciente=User.getDisplayName()!=null ? User.getDisplayName() : getString(R.string.anonimo);
        _ui_pediatra=Obtener_Uid_pediatra();
        _nombre_pediatra=NOMBRE_P;
        if (sw_elegir_pediatra.isChecked()){
            _nombre_pediatra=nombre_pediatra.getText().toString();
        }
        flag_atendido=false;
        flag_cancelado=false;
        flag_postergado=false;
        estado=true;
        try{
        _fecha_registro= new Date();
        citas=new Citas();
        citas.setId(_uid_cita);
        citas.setAsunto(_asunto);
        citas.setDescripcion(_descripcion);
        citas.setFecha(_fecha);
        citas.setHora(_hora);
        citas.setCantidad_personas(_n_personas);
        citas.setUid_paciente(_ui_paciente);
        citas.setNombre_paciente(_nombre_paciente);
        citas.setUid_pediatra(_ui_pediatra);
        citas.setNombre_pediatra(_nombre_pediatra);
        citas.setFecha_registro(_fecha_registro.toString());
        citas.setFlag_atendido(flag_atendido);
        citas.setFlag_cancelado(flag_cancelado);
        citas.setFlag_postergado(flag_postergado);
        citas.setEstado(estado);
        fire_base.child("Citas").child(_uid_cita).setValue(citas);
        Toast.makeText(this, R.string.cita_registrado_ok, Toast.LENGTH_SHORT).show();
        finish();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    private String Obtener_Uid_pediatra(){
        if (sw_elegir_pediatra.isChecked()){
            UID_P=uid_pediatra.getText().toString();
        }
        return UID_P;
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
