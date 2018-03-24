package com.chaicopaillag.app.mageli.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chaicopaillag.app.mageli.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroUsuarioActivity extends AppCompatActivity {
    EditText txt_correo,txt_contrasenia,getTxt_contraseniabiz;
    Button btn_registro;
    FloatingActionButton btn_facebook,btn_google;

    FirebaseAuth firebase_autent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        firebase_autent=  FirebaseAuth.getInstance();

        txt_correo=(EditText)findViewById(R.id.txtcorreo);
        txt_contrasenia=(EditText)findViewById(R.id.txtcontrasenia);
        getTxt_contraseniabiz=(EditText)findViewById(R.id.txtcontraseniabiz);

        btn_registro=(Button)findViewById(R.id.btnregistro);

        btn_facebook=(FloatingActionButton)findViewById(R.id.btnfacebook);
        btn_google=(FloatingActionButton)findViewById(R.id.btngoogle);


        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               RegistrarUsuario();
            }
        });

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
        else if (txt_contrasenia.length()<6){
            txt_contrasenia.setError(getString(R.string.error_contrasenia_no_valido));
            return;
        }else if (contrasenia!=contraseniabiz){
            txt_contrasenia.setError(getString(R.string.error_contrasenia_no_igual));
            getTxt_contraseniabiz.setError(getString(R.string.error_contrasenia_no_igual));
            return;
        }
        else{
            firebase_autent.createUserWithEmailAndPassword(correo,contrasenia).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                    }else{

                    }
                }
            });
        }
    }
    public static boolean validarcorreo(String correo){

        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);

        return matcher.matches();
    }
}
