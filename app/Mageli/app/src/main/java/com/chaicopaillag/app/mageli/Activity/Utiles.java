package com.chaicopaillag.app.mageli.Activity;

import java.text.SimpleDateFormat;

public  class Utiles {
    public static boolean validarFecha(String fecha) {

        try {

            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

            formatoFecha.setLenient(false);

            formatoFecha.parse(fecha);

        } catch (Exception e) {

            return false;

        }
        return true;

    }
    public static boolean validarHora(String hora) {

        try {

            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

            formatoHora.setLenient(false);

            formatoHora.parse(hora);

        } catch (Exception e) {

            return false;

        }
        return true;

    }
    public static boolean validarNumero(String numero){
        boolean n_correcto=false;
        int MiNumero;
        try {
            MiNumero=Integer.parseInt(numero);
            n_correcto=true;
        }catch (Exception e){
            n_correcto=false;
        }
        return n_correcto;
    }
}
