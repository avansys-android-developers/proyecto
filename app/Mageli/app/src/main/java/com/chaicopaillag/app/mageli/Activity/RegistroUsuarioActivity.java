package com.chaicopaillag.app.mageli.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chaicopaillag.app.mageli.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroUsuarioActivity extends AppCompatActivity {
    private EditText txt_correo,txt_contrasenia,getTxt_contraseniabiz;
    private Button btn_registro, btnIrlogin;
    private FirebaseAuth firebase_autent;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        firebase_autent=  FirebaseAuth.getInstance();

        txt_correo=(EditText)findViewById(R.id.txtcorreo);
        txt_contrasenia=(EditText)findViewById(R.id.txtcontrasenia);
        getTxt_contraseniabiz=(EditText)findViewById(R.id.txtcontraseniabiz);

        btn_registro=(Button)findViewById(R.id.btnregistro);
        btnIrlogin=(Button) findViewById(R.id.btn_ir_login);

        btnIrlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegresarLogin();
            }
        });

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarUsuario();
            }
        });
    }
    private void RegresarLogin() {
        Intent intent = new Intent(RegistroUsuarioActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void RegistrarUsuario(){

        String correo=txt_correo.getText().toString();
        String contrasenia= txt_contrasenia.getText().toString();
        String contraseniabiz=getTxt_contraseniabiz.getText().toString();

        if (TextUtils.isEmpty(correo)){
            txt_correo.setError(getString(R.string.error_correo));
            return;
        }
        else if (!validarcorreo(correo)){
            txt_correo.setError(getString(R.string.error_correo_no_valido));
            return;
        }
        else if (TextUtils.isEmpty(contrasenia)){
            txt_contrasenia.setError(getString(R.string.error_contrasenia));
            return;
        }
        else if (TextUtils.isEmpty(contraseniabiz)){
            getTxt_contraseniabiz.setError(getString(R.string.error_contraseniabiz));
            return;
        }
        else if (txt_contrasenia.length()<6){
            txt_contrasenia.setError(getString(R.string.error_contrasenia_no_valido));
            return;
        }else if (!Objects.equals(contrasenia,contraseniabiz)){
            txt_contrasenia.setError(getString(R.string.error_contrasenia_no_igual));
            getTxt_contraseniabiz.setError(getString(R.string.error_contrasenia_no_igual));
            return;
        }
        else{
            firebase_autent.createUserWithEmailAndPassword(correo,contrasenia).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegistroUsuarioActivity.this,R.string.registro_exito,Toast.LENGTH_LONG).show();
                        VerificarCorreo();
                        RegresarLogin();
                    }else{
                        Toast.makeText(RegistroUsuarioActivity.this,R.string.registro_error, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void VerificarCorreo() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistroUsuarioActivity.this,getText(R.string.verificacion_correo),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public static boolean validarcorreo(String correo){

        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);

        return matcher.matches();
    }
}
