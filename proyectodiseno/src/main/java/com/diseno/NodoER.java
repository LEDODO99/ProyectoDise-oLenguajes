package com.diseno;

import java.util.ArrayList;

public class NodoER {
    String contenido;
    ArrayList<Integer> firstPos;
    ArrayList<Integer> lastPos;
    ArrayList<Integer> followPos;
    ArrayList<Integer> hijos;

    public NodoER(String contenido){
        this.contenido=contenido;
        firstPos = new ArrayList<>();
        lastPos = new ArrayList<>();
        followPos = new ArrayList<>();
        hijos = new ArrayList<>();
    }

    public void setContenido(String contenido){
        this.contenido=contenido;
    }

    public void addFirstPos(int position){
        firstPos.add(position);
    }

    public void addLastPos(int position){
        lastPos.add(position);
    }

    public void addFollowPos(int position){
        followPos.add(position);
    }

    public void addHijo(int position){
        hijos.add(position);
    }
}
