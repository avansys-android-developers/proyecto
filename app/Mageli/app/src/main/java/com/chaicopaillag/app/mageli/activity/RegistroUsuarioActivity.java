package com.chaicopaillag.app.mageli.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chaicopaillag.app.mageli.R;
import com.google.firebase.auth.FirebaseAuth;

public class RegistroUsuarioActivity extends AppCompatActivity {
    EditText txt_correo,txt_contrasenia,getTxt_contraseniabiz;
    Button btn_registro;
    FloatingActionButton btn_facebook,btn_google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        txt_correo=(EditText)findViewById(R.id.txtcorreoreg);
        txt_contrasenia=(EditText)findViewById(R.id.txtcontraseniareg);
        getTxt_contraseniabiz=(EditText)findViewById(R.id.txtcontraseniabizreg);

        btn_registro=(Button)findViewById(R.id.btnregistro);

        btn_facebook=(FloatingActionButton)findViewById(R.id.btnfacebookreg);
        btn_google=(FloatingActionButton)findViewById(R.id.btngooglereg);

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void ValidarRegistro(){

        String correo=txt_correo.getText().toString();
        String contrasenia= txt_contrasenia.getText().toString();

        if (correo.isEmpty()){

        }
    }
}
