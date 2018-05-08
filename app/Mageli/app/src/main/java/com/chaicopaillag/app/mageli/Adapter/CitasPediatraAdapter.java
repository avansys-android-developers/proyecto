package com.chaicopaillag.app.mageli.Adapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.chaicopaillag.app.mageli.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CitasPediatraAdapter {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public TextView asunto;
        public TextView nombre_paciente;
        public TextView descripcion;
        public TextView fecha;
        public TextView hora;
        public TextView estado;
        public TextView cantidad_personas;
        public Button btn_atendido,btn_no_atendido;
        public CircleImageView img_paciente;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.carview);
            asunto=(TextView)itemView.findViewById(R.id.item_asunto_cita_pediatra);
            descripcion=(TextView)itemView.findViewById(R.id.item_descripcion_cita_pediatra);
            fecha=(TextView)itemView.findViewById(R.id.item_fecha_cita_pediatra);
            hora=(TextView)itemView.findViewById(R.id.item_hora_cita_pediatra);
            estado=(TextView)itemView.findViewById(R.id.item_estado_cita_pediatra);
            nombre_paciente=(TextView)itemView.findViewById(R.id.item_cita_nombre_pacient);
            btn_atendido=(Button)itemView.findViewById(R.id.btn_si_tendido);
            btn_no_atendido=(Button)itemView.findViewById(R.id.btn_no_atendido);
            cantidad_personas=(TextView)itemView.findViewById(R.id.item_cita_pediatra_cantidad_personas);
            img_paciente=(CircleImageView)itemView.findViewById(R.id.img_paciente_cita);
        }

        public void setAsunto(String asunto) {
            this.asunto.setText(asunto);
        }

        public void setNombre_paciente(String nombre_paciente) {
            this.nombre_paciente.setText(nombre_paciente);
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

        public void setCantidad_personas(String cantidad_personas) {
            this.cantidad_personas.setText(cantidad_personas);
        }
    }

}
