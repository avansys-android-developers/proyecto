package com.chaicopaillag.app.mageli.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.chaicopaillag.app.mageli.R;
public class CitasAdapter{

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public TextView asunto;
        public TextView nombre_peditra;
        public TextView descripcion;
        public TextView fecha;
        public TextView hora;
        public TextView estado;
        public Button btn_eliminar_cita,btn_cancelar_cita,btn_posponer;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.carview);
            asunto=(TextView)itemView.findViewById(R.id.item_asunto_cita);
            descripcion=(TextView)itemView.findViewById(R.id.item_descripcion_cita);
            fecha=(TextView)itemView.findViewById(R.id.item_fecha);
            hora=(TextView)itemView.findViewById(R.id.item_hora_cita);
            estado=(TextView)itemView.findViewById(R.id.item_estado_cita);
            nombre_peditra=(TextView)itemView.findViewById(R.id.item_cita_nombre_pediatra);
            btn_eliminar_cita=(Button)itemView.findViewById(R.id.btn_eliminar_cita);
            btn_cancelar_cita=(Button)itemView.findViewById(R.id.btn_cancelar_cita);
            btn_posponer=(Button)itemView.findViewById(R.id.btn_posponer_cita);
        }

        public void setAsunto(String asunto) {
            this.asunto.setText(asunto);
        }

        public void setDescripcion(String descripcion) {
            this.descripcion.setText(descripcion);
        }

        public void setFecha(String fecha) {
            this.fecha.setText(fecha);
        }

        public void setHora(String hora) {
            this.hora.setText(hora);
        }

        public void setEstado(String estado) {
            this.estado.setText(estado);
        }
        public void setNombre_peditra(String nombre_peditra){
            this.nombre_peditra.setText(nombre_peditra);
        }

    }
}
//https://medium.com/@beingrahul/firebase-cloud-firestore-v-s-firebase-realtime-database-931d4265d4b0