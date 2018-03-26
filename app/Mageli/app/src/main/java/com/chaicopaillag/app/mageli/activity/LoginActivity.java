package com.chaicopaillag.app.mageli.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chaicopaillag.app.mageli.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText txt_correo,txt_contrasenia;
    Button btn_iniciar,btn_registro;
    TextInputLayout inputcorreo,inputcontrasenia;

    private LoginButton btn_facebook;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        txt_correo=(EditText)findViewById(R.id.txtcorreo);
        txt_contrasenia=(EditText)findViewById(R.id.txtcontrasenia);

        btn_iniciar=(Button)findViewById(R.id.btnlogin);
        btn_registro=(Button)findViewById(R.id.btnregistrarse);

        btn_facebook=(LoginButton) findViewById(R.id.btnfacebook);
//        btn_google=(FloatingActionButton)findViewById(R.id.btngoogle);



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

        btn_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Ir_a_inicio();
            }

            @Override
            public void onCancel() {
            Toast.makeText(getApplicationContext(),getString(R.string.cancela_sesion_facebook),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),getString(R.string.error_sesion_facebook),Toast.LENGTH_LONG).show();
            }
        });

        if(AccessToken.getCurrentAccessToken() != null){
            Ir_a_inicio();
        }


    }

    private void Ir_a_inicio() {
        Intent intent = new Intent(LoginActivity.this, InicioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
    public static boolean validarcorreo(String correo){

        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);

        return matcher.matches();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
