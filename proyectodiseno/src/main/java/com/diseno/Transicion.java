package com.diseno;

public class Transicion {
    private String parametro;
    private int objetivo;

    public Transicion() {
    }

    public Transicion(String parametro, int objetivo) {
        this.parametro = parametro;
        this.objetivo = objetivo;
    }


    public String getParametro() {
        return this.parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public int getObjetivo() {
        return this.objetivo;
    }

    public void setObjetivo(int objetivo) {
        this.objetivo = objetivo;
    }

}
