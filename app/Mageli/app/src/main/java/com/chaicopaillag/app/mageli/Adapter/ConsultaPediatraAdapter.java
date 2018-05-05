package com.chaicopaillag.app.mageli.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chaicopaillag.app.mageli.R;

public class ConsultaPediatraAdapter {
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public TextView asunto,descripcion,respuesta,nombre_paciente,fecha_consulta,estado_respuesta;
        public Button btn_responder;
        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view.findViewById(R.id.carview);
            asunto=(TextView)view.findViewById(R.id.item_asunto_consulta);
            nombre_paciente=(TextView)view.findViewById(R.id.item_nombre_pediatra_consulta);
            descripcion=(TextView)view.findViewById(R.id.item_descripcion_consulta);
            fecha_consulta=(TextView)view.findViewById(R.id.item_fecha_consulta);
            respuesta=(TextView)view.findViewById(R.id.item_respuesta_consulta_contador);
            btn_responder=(Button) view.findViewById(R.id.btn_editar_consulta);
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
