package com.chaicopaillag.app.mageli.Adapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.chaicopaillag.app.mageli.R;
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
        public TextView dias_restantes;
        public Button btn_atendido,btn_no_atendido;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.carview);
            asunto=(TextView)itemView.findViewById(R.id.item_asunto_cita);
            descripcion=(TextView)itemView.findViewById(R.id.item_descripcion_cita);
            fecha=(TextView)itemView.findViewById(R.id.item_fecha);
            hora=(TextView)itemView.findViewById(R.id.item_hora_cita);
            estado=(TextView)itemView.findViewById(R.id.item_estado_cita);
            nombre_paciente=(TextView)itemView.findViewById(R.id.item_cita_nombre_pediatra);
            btn_atendido=(Button)itemView.findViewById(R.id.btn_eliminar_cita);
            btn_no_atendido=(Button)itemView.findViewById(R.id.btn_cancelar_cita);
            cantidad_personas=(TextView)itemView.findViewById(R.id.item_cita_pediatra_cantidad_personas);
            dias_restantes=(TextView)itemView.findViewById(R.id.item_dias_restante);
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

        public void setDias_restantes(String dias_restantes) {
            this.dias_restantes.setText(dias_restantes);
        }
    }

}
