package com.diseno;

import java.util.ArrayList;

public class ArbolER {
    private ArrayList<NodoER> nodos;
    
    public ArbolER(){
        nodos = new ArrayList<>();
    }

    public void addNodo(String content, boolean leaf){
        NodoER nodoNew = new NodoER(content);
        if (leaf){
            nodoNew.addFirstPos(nodos.size());
            nodoNew.addFollowPos(nodos.size());
        }
        nodos.add(nodoNew);
    }

    public ArrayList<NodoER> getNodos(){
        return this.nodos;
    }

    public void addChild (int nodoBase, int nodoHijo){
        nodos.get(nodoBase).addHijo(nodoHijo);
    }
}
