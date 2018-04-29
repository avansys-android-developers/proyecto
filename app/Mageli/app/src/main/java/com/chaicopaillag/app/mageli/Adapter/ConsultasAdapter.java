package com.chaicopaillag.app.mageli.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chaicopaillag.app.mageli.R;

public class ConsultasAdapter {
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public TextView asunto,descripcion,respuesta;
        public ImageButton btn_editar,btn_eliminar;
        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view.findViewById(R.id.carview);
            asunto=(TextView)view.findViewById(R.id.item_asunto_consulta);
            descripcion=(TextView)view.findViewById(R.id.item_descripcion_consulta);
            respuesta=(TextView)view.findViewById(R.id.item_respuesta_consulta);
            btn_editar=(ImageButton)view.findViewById(R.id.btn_editar_consulta);
            btn_eliminar=(ImageButton)view.findViewById(R.id.btn_eliminar_consulta);
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
    }
}
