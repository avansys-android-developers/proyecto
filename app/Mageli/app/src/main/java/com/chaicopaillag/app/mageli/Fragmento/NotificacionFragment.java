package com.chaicopaillag.app.mageli.Fragmento;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.chaicopaillag.app.mageli.Adapter.NotificacionAdapter;
import com.chaicopaillag.app.mageli.Modelo.Notificacion;
import com.chaicopaillag.app.mageli.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class NotificacionFragment extends Fragment {
    private DatabaseReference firebase_bd;
    private RecyclerView RecicleNotificacion;
    private LinearLayout layoutSinNotify;
    private FirebaseRecyclerAdapter<Notificacion,NotificacionAdapter.ViewHolder> adapter;
    private FirebaseRecyclerOptions<Notificacion> notify_items;
    private ProgressDialog progress_carga;
    private FirebaseAuth auth;
    private FirebaseUser user;
    public NotificacionFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notificacion, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InicializarServicsio();
        InicializarControles();
    }
    private void InicializarServicsio() {
        firebase_bd= FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
    }
    private void InicializarControles() {
        RecicleNotificacion=(RecyclerView)getView().findViewById(R.id.recyclerNotificacion);
        layoutSinNotify=(LinearLayout)getView().findViewById(R.id.layautSinNotificacion);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecicleNotificacion.setLayoutManager(linearLayoutManager);
        progress_cargando_citass();
        Query query=firebase_bd.child("Notificacion").child(user.getUid()).orderByChild(user.getUid()).limitToFirst(100);
        notify_items= new FirebaseRecyclerOptions.Builder<Notificacion>().setQuery(query,Notificacion.class).build();
        adapter= new FirebaseRecyclerAdapter<Notificacion, NotificacionAdapter.ViewHolder>(notify_items) {
            @Override
            protected void onBindViewHolder(@NonNull NotificacionAdapter.ViewHolder holder, int position, @NonNull Notificacion model) {
                holder.setTitutlo(model.getTitulo());
                holder.setMensaje(model.getMensaje());
                holder.setFecha_nofity(model.getFecha());
            }

            @NonNull
            @Override
            public NotificacionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_notificacion, parent, false);
                return new NotificacionAdapter.ViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (adapter.getItemCount()>0){
                    RecicleNotificacion.setVisibility(View.VISIBLE);
                    layoutSinNotify.setVisibility(View.GONE);
                }else {
                    layoutSinNotify.setVisibility(View.VISIBLE);
                    RecicleNotificacion.setVisibility(View.GONE);
                }
                if (progress_carga.isShowing()){
                    progress_carga.dismiss();
                }
            }
        };
        RecicleNotificacion.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    private void progress_cargando_citass() {
        progress_carga=new ProgressDialog(getContext(),R.style.progrescolor);
        progress_carga.setTitle(R.string.app_name);
        progress_carga.setMessage(getString(R.string.cargando_citas));
        progress_carga.setIndeterminate(true);
        progress_carga.setCancelable(false);
        progress_carga.show();
    }

}
