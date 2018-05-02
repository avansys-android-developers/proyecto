package com.chaicopaillag.app.mageli.Modelo;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Citas {
    private String id;
    private String asunto;
    private String descripcion;
    private String fecha;
    private String hora;
    private int cantidad_personas;
    private String uid_paciente;
    private String nombre_paciente;
    private String uid_pediatra;
    private String nombre_pediatra;
    private String fecha_registro;
    private boolean flag_atendido;
    private boolean flag_cancelado;
    private boolean flag_postergado;
    private boolean estado;

    public Citas() {
    }

    public Citas(String id, String asunto, String descripcion, String fecha, String hora, int cantidad_personas, String uid_paciente, String nombre_paciente, String uid_pediatra, String nombre_pediatra, String fecha_registro, boolean flag_atendido, boolean flag_cancelado, boolean flag_postergado, boolean estado) {
        this.id = id;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.cantidad_personas = cantidad_personas;
        this.uid_paciente = uid_paciente;
        this.nombre_paciente = nombre_paciente;
        this.uid_pediatra = uid_pediatra;
        this.nombre_pediatra = nombre_pediatra;
        this.fecha_registro = fecha_registro;
        this.flag_atendido = flag_atendido;
        this.flag_cancelado = flag_cancelado;
        this.flag_postergado = flag_postergado;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getCantidad_personas() {
        return cantidad_personas;
    }

    public void setCantidad_personas(int cantidad_personas) {
        this.cantidad_personas = cantidad_personas;
    }

    public String getUid_paciente() {
        return uid_paciente;
    }

    public void setUid_paciente(String uid_paciente) {
        this.uid_paciente = uid_paciente;
    }

    public String getNombre_paciente() {
        return nombre_paciente;
    }

    public void setNombre_paciente(String nombre_paciente) {
        this.nombre_paciente = nombre_paciente;
    }

    public String getUid_pediatra() {
        return uid_pediatra;
    }

    public void setUid_pediatra(String uid_pediatra) {
        this.uid_pediatra = uid_pediatra;
    }

    public String getNombre_pediatra() {
        return nombre_pediatra;
    }

    public void setNombre_pediatra(String nombre_pediatra) {
        this.nombre_pediatra = nombre_pediatra;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public boolean isFlag_atendido() {
        return flag_atendido;
    }

    public void setFlag_atendido(boolean flag_atendido) {
        this.flag_atendido = flag_atendido;
    }

    public boolean isFlag_cancelado() {
        return flag_cancelado;
    }

    public void setFlag_cancelado(boolean flag_cancelado) {
        this.flag_cancelado = flag_cancelado;
    }

    public boolean isFlag_postergado() {
        return flag_postergado;
    }

    public void setFlag_postergado(boolean flag_postergado) {
        this.flag_postergado = flag_postergado;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    @Exclude
    public Map<String,Object> miMap(){
        HashMap<String ,Object> Resultado=new HashMap<>();
        Resultado.put("id",id);
        Resultado.put("asunto",asunto);
        Resultado.put("descripcion",descripcion);
        Resultado.put("fecha",fecha);
        Resultado.put("hora",hora);
        Resultado.put("cantidad_personas",cantidad_personas);
        Resultado.put("uid_paciente",uid_paciente);
        Resultado.put("nombre_paciente",nombre_paciente);
        Resultado.put("uid_pediatra",uid_pediatra);
        Resultado.put("nombre_pediatra",nombre_pediatra);
        Resultado.put("fecha_registro",fecha_registro);
        Resultado.put("flag_atendido",flag_atendido);
        Resultado.put("flag_cancelado",flag_cancelado);
        Resultado.put("flag_postergado",flag_postergado);
        Resultado.put("estado",estado);
        return Resultado;
    }
}
