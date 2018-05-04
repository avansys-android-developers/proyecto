package com.chaicopaillag.app.mageli.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chaicopaillag.app.mageli.Modelo.Citas;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private  TextView nombre_pediatra,uid_pediatra,correo_ped,esp_ped;
    private SwitchCompat sw_elegir_pediatra;
    private Button btn_consulta;
    private AlertDialog.Builder PopapPediatras;
    private ProgressDialog progress_carga;
    private String UID_P="AUnfN3zdsHevqcOGwfz29lB09Y33";
    private String NOMBRE_P="";
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
        esp_ped=(TextView) findViewById(R.id.especialidad_pediatra_consulta);
        flexbox_pediatra=(FlexboxLayout)findViewById(R.id.fila_pediatra_privada);
        btn_consulta=(Button)findViewById(R.id.btn_consulta);
        sw_elegir_pediatra=(SwitchCompat)findViewById(R.id.sw_consulta_privada);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
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
                            esp_ped.setText("");
                            correo_ped.setText("");
                            UID_P=consulta.getUid_pediatra();
                            NOMBRE_P=consulta.getNombre_pediatra();
                            flexbox_pediatra.setVisibility(View.VISIBLE);
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
        PopapPediatras=new AlertDialog.Builder(ConsultaActivity.this);
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
                                esp_ped.setText(persona.getEspecialidad());
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

    private void actualizar_consulta() {
        String _uid_consulta,_asunto,_descripcion,_ui_paciente,_nombre_paciente,_ui_pediatra,_nombre_pediatra,_fecha_registro;
        boolean flag_respuesta,estado,privacidad;
        Date fecha_hora= new Date();
        Intent intent= getIntent();
        _uid_consulta= intent.getStringExtra("uid_consulta");
        _asunto=asunto.getText().toString();
        _descripcion=descripcion.getText().toString();
        _ui_paciente=User.getUid();
        _nombre_paciente=User.getDisplayName();
        _ui_pediatra=Obtener_Uid_pediatra();
        _nombre_pediatra=NOMBRE_P;
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
            DateFormat formatofechahora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            _fecha_registro=formatofechahora.format(fecha_hora);
            consulta=new Consulta();
            consulta.setId(_uid_consulta);
            consulta.setAsunto(_asunto);
            consulta.setDescripcion(_descripcion);
            consulta.setUid_paciente(_ui_paciente);
            consulta.setNombre_paciente(_nombre_paciente);
            consulta.setNombre_pediatra(_nombre_pediatra);
            consulta.setUid_pediatra(_ui_pediatra);
            consulta.setFecha_registro(_fecha_registro);
            consulta.setFlag_respuesta(flag_respuesta);
            consulta.setEstado(estado);
            consulta.setFlag_privacidad(privacidad);
            Map<String, Object> actualizacion_cita = new HashMap<>();
            actualizacion_cita.put("/id",consulta.getId() );
            actualizacion_cita.put("/asunto",consulta.getAsunto());
            actualizacion_cita.put("/descripcion",consulta.getDescripcion());
            actualizacion_cita.put("/uid_paciente",consulta.getUid_paciente());
            actualizacion_cita.put("/nombre_paciente",consulta.getNombre_paciente());
            actualizacion_cita.put("/uid_pediatra",consulta.getUid_pediatra());
            actualizacion_cita.put("/nombre_pediatra",consulta.getNombre_pediatra());
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

    private void guardar_cita() {
        String _uid_consulta,_asunto,_descripcion,_ui_paciente,_nombre_paciente,_ui_pediatra,_nombre_pediatra,_fecha_registro;
        boolean flag_respuesta,estado,privacidad;
        Date fecha_hora= new Date();
        _uid_consulta= UUID.randomUUID().toString();
        _asunto=asunto.getText().toString();
        _descripcion=descripcion.getText().toString();
        _ui_paciente=User.getUid();
        _nombre_paciente=User.getDisplayName();
        _ui_pediatra=Obtener_Uid_pediatra();
        _nombre_pediatra=getString(R.string.anonimo);
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
            DateFormat formatofechahora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            _fecha_registro=formatofechahora.format(fecha_hora);
            consulta=new Consulta();
            consulta.setId(_uid_consulta);
            consulta.setAsunto(_asunto);
            consulta.setDescripcion(_descripcion);
            consulta.setUid_paciente(_ui_paciente);
            consulta.setNombre_paciente(_nombre_paciente);
            consulta.setNombre_pediatra(_nombre_pediatra);
            consulta.setUid_pediatra(_ui_pediatra);
            consulta.setFecha_registro(_fecha_registro.toString());
            consulta.setFlag_respuesta(flag_respuesta);
            consulta.setEstado(estado);
            consulta.setFlag_privacidad(privacidad);
            fire_base.child("Consultas").child(_uid_consulta).setValue(consulta);
            Toast.makeText(this, R.string.consulta_registrado_ok, Toast.LENGTH_SHORT).show();
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
}
