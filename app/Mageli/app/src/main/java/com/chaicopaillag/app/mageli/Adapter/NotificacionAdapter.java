package com.chaicopaillag.app.mageli.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chaicopaillag.app.mageli.R;

public class NotificacionAdapter {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public TextView titutlo;
        public TextView mensaje;
        public TextView fecha_nofity;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.cardnotify);
            titutlo=(TextView)itemView.findViewById(R.id.item_titulo_notify);
            mensaje=(TextView)itemView.findViewById(R.id.item_mensaje_notify);
            fecha_nofity=(TextView)itemView.findViewById(R.id.item_fecha_notify);
        }

        public void setTitutlo(String titutlo) {
            this.titutlo.setText(titutlo);
        }

        public void setMensaje(String mensaje) {
            this.mensaje.setText(mensaje);
        }

        public void setFecha_nofity(String fecha_nofity) {
            this.fecha_nofity.setText(fecha_nofity);
        }
    }
}
