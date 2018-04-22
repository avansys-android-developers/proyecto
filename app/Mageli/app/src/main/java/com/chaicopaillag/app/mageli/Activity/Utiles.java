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
}
