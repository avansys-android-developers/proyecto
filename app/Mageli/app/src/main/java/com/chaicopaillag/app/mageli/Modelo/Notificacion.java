package com.chaicopaillag.app.mageli.Modelo;

public class Notificacion {
    private  String id;
    private  String titulo;
    private String mensaje;
    private  String fecha;
    private String uid_persona;

    public Notificacion() {
    }

    public Notificacion(String id, String titulo, String mensaje, String fecha, String uid_persona) {
        this.id = id;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.uid_persona = uid_persona;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUid_persona() {
        return uid_persona;
    }

    public void setUid_persona(String uid_persona) {
        this.uid_persona = uid_persona;
    }
}
