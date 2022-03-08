package com.diseno;

import java.util.ArrayList;

public class NodoGrafo {
    private boolean isFinal;
    private ArrayList<Transicion> transiciones;
    private int numTransiciones=0;

    public NodoGrafo() {
        isFinal=false;
        transiciones = new ArrayList<>();
        numTransiciones=0;
    }

    public NodoGrafo(boolean isFinal, ArrayList<Transicion> transiciones) {
        this.isFinal = isFinal;
        this.transiciones = transiciones;
        numTransiciones=transiciones.size();
    }

    public boolean isIsFinal() {
        return this.isFinal;
    }

    public boolean getIsFinal() {
        return this.isFinal;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public ArrayList<Transicion> getTransiciones() {
        return this.transiciones;
    }

    public void setTransiciones(ArrayList<Transicion> transiciones) {
        this.transiciones = transiciones;
    }

    public int getNumTransiciones() {
        return this.numTransiciones;
    }

    public void setNumTransiciones(int numTransiciones) {
        this.numTransiciones = numTransiciones;
    }

    public void addTransicion(int nodo, String parametro){
        transiciones.add( new Transicion(parametro, nodo) );
        numTransiciones++;
    }
}
