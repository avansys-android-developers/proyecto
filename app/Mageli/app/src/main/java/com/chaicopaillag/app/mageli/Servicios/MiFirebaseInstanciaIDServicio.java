package com.chaicopaillag.app.mageli.Servicios;
import android.util.Log;
import com.chaicopaillag.app.mageli.Activity.Utiles;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MiFirebaseInstanciaIDServicio  extends FirebaseInstanceIdService{
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Utiles.TOKEN = FirebaseInstanceId.getInstance().getToken();
        Log.d("Mi Token", "Actualizado : " + Utiles.TOKEN);
    }
}
