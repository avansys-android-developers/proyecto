package com.chaicopaillag.app.mageli.modelo;

import java.util.Date;

public class  Persona {
    private String id;
    private String nombre;
    private String apellidos;
    private String numero_documento;
    private String numero_hc;
    private String direccion;
    private String telefono;
    private String correo;
    private boolean genero;
    private int tipo_doc;
    private Date fecha_nacimiento;
    private Date fecha_registro;
    private boolean estado;
    private int tipo_persona;
    private String especialidad;

    public Persona() {
        super();
    }

    public Persona(String id, String nombre, String apellidos, String numero_documento, String numero_hc, String direccion, String telefono, String correo, boolean genero, int tipo_doc, Date fecha_nacimiento, Date fecha_registro, boolean estado, int tipo_persona, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.numero_documento = numero_documento;
        this.numero_hc = numero_hc;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.genero = genero;
        this.tipo_doc = tipo_doc;
        this.fecha_nacimiento = fecha_nacimiento;
        this.fecha_registro = fecha_registro;
        this.estado = estado;
        this.tipo_persona = tipo_persona;
        this.especialidad = especialidad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNumero_documento() {
        return numero_documento;
    }

    public void setNumero_documento(String numero_documento) {
        this.numero_documento = numero_documento;
    }

    public String getNumero_hc() {
        return numero_hc;
    }

    public void setNumero_hc(String numero_hc) {
        this.numero_hc = numero_hc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isGenero() {
        return genero;
    }

    public void setGenero(boolean genero) {
        this.genero = genero;
    }

    public int getTipo_doc() {
        return tipo_doc;
    }

    public void setTipo_doc(int tipo_doc) {
        this.tipo_doc = tipo_doc;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getTipo_persona() {
        return tipo_persona;
    }

    public void setTipo_persona(int tipo_persona) {
        this.tipo_persona = tipo_persona;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
