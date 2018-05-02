package com.chaicopaillag.app.mageli.Modelo;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
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
    private String tipo_doc;
    private String fecha_nacimiento;
    private String fecha_registro;
    private boolean estado;
    private int tipo_persona;
    private String especialidad;
    private String foto_url;

    public Persona() {
    }

    public Persona(String id, String nombre, String apellidos, String numero_documento, String numero_hc, String direccion, String telefono, String correo, boolean genero, String tipo_doc, String fecha_nacimiento, String fecha_registro, boolean estado, int tipo_persona, String especialidad, String foto_url) {
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
        this.foto_url = foto_url;
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

    public String getTipo_doc() {
        return tipo_doc;
    }

    public void setTipo_doc(String tipo_doc) {
        this.tipo_doc = tipo_doc;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
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

    public String getFoto_url() {
        return foto_url;
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }

    @Exclude
    public Map<String,Object> miMap(){
        HashMap<String ,Object>Resultado=new HashMap<>();
        Resultado.put("id",id);
        Resultado.put("nombre",nombre);
        Resultado.put("apellidos",apellidos);
        Resultado.put("numero_documento",numero_documento);
        Resultado.put("numero_hc",numero_hc);
        Resultado.put("direccion",direccion);
        Resultado.put("telefono",telefono);
        Resultado.put("correo",correo);
        Resultado.put("genero",genero);
        Resultado.put("tipo_doc",tipo_doc);
        Resultado.put("fecha_nacimiento",fecha_nacimiento);
        Resultado.put("fecha_registro",fecha_registro);
        Resultado.put("estado",estado);
        Resultado.put("tipo_persona",tipo_persona);
        Resultado.put("especialidad",especialidad);
        Resultado.put("foto_url",foto_url);
        return Resultado;
    }
}
