package com.chaicopaillag.app.mageli.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chaicopaillag.app.mageli.Fragmento.CitasFragment;
import com.chaicopaillag.app.mageli.Fragmento.ConsultasFragment;
import com.chaicopaillag.app.mageli.Fragmento.CuentasFragment;
import com.chaicopaillag.app.mageli.Fragmento.InicioFragment;
import com.chaicopaillag.app.mageli.Fragmento.PerfilFragment;
import com.chaicopaillag.app.mageli.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private NavigationView menu_slide;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private TextView nombreUser,correoUser;
    private ImageView imgUsuario;
    private View nav_cabecera;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        inicializar_servicios();
        inicializar_controles();

    }

    private void inicializar_servicios() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.contenedor_menu);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.abrir_nav, R.string.cerar_nav);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        menu_slide = (NavigationView) findViewById(R.id.nav_sidebar);
        nav_cabecera=(View)menu_slide.getHeaderView(0);

        nombreUser=(TextView) nav_cabecera.findViewById(R.id.nombreUsuario);
        correoUser=(TextView)nav_cabecera.findViewById(R.id.correoUsuario);
        imgUsuario=(ImageView)nav_cabecera.findViewById(R.id.imgUsuario);
        menu_slide.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                boolean transframe=false;
                Fragment mi_fragmento=null;
                int id = item.getItemId();
                switch (id){
                    case R.id.item_perfil:
                        mi_fragmento=new PerfilFragment();
                        transframe=true;
                        break;
                    case R.id.item_citas:
                        mi_fragmento=new CitasFragment();
                        transframe=true;
                        break;
                    case R.id.item_consulta:
                        mi_fragmento=new ConsultasFragment();
                        transframe=true;
                        break;
                    case R.id.item_cuenta:
                        mi_fragmento=new CuentasFragment();
                        transframe=true;
                        break;
                    case R.id.item_salir:
                        SalirMenu();
                        break;
                }
                if (transframe){
                    ponerFragmento(mi_fragmento);
                    getSupportActionBar().setTitle(item.getTitle());
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.contenedor_menu);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        ponerFragmento(new InicioFragment());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    colocar_datos_usuario(user);
                } else {
                    Ir_a_login();
                }
            }
        };
    }
    private void ponerFragmento(Fragment mi_fragmento) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedor,mi_fragmento)
                .commit();
    }
    private void inicializar_controles() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    private void colocar_datos_usuario(FirebaseUser user) {
        nombreUser.setText(user.getDisplayName());
        correoUser.setText(user.getEmail());
        if (user.getPhotoUrl()!=null){
            Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(imgUsuario);
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.contenedor_menu);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_extra, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item_notificacion:
                item.setIcon(R.drawable.si_notificacion);
                return true;
            case R.id.item_inicio:
                ponerFragmento(new InicioFragment());
                return true;
            default:
                    return true;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")

    private void SalirMenu() {

           final AlertDialog.Builder alerta= new AlertDialog.Builder(MenuActivity.this);
            alerta.setTitle(R.string.app_name);
            alerta.setMessage(R.string.mensaje_salir);
            alerta.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseAuth.getInstance().signOut();
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Toast.makeText(getApplicationContext(), R.string.cerrar_sesion, Toast.LENGTH_SHORT).show();
                                Ir_a_login();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.error_conexion_google, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            alerta.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    menu_slide.setCheckedItem(0);
                }
            });
            alerta.show();
    }
    private void Ir_a_login() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

    }
}
