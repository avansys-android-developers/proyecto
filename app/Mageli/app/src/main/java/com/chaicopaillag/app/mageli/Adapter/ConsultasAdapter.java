package com.chaicopaillag.app.mageli.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chaicopaillag.app.mageli.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConsultasAdapter {
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public TextView asunto,descripcion,respuesta,nombre_pediatra,fecha_consulta;
        public ImageButton btn_respuesta;
        public Button btn_editar,btn_eliminar;
        public CircleImageView img_pediatra;
        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view.findViewById(R.id.carview);
            asunto=(TextView)view.findViewById(R.id.item_asunto_consulta);
            nombre_pediatra=(TextView)view.findViewById(R.id.item_nombre_pediatra_consulta);
            descripcion=(TextView)view.findViewById(R.id.item_descripcion_consulta);
            fecha_consulta=(TextView)view.findViewById(R.id.item_fecha_consulta);
            respuesta=(TextView)view.findViewById(R.id.item_respuesta_consulta_contador);
            btn_editar=(Button) view.findViewById(R.id.btn_editar_consulta);
            btn_eliminar=(Button) view.findViewById(R.id.btn_eliminar_consulta);
            btn_respuesta=(ImageButton)view.findViewById(R.id.btn_respuesta_consulta);
            img_pediatra=(CircleImageView)itemView.findViewById(R.id.img_pediatra_consulta);
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
        public void setNombre_pediatra(String nombre_pediatra) {
            this.nombre_pediatra.setText( nombre_pediatra);
        }
        public void setFecha_consulta(String fecha_consulta) {
            this.fecha_consulta.setText(fecha_consulta);
        }

    }
}
