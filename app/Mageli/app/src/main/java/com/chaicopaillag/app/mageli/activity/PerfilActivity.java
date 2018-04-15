package com.chaicopaillag.app.mageli.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.chaicopaillag.app.mageli.R;
import com.chaicopaillag.app.mageli.modelo.Persona;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PerfilActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText txt_nombre,    txt_apellidos,txt_numero_doc,txt_numero_hc,txt_direccion,txt_telefono,fecha_nac;
    private RadioButton generoMasculino,generoFemenino;
    private Spinner sp_tipo_doc;
    private Switch sw_nhc;
    private Button btn_guardar;

    private String id_ui,especialidad,correo_ui;
    private int tipo_persona;
    private boolean estado;
    private Date fecha_reg;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase mi_db;
    DatabaseReference firebase_ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        inicializar_controles();
        mi_db= FirebaseDatabase.getInstance();
        firebase_ref=mi_db.getReference();

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        correo_ui=user.getEmail();
        id_ui=user.getUid();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.egresar_atras));
        getSupportActionBar().setTitle(getString(R.string.perfil));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar_datos();
            }
        });
    }

    private void guardar_datos(){
        Persona persona=null;
        String id,nombre,apellidos,numero_documento,numero_hc,direccion, telefono,correo,especialidad,tipo_doc;
        boolean genero,estado;
        int tipo_persona;
        Date fecha_nacimient, fecha_registro;
        id=id_ui;
        nombre=txt_nombre.getText().toString();
        apellidos=txt_apellidos.getText().toString();
        numero_documento=txt_numero_doc.getText().toString();
        numero_hc=txt_numero_hc.getText().toString();
        direccion=txt_direccion.getText().toString();
        telefono=txt_telefono.getText().toString();
        correo=correo_ui;
        especialidad="Pediatra";
        tipo_doc=sp_tipo_doc.getSelectedItem().toString();
        estado=true;
        tipo_persona=1;
        if (generoMasculino.isChecked()){
            genero=true;
        }else {
            genero=false;
        }
        try {
            SimpleDateFormat parseador_nac = new SimpleDateFormat("dd-MM-yy");
            Date date_nac = parseador_nac.parse(fecha_nac.toString());
            fecha_nacimient=date_nac;
            SimpleDateFormat parseador_reg = new SimpleDateFormat("dd-MM-yy");
            Date date_reg = parseador_reg.parse(fecha_nac.toString());
            fecha_registro=date_reg;
            persona= new Persona(
                    id,nombre,apellidos,numero_documento,
                    numero_hc,direccion,telefono,correo,genero,
                    tipo_persona,fecha_nacimient,fecha_registro,
                    estado,tipo_persona,especialidad);
            firebase_ref.child("Persona").child(id_ui).setValue(persona);
            Toast.makeText(PerfilActivity.this,"Se registro con exito",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            System.out.print(e.getMessage());
        }


    }

    private void inicializar_controles() {
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
    }

}
