package com.chaicopaillag.app.mageli.Servicios;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MiFirebaseInstanciaIDServicio  extends FirebaseInstanceIdService{
    private DatabaseReference firebase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String mitoken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Mi Token", "Refreshed token: " + mitoken);
        AgregarTokenMobil(mitoken);
    }

    private void AgregarTokenMobil(String mitoken) {
        firebase= FirebaseDatabase.getInstance().getReference("TokensApp");
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebase.child(firebaseUser.getUid()).child("token").setValue(mitoken);
    }

}
