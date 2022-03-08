package com.diseno;

import java.util.ArrayList;

public class Grafo {
    private ArrayList<NodoGrafo> nodos;

    public Grafo() {
        nodos = new ArrayList<>();
        nodos.add(new NodoGrafo());
    }
    
    public void addNode(int nodoFuente, String parametro){
        nodos.get(nodoFuente).addTransicion(nodos.size(), parametro);
        nodos.add(new NodoGrafo());
    }

    public NodoGrafo getNodo(int numeroNodo){
        return nodos.get(numeroNodo);
    }
    
    public int getCantidadNodos (){
        return nodos.size();
    }

    public void addTransicion(int nodoFuente, int nodoDestino, String parametro){
        nodos.get(nodoFuente).addTransicion(nodoDestino, parametro);
    }
}
