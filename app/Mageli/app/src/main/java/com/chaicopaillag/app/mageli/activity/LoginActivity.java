package com.chaicopaillag.app.mageli.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chaicopaillag.app.mageli.R;

public class LoginActivity extends AppCompatActivity {

    EditText txt_correo,txt_contrasenia;
    Button btn_iniciar,btn_registro;
    FloatingActionButton btn_facebook,btn_google;
    TextInputLayout inputcorreo,inputcontrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        inputcorreo=(TextInputLayout)findViewById(R.id.inputcorreolog);
//        inputcontrasenia=(TextInputLayout)findViewById(R.id.inputlogcontrasenialog);

        txt_correo=(EditText)findViewById(R.id.txtcorreolog);
        txt_contrasenia=(EditText)findViewById(R.id.txtcontrasenialog);

        btn_iniciar=(Button)findViewById(R.id.btnlogin);
        btn_registro=(Button)findViewById(R.id.btnregistrarse);

        btn_facebook=(FloatingActionButton)findViewById(R.id.btnfacebooklog);
        btn_google=(FloatingActionButton)findViewById(R.id.btngooglelog);



        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LoginActivity.this,RegistroUsuarioActivity.class);
                startActivity(intent);
            }
        });

        btn_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validarlogeo();
            }
        });

        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
    public  void Validarlogeo(){

        String correo,contrasenia;
        correo=txt_correo.getText().toString();
        contrasenia=txt_contrasenia.getText().toString();

        if (correo.equals("")){
            txt_correo.setError(getString(R.string.error_correo));
        }

        if(contrasenia.equals("")){
            txt_contrasenia.setError(getString(R.string.error_contrasenia));
        }

    }
}
