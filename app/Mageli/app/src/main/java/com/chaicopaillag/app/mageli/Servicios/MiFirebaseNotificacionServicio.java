package com.chaicopaillag.app.mageli.Servicios;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.chaicopaillag.app.mageli.Activity.MenuActivity;
import com.chaicopaillag.app.mageli.Modelo.Notificacion;
import com.chaicopaillag.app.mageli.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class MiFirebaseNotificacionServicio extends FirebaseMessagingService {
    DatabaseReference firebase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("TAG","Mensaje : "+remoteMessage.getNotification().getBody());
        if(remoteMessage.getData().size()>0){
            AbrirNotificacion(remoteMessage);
            GuardarNotificacion(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }else {
        }
    }


    @SuppressLint("SimpleDateFormat")
    public void GuardarNotificacion(String titulo,String mensaje) {
        firebase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        if(firebase!=null){
            Date fecha_reg=new Date();
            String fecha_notify;
            DateFormat formatofechahora;
            formatofechahora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            fecha_notify=formatofechahora.format(fecha_reg);
            String UID_Notify= UUID.randomUUID().toString();
            Notificacion notificacion= new Notificacion(
                    UID_Notify,
                    titulo,
                    mensaje,
                    fecha_notify,
                    firebaseUser.getUid()
            );
            firebase.child(Notificacion.class.getSimpleName()).child(firebaseUser.getUid()).child(UID_Notify).setValue(notificacion);
        }

    }

    private void AbrirNotificacion(RemoteMessage remoteMessage) {

        Intent intent= new Intent(this, MenuActivity.class);
        intent.putExtra("notificacion",remoteMessage.getNotification().getTitle());
        PendingIntent pendingIntent= PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.notificacion)
                .setColor(getResources().getColor(R.color.celeste_oscuro))
                .setAutoCancel(true)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSound(uri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify("TAG",10, builder.build());

    }
}
