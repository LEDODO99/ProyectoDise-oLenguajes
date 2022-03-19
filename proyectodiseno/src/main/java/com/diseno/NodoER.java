package com.diseno;

import java.util.ArrayList;

public class NodoER {
    private String contenido;
    private ArrayList<Integer> firstPos;
    private ArrayList<Integer> lastPos;
    private ArrayList<Integer> followPos;
    private boolean isLeaf;
    private int[] hijos;

    public NodoER(String contenido, boolean isLeaf){
        this.contenido=contenido;
        firstPos = new ArrayList<>();
        lastPos = new ArrayList<>();
        followPos = new ArrayList<>();
        hijos = new int [2];
        this.isLeaf = isLeaf;
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

    public void setLeftChild(int position){
        hijos[0]=position;
    }
    public void setRightChild(int position){
        hijos[1]=position;
    }

    public int getLeftChild(){
        return hijos[0];
    }
    public int getRightChild(){
        return hijos[1];
    }
    public boolean getIsLeaf(){
        return isLeaf;
    }
    public String getContenido(){
        return contenido;
    }
    public ArrayList<Integer> getFirstPos() {
        return firstPos;
    }
    public ArrayList<Integer> getLastPos() {
        return lastPos;
    }
    public void setFirstPos(ArrayList<Integer> firstPos) {
        this.firstPos = firstPos;
    }
    public void setLastPos(ArrayList<Integer> lastPos) {
        this.lastPos = lastPos;
    }
}
