package com.chaicopaillag.app.mageli.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.chaicopaillag.app.mageli.R;
import com.google.android.flexbox.FlexboxLayout;

import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CitaActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Calendar calenda;
    private TimePickerDialog reloj;
    private DatePickerDialog calendario;
    private int anio,mes,dia,hora,minuto;
    private EditText asunto,descripcion,fecha,mihora,numero_personas;
    private CircleImageView img_perfil_pediatra;
    private  TextView nombre_pediatra,espeecialidad_pediatra;
    private SwitchCompat sw_elegir_pediatra;
    private Button btn_cita;
    private FlexboxLayout flexbox_pediatra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cita);
        inicializar_controles();
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
        espeecialidad_pediatra=(TextView) findViewById(R.id.especialidad_pediatra_cita);
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
                    flexbox_pediatra.setVisibility(View.VISIBLE);
                }else {
                    flexbox_pediatra.setVisibility(View.GONE);
                }
            }
        });
    }
    private void cargar_calendario() {
        calenda= Calendar.getInstance();
        anio=calenda.YEAR;
        mes=calenda.MONTH;
        dia=calenda.DAY_OF_MONTH;
        calendario= new DatePickerDialog(CitaActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        hora=calenda.HOUR;
        minuto=calenda.MINUTE;
        reloj= new TimePickerDialog(CitaActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mihora.setText(hourOfDay+":"+minute);
            }
        },hora,minuto,false);
        reloj.show();
    }
}
