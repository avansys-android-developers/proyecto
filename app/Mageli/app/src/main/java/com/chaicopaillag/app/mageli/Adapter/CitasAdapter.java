package com.chaicopaillag.app.mageli.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chaicopaillag.app.mageli.Modelo.Citas;
import com.chaicopaillag.app.mageli.R;

import java.util.List;

public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.ViewHolder> {
    private List<Citas>MisCitas;
    Context context;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.citas_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.asunto.setText(MisCitas.get(position).getAsunto());
    }

    @Override
    public int getItemCount() {

        return MisCitas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView asunto,descripcion,fecha,hora,estado;
        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view.findViewById(R.id.carview);
            asunto=(TextView)view.findViewById(R.id.item_asunto_cita);
        }
    }
}
//https://medium.com/@beingrahul/firebase-cloud-firestore-v-s-firebase-realtime-database-931d4265d4b0