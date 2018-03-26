package com.chaicopaillag.app.mageli.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chaicopaillag.app.mageli.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

public class InicioActivity extends AppCompatActivity {

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        btn=(Button)findViewById(R.id.btnsalir);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            LoginManager.getInstance().logOut();
            Ir_a_login();
            }
        });

        if (AccessToken.getCurrentAccessToken() == null) {
            Ir_a_login();
        }
    }

    private void Ir_a_login() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(View view) {
        LoginManager.getInstance().logOut();
        Ir_a_login();
    }
}
