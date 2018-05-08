package com.chaicopaillag.app.mageli.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chaicopaillag.app.mageli.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private EditText txt_correo,txt_contrasenia;
    private Button btn_iniciar,btn_registro,btn_restablecer_contra;
    private SignInButton btn_google;
    private GoogleApiClient googleApiClient;
    public static final int SIGN_IN_CODE = 9001;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser usuario;
    private AlertDialog.Builder alerta;
    private ProgressDialog progress_carga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Inicializar_servicios();
        Inicializar_controles();

    }

    private void Inicializar_controles() {
        progress_carga=new ProgressDialog(LoginActivity.this,R.style.progrescolor);
        txt_correo=(EditText)findViewById(R.id.txtcorreo);
        txt_contrasenia=(EditText)findViewById(R.id.txtcontrasenia);

        btn_iniciar=(Button)findViewById(R.id.btnlogin);
        btn_registro=(Button) findViewById(R.id.btnregistrateahora);

        btn_restablecer_contra=(Button) findViewById(R.id.btnrestablecer_contrasenia);
        btn_google=(SignInButton)findViewById(R.id.btngoogle);

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LoginActivity.this,RegistroUsuarioActivity.class);
                startActivity(intent);
            }
        });
        btn_restablecer_contra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo,contrasenia;
                correo=txt_correo.getText().toString();

                if (correo.equals("")){
                    txt_correo.setError(getString(R.string.error_correo));
                    return;
                }else if (!validarcorreo(correo)){
                    txt_correo.setError(getString(R.string.error_correo_no_valido));
                    return;
                }
                firebaseAuth.sendPasswordResetEmail(correo)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, getText(R.string.restablecer_contrasia_mensaje), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        btn_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validarlogeo();
            }
        });
        btn_google.setColorScheme(SignInButton.COLOR_DARK);
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });
    }

    private void Inicializar_servicios() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                usuario = firebaseAuth.getCurrentUser();
                if (usuario != null) {
                    if (usuario.isEmailVerified()){
                        if(progress_carga.isShowing()){
                            progress_carga.dismiss();
                        }
                        Ir_a_inicio();
                    }else {
                        FirebaseAuth.getInstance().signOut();
                        mensaje_verifa_tu_correo();
                    }
                }
            }
        };
    }

    private void mensaje_verifa_tu_correo() {
        alerta= new AlertDialog.Builder(LoginActivity.this);
        alerta.setTitle(R.string.app_name);
        alerta.setMessage(R.string.no_verificado_correo);
        alerta.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alerta.show();
    }


    private void Ir_a_inicio() {
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
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
        else if (!validarcorreo(correo)){
            txt_correo.setError(getString(R.string.error_correo_no_valido));
            return;
        }else {
            progres_iniciando_sesion();
            firebaseAuth.signInWithEmailAndPassword(correo,contrasenia)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Ir_a_inicio();
                            }else {
                                progress_carga.dismiss();
                                Toast.makeText(LoginActivity.this, getText(R.string.error_ingreso), Toast.LENGTH_SHORT).show();
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
    private void progres_iniciando_sesion() {
        progress_carga.setTitle(R.string.app_name);
        progress_carga.setMessage(getString(R.string.verificar_sesion));
        progress_carga.setIndeterminate(true);
        progress_carga.setCancelable(false);
        progress_carga.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            firebaseAuthWithGoogle(result.getSignInAccount());
        }else {
            Toast.makeText(this, R.string.error_conexion_google, Toast.LENGTH_SHORT).show();
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        progres_iniciando_sesion();
        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progress_carga.dismiss();
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.error_login_firebase, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, R.string.error_conexion_google, Toast.LENGTH_SHORT).show();
    }
}
