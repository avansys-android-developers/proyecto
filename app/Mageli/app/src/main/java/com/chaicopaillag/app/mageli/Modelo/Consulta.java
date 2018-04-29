package com.chaicopaillag.app.mageli.Modelo;

public class Consulta {
    private String id;
    private String asunto;
    private String descripcion;
    private String uid_paciente;
    private String uid_pediatra;
    private String fecha_registro;
    private boolean flag_respuesta;
    private boolean flag_privacidad;
    private boolean estado;

    public Consulta() {
    }

    public Consulta(String id, String asunto, String descripcion, String uid_paciente, String uid_pediatra, String fecha_registro, boolean flag_respuesta, boolean flag_privacidad, boolean estado) {
        this.id = id;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.uid_paciente = uid_paciente;
        this.uid_pediatra = uid_pediatra;
        this.fecha_registro = fecha_registro;
        this.flag_respuesta = flag_respuesta;
        this.flag_privacidad = flag_privacidad;
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

    public String getUid_paciente() {
        return uid_paciente;
    }

    public void setUid_paciente(String uid_paciente) {
        this.uid_paciente = uid_paciente;
    }

    public String getUid_pediatra() {
        return uid_pediatra;
    }

    public void setUid_pediatra(String uid_pediatra) {
        this.uid_pediatra = uid_pediatra;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public boolean isFlag_respuesta() {
        return flag_respuesta;
    }

    public void setFlag_respuesta(boolean flag_respuesta) {
        this.flag_respuesta = flag_respuesta;
    }

    public boolean isFlag_privacidad() {
        return flag_privacidad;
    }

    public void setFlag_privacidad(boolean flag_privacidad) {
        this.flag_privacidad = flag_privacidad;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
