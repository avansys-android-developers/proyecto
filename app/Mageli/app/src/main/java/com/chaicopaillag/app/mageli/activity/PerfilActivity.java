package com.chaicopaillag.app.mageli.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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

public class PerfilActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText txt_nombre,    txt_apellidos,txt_numero_doc,txt_numero_hc,txt_direccion,txt_telefono,fecha_nac;
    private RadioButton generoMasculino,generoFemenino;
    private Spinner sp_tipo_doc;
    private TextInputLayout text_inputnum_hc;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        inicializar_controles();
        inicializar_servicios();

        
    }

    private void inicializar_servicios() {
        mi_db= FirebaseDatabase.getInstance();
        firebase_ref=mi_db.getReference("Persona");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        id_ui=user.getUid();
        Intent intent = getIntent();
        if (intent!=null){
            firebase_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Persona persona= dataSnapshot.getValue(Persona.class);
                    if (persona!=null){
                        txt_nombre.setText(persona.getNombre());
                        txt_apellidos.setText(persona.getApellidos());
                        sp_tipo_doc.setSelected(true);
                        txt_numero_doc.setText(persona.getNumero_documento());
                        txt_numero_hc.setText(persona.getNumero_hc());
                        txt_direccion.setText(persona.getDireccion());
                        txt_telefono.setText(persona.getTelefono());
                        fecha_nac.setText(persona.getFecha_nacimiento());
                        if (persona.isGenero()){
                            generoMasculino.isChecked();
                        }else {
                            generoFemenino.isChecked();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    private void inicializar_controles() {
        txt_nombre=(EditText)findViewById(R.id.txt_nombre);
        txt_apellidos=(EditText)findViewById(R.id.txt_apellidos);
        txt_numero_doc=(EditText)findViewById(R.id.txt_numero_doc);
        text_inputnum_hc=(TextInputLayout)findViewById(R.id.input_num_hc);
        txt_numero_hc=(EditText)findViewById(R.id.txt_num_hc);
        txt_direccion=(EditText)findViewById(R.id.txt_direccion);
        txt_telefono=(EditText)findViewById(R.id.txt_telefono);
        fecha_nac=(EditText)findViewById(R.id.txt_fecha_nac);
        generoMasculino=(RadioButton) findViewById(R.id.masculino);
        generoFemenino=(RadioButton) findViewById(R.id.femenino);
        sp_tipo_doc=(Spinner)findViewById(R.id.sp_tipo_doc);
        btn_guardar=(Button) findViewById(R.id.btn_perfil);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.regresar_atras));
        getSupportActionBar().setTitle(getString(R.string.perfil));
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
                guardar_datos();
            }
        });
    }

    private void pregunta_numero_hc() {
        final AlertDialog.Builder alert_hc = new AlertDialog.Builder(PerfilActivity.this);
        alert_hc.setTitle(R.string.numero_hc_titulo);
        alert_hc.setMessage(R.string.mensaje_num_hc);
        alert_hc.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                text_inputnum_hc.setEnabled(true);
                txt_numero_hc.setEnabled(true);
                txt_numero_hc.isFocused();
            }
        });
        alert_hc.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                text_inputnum_hc.setEnabled(false);
                txt_numero_hc.setEnabled(false);
            }
        });
        alert_hc.show();
    }

    private void cargar_calendario() {
        calendario=Calendar.getInstance();
        anio=calendario.YEAR;
        mes=calendario.MONTH;
        dia=calendario.DAY_OF_MONTH;
        mi_popap= new DatePickerDialog(PerfilActivity.this, new DatePickerDialog.OnDateSetListener() {
           @Override
           public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            fecha_nac.setText(dayOfMonth+"/"+(month+1)+"/"+year);
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
            persona= new Persona(
                    id,nombre,apellidos,numero_documento,
                    numero_hc,direccion,telefono,correo,
                    genero,tipo_doc,fecha_nacimient,fecha_registro+"",
                    estado,tipo_persona,especialidad);
            firebase_ref.child("Persona").child(id_ui).setValue(persona);
            Toast.makeText(PerfilActivity.this,R.string.perfil_ok,Toast.LENGTH_LONG).show();
            onBackPressed();
            finish();
        }catch (Exception e){
            System.out.print(e.getMessage());
            Toast.makeText(PerfilActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
