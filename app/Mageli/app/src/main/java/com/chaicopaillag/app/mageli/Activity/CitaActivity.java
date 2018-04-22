package com.chaicopaillag.app.mageli.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chaicopaillag.app.mageli.R;

public class CitaActivity extends AppCompatActivity {
    private Toolbar toolbar;
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }
}
