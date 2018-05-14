package com.chaicopaillag.app.mageli.Modelo;

public class RespuestaConsulta {
    private  String id;
    private String descripcion;
    private String fecha;

    public RespuestaConsulta() {
    }

    public RespuestaConsulta(String id, String descripcion, String fecha) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
