package com.chaicopaillag.app.mageli.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.chaicopaillag.app.mageli.Modelo.Persona;
import com.chaicopaillag.app.mageli.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText txt_nombre,    txt_apellidos,txt_numero_doc,txt_numero_hc,txt_direccion,txt_telefono,fecha_nac;
    private RadioButton generoMasculino,generoFemenino;
    private Spinner sp_tipo_doc;
    private Button btn_guardar;
    private String id_ui,especialidad,correo_ui,tipo_doc;
    private int tipo_persona,anio,mes,dia;
    private boolean estado;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase mi_db;
    private DatabaseReference firebase_ref;
    private Calendar calendario;
    private DatePickerDialog mi_popap;
    private ArrayAdapter sp_adap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        inicializar_controles();
        inicializar_servicios();
    }

    private void inicializar_servicios() {
        auth=FirebaseAuth.getInstance();
        mi_db= FirebaseDatabase.getInstance();
        user=auth.getCurrentUser();
        id_ui=user.getUid();
        correo_ui=user.getEmail();
        firebase_ref=mi_db.getReference("Persona");
        Intent intent = getIntent();
        if (intent!=null){
            firebase_ref.child(id_ui).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Persona persona= dataSnapshot.getValue(Persona.class);
                    if (persona!=null){
                        txt_nombre.setText(persona.getNombre());
                        txt_apellidos.setText(persona.getApellidos());
                        txt_numero_doc.setText(persona.getNumero_documento());
                        txt_numero_hc.setText(persona.getNumero_hc());
                        txt_direccion.setText(persona.getDireccion());
                        txt_telefono.setText(persona.getTelefono());
                        fecha_nac.setText(persona.getFecha_nacimiento());
                        if (persona.isGenero()){
                            generoMasculino.setChecked(true);
                        }else {
                            generoFemenino.setChecked(true);
                        }
                        sp_adap = (ArrayAdapter) sp_tipo_doc.getAdapter();
                        int posicion=sp_adap.getPosition(persona.getTipo_doc());
                        sp_tipo_doc.setSelection(posicion);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(PerfilActivity.this,R.string.cancelar_leer_datos_perfil,Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void inicializar_controles() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.regresar_atras));
        getSupportActionBar().setTitle(getString(R.string.perfil));
        txt_nombre=(EditText)findViewById(R.id.txt_nombre);
        txt_apellidos=(EditText)findViewById(R.id.txt_apellidos);
        txt_numero_doc=(EditText)findViewById(R.id.txt_numero_doc);
        txt_numero_hc=(EditText)findViewById(R.id.txt_num_hc);
        txt_direccion=(EditText)findViewById(R.id.txt_direccion);
        txt_telefono=(EditText)findViewById(R.id.txt_telefono);
        fecha_nac=(EditText)findViewById(R.id.txt_fecha_nac);
        generoMasculino=(RadioButton) findViewById(R.id.masculino);
        generoFemenino=(RadioButton) findViewById(R.id.femenino);
        sp_tipo_doc=(Spinner)findViewById(R.id.sp_tipo_doc);
        btn_guardar=(Button) findViewById(R.id.btn_perfil);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        txt_numero_hc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    pregunta_numero_hc();
                }
            }
        });

        fecha_nac.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    cargar_calendario();
                }
            }
        });
        sp_tipo_doc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tipo_doc=sp_tipo_doc.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validar_campos();
            }
        });
    }

    private void modificar_datos() {
        Persona persona;
        String id,nombre,apellidos,numero_documento,numero_hc,direccion, telefono,correo,fecha_nacimient;
        boolean genero;
        Date fecha_registro;
        id=id_ui;
        nombre=txt_nombre.getText().toString();
        apellidos=txt_apellidos.getText().toString();
        numero_documento=txt_numero_doc.getText().toString();
        numero_hc=txt_numero_hc.getText().toString();
        direccion=txt_direccion.getText().toString();
        telefono=txt_telefono.getText().toString();
        correo=correo_ui;
        especialidad="Pediatra";
        estado=true;
        tipo_persona=1;
        if (generoMasculino.isChecked()){
            genero=true;
        }else {
            genero=false;
        }
        fecha_nacimient= fecha_nac.getText().toString();
        fecha_registro = new Date();
        try {
//            String key =firebase_ref.child(id_ui).push().getKey();
            persona= new Persona(
                    id,nombre,apellidos,numero_documento,
                    numero_hc,direccion,telefono,correo,
                    genero,tipo_doc,fecha_nacimient,fecha_registro+"",
                    estado,tipo_persona,especialidad);
//            Map<String, Object> miPersona = persona.miMap();
            Map<String, Object> Act_Persona_especifico = new HashMap<>();
            Act_Persona_especifico.put("/id",persona.getId() );
            Act_Persona_especifico.put("/nombre",persona.getNombre());
            Act_Persona_especifico.put("/apellidos",persona.getApellidos());
            Act_Persona_especifico.put("/numero_documento",persona.getNumero_documento());
            Act_Persona_especifico.put("/numero_hc",persona.getNumero_hc());
            Act_Persona_especifico.put("/direccion",persona.getDireccion());
            Act_Persona_especifico.put("/telefono",persona.getTelefono());
            Act_Persona_especifico.put("/correo",persona.getCorreo());
            Act_Persona_especifico.put("/genero",persona.isGenero());
            Act_Persona_especifico.put("/tipo_doc",persona.getTipo_doc());
            Act_Persona_especifico.put("/fecha_nacimiento",persona.getFecha_nacimiento());
            Act_Persona_especifico.put("/fecha_registro",persona.getFecha_registro());
            Act_Persona_especifico.put("/estado",persona.isEstado());
            Act_Persona_especifico.put("/tipo_persona",persona.getTipo_persona());
            Act_Persona_especifico.put("/especialidad",persona.getEspecialidad());
            firebase_ref.child(id_ui).updateChildren(Act_Persona_especifico);
            Toast.makeText(PerfilActivity.this,R.string.perfil_ok,Toast.LENGTH_LONG).show();
            finish();
        }catch (Exception e){
            System.out.print(e.getMessage());
            Toast.makeText(PerfilActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void pregunta_numero_hc() {
        final AlertDialog.Builder alert_hc = new AlertDialog.Builder(PerfilActivity.this,R.style.progrescolor);
        alert_hc.setTitle(R.string.app_name);
        alert_hc.setMessage(R.string.mensaje_num_hc);
        alert_hc.setPositiveButton(R.string.si,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                txt_numero_hc.setText("");
                txt_numero_hc.isFocused();
            }
        });
        alert_hc.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                txt_numero_hc.setText("0000000000");
                txt_direccion.isFocused();
            }
        });
        alert_hc.show();
    }

    private void cargar_calendario() {
        calendario=Calendar.getInstance();
        anio=calendario.get(Calendar.YEAR);
        mes=calendario.get(Calendar.MONTH);
        dia=calendario.get(Calendar.DAY_OF_MONTH);
        mi_popap= new DatePickerDialog(PerfilActivity.this,R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
           @Override
           public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String dia=dayOfMonth<10 ? "0"+dayOfMonth : dayOfMonth+"";
            String mes =(month+1)<10 ? "0"+(month+1) : (month+1)+"";
            fecha_nac.setText(dia+"/"+mes+"/"+year);
           }
       },anio,mes,dia);
        mi_popap.show();
    }

    private void guardar_datos() {
        Persona persona;
        String id,nombre,apellidos,numero_documento,numero_hc,direccion, telefono,correo,fecha_nacimient;
        boolean genero;
        Date fecha_registro;
        id=id_ui;
        nombre=txt_nombre.getText().toString();
        apellidos=txt_apellidos.getText().toString();
        numero_documento=txt_numero_doc.getText().toString();
        numero_hc=txt_numero_hc.getText().toString();
        direccion=txt_direccion.getText().toString();
        telefono=txt_telefono.getText().toString();
        correo=correo_ui;
        especialidad="...";
        estado=true;
        tipo_persona=2;
        if (generoMasculino.isChecked()){
            genero=true;
        }else {
            genero=false;
        }
        fecha_nacimient= fecha_nac.getText().toString();
        fecha_registro = new Date();
        try {
            persona= new Persona(
                    id,nombre,apellidos,numero_documento,
                    numero_hc,direccion,telefono,correo,
                    genero,tipo_doc,fecha_nacimient,fecha_registro+"",
                    estado,tipo_persona,especialidad);
            firebase_ref.child(id_ui).setValue(persona);
            Toast.makeText(PerfilActivity.this,R.string.perfil_ok,Toast.LENGTH_LONG).show();
            onBackPressed();
            finish();
        }catch (Exception e){
            System.out.print(e.getMessage());
            Toast.makeText(PerfilActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void Validar_campos() {
        String nombre,apellidos,numero_documento,numero_hc,direccion, telefono,fecha_nacimient;
        nombre=txt_nombre.getText().toString();
        apellidos=txt_apellidos.getText().toString();
        numero_documento=txt_numero_doc.getText().toString();
        numero_hc=txt_numero_hc.getText().toString();
        direccion=txt_direccion.getText().toString();
        telefono=txt_telefono.getText().toString();
        fecha_nacimient= fecha_nac.getText().toString();

        if (TextUtils.isEmpty(nombre) || nombre.length()<2||nombre.length()>30){
           txt_nombre.setError(getString(R.string.error_nombre));
           return;
        }else  if (TextUtils.isEmpty(apellidos)||apellidos.length()<4 ||apellidos.length()>50){
            txt_apellidos.setError(getString(R.string.error_apellidos));
            return;
        }else  if (TextUtils.isEmpty(tipo_doc)|| tipo_doc.equals("Selecciona tipo de documento")){
            Toast.makeText(PerfilActivity.this, R.string.error_tipo_doc, Toast.LENGTH_LONG).show();
            return;
        }else  if (TextUtils.isEmpty(numero_documento)||numero_documento.length()!=8){
            txt_numero_doc.setError(getString(R.string.error_numero_documento));
            return;
        }else  if (TextUtils.isEmpty(numero_hc)||numero_hc.length()!=10){
            txt_numero_hc.setError(getString(R.string.error_numero_hc));
            return;
        }else  if (TextUtils.isEmpty(direccion)||direccion.length()<4||direccion.length()>100){
            txt_direccion.setError(getString(R.string.error_direccion));
            return;
        }else  if (TextUtils.isEmpty(telefono)||telefono.length()!=9){
            txt_telefono.setError(getString(R.string.error_telefono));
            return;
        }else  if (TextUtils.isEmpty(fecha_nacimient)|| fecha_nacimient.length()!=10|| !Utiles.validarFecha(fecha_nacimient)){
            fecha_nac.setError(getString(R.string.error_fecha_nacimiento));
            return;
        }else {
            Intent intent = getIntent();
            if (intent.getBooleanExtra("editar",true)){
                modificar_datos();
            }else {
                guardar_datos();
            }
        }
    }
}
