package com.chaicopaillag.app.mageli.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chaicopaillag.app.mageli.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConsultaPediatraAdapter {
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public TextView asunto,descripcion,respuesta,nombre_paciente,fecha_consulta,estado_respuesta;
        public Button btn_responder;
        public CircleImageView img_paciente_consulta;
        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view.findViewById(R.id.carview);
            asunto=(TextView)view.findViewById(R.id.item_asunto_consulta_pediatra);
            nombre_paciente=(TextView)view.findViewById(R.id.item_nombre_paciente_consulta);
            descripcion=(TextView)view.findViewById(R.id.item_descripcion_consulta_pediatra);
            fecha_consulta=(TextView)view.findViewById(R.id.item_fecha_consulta_pediatra);
            respuesta=(TextView)view.findViewById(R.id.item_estado_respuesta);
            btn_responder=(Button) view.findViewById(R.id.btn_responder_consulta);
            img_paciente_consulta=(CircleImageView)itemView.findViewById(R.id.img_paciente_consulta);
        }

        public void setAsunto(String asunto) {
            this.asunto.setText(asunto);
        }

        public void setDescripcion(String descripcion) {
            this.descripcion.setText(descripcion);
        }

        public void setRespuesta(String respuesta) {
            this.respuesta.setText(respuesta);
        }

        public void setNombre_paciente(String nombre_paciente) {
            this.nombre_paciente.setText(nombre_paciente);
        }

        public void setFecha_consulta(String fecha_consulta) {
            this.fecha_consulta.setText(fecha_consulta);
        }

        public void setEstado_respuesta(String estado_respuesta) {
            this.estado_respuesta.setText(estado_respuesta);
        }
    }
}
