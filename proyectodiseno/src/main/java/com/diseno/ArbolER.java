package com.diseno;

import java.util.ArrayList;

public class ArbolER {
    private ArrayList<NodoER> nodos;
    private ArrayList<Integer> leaves;
    private ArrayList<String> leafContents;
    private ArrayList<ArrayList<Integer>>leafFollopos;
    
    public ArbolER(){
        nodos = new ArrayList<>();
        leafContents = new ArrayList<>();
        leaves = new ArrayList<>();
        leafFollopos = new ArrayList<>();
    }

    public void addNodo(String content, boolean leaf){
        NodoER nodoNew = new NodoER(content, leaf);
        if (leaf){
            nodoNew.addFirstPos(nodos.size());
            nodoNew.addLastPos(nodos.size());
            leaves.add(nodos.size());
            leafContents.add(content);
        }
        nodos.add(nodoNew);
    }

    public ArrayList<NodoER> getNodos(){
        return this.nodos;
    }

    public void addLeftChild(int parent, int child){
        nodos.get(parent).setLeftChild(child);
    }
    public void addRightChild(int parent, int child){
        nodos.get(parent).setRightChild(child);
    }
    public int getCantidadNodos(){
        return nodos.size();
    }

    public ArrayList<Integer> getLeaves(){
        return leaves;
    }

    public ArrayList<String> getLeafContents(){
        return leafContents;
    }

    public ArrayList<ArrayList<Integer>> getFolloposes(){
        return leafFollopos;
    }
    
    public void calculateFollowPos(){
        for (int i = 0; i<leaves.size() ; i++){
            leafFollopos.add(new ArrayList<>());
        }
        for (int i = 0; i<nodos.size(); i++){
            if (!nodos.get(i).getIsLeaf()){
                if (nodos.get(i).getContenido().equals("concat")){
                    ArrayList<Integer> positions1 = nodos.get(nodos.get(i).getLeftChild()).getLastPos();
                    ArrayList<Integer> positions2 = nodos.get(nodos.get(i).getRightChild()).getFirstPos();
                    for (int j=0; j<positions1.size(); j++){
                        for (int k=0; k<positions2.size(); k++){
                            if(!leafFollopos.get((leaves.indexOf(positions1.get(j)))).contains(positions2.get(k))){
                                leafFollopos.get((leaves.indexOf(positions1.get(j)))).add(positions2.get(k));
                            }
                        }
                    }
                }else if (nodos.get(i).getContenido().equals("*")){
                    ArrayList<Integer> positions1 = nodos.get(nodos.get(i).getLeftChild()).getLastPos();
                    ArrayList<Integer> positions2 = nodos.get(nodos.get(i).getLeftChild()).getFirstPos();
                    for (int j=0; j<positions1.size(); j++){
                        for (int k=0; k<positions2.size(); k++){
                            if(!leafFollopos.get((leaves.indexOf(positions1.get(j)))).contains(positions2.get(k))){
                                leafFollopos.get((leaves.indexOf(positions1.get(j)))).add(positions2.get(k));
                            }
                        }
                    }
                }else if (nodos.get(i).getContenido().equals("+")){
                    ArrayList<Integer> positions1 = nodos.get(nodos.get(i).getLeftChild()).getLastPos();
                    ArrayList<Integer> positions2 = nodos.get(nodos.get(i).getLeftChild()).getFirstPos();
                    for (int j=0; j<positions1.size(); j++){
                        for (int k=0; k<positions2.size(); k++){
                            if(!leafFollopos.get((leaves.indexOf(positions1.get(j)))).contains(positions2.get(k))){
                                leafFollopos.get((leaves.indexOf(positions1.get(j)))).add(positions2.get(k));
                            }
                        }
                    }
                }
            }
        }
    }

    public void calculateNullableFirstPosLastPos(){
        for (int i=0; i<nodos.size();i++){
            recursiveCalFirstPos(i);
            recursiveCalLastPos(i);
        }
    }
    private ArrayList<Integer> recursiveCalLastPos(int nodo){
        ArrayList<Integer> returnableList = new ArrayList<>();
        if (nodos.get(nodo).getIsLeaf()){
            returnableList = nodos.get(nodo).getLastPos();
        }else if(nodos.get(nodo).getContenido().equals("*")){
            returnableList = recursiveCalLastPos(nodos.get(nodo).getLeftChild());
        }else if(nodos.get(nodo).getContenido().equals("?")){
            returnableList = recursiveCalLastPos(nodos.get(nodo).getLeftChild());
        }else if (nodos.get(nodo).getContenido().equals("+")){
            returnableList = recursiveCalLastPos(nodos.get(nodo).getLeftChild());
        }else if (nodos.get(nodo).getContenido().equals("|")){
            ArrayList<Integer> arTempo = new ArrayList<>();
            arTempo=recursiveCalLastPos(nodos.get(nodo).getLeftChild());
            for (int i=0; i<arTempo.size(); i++){
                if(!returnableList.contains(arTempo.get(i))){
                    returnableList.add(arTempo.get(i));
                }
            }
            arTempo=recursiveCalLastPos(nodos.get(nodo).getRightChild());
            for (int i=0; i<arTempo.size(); i++){
                if(!returnableList.contains(arTempo.get(i))){
                    returnableList.add(arTempo.get(i));
                }
            }
        }else if (nodos.get(nodo).getContenido().equals("concat")){
            ArrayList<Integer> arTempo = new ArrayList<>();
            if(recursiveGetIsNullable(nodos.get(nodo).getRightChild())){
                arTempo=recursiveCalLastPos(nodos.get(nodo).getLeftChild());
                for (int i=0; i<arTempo.size(); i++){
                    if(!returnableList.contains(arTempo.get(i))){
                        returnableList.add(arTempo.get(i));
                    }
                }
                arTempo=recursiveCalLastPos(nodos.get(nodo).getRightChild());
                for (int i=0; i<arTempo.size(); i++){
                    if(!returnableList.contains(arTempo.get(i))){
                        returnableList.add(arTempo.get(i));
                    }
                }
            }else{
                arTempo=recursiveCalLastPos(nodos.get(nodo).getRightChild());
                for (int i=0; i<arTempo.size(); i++){
                    if(!returnableList.contains(arTempo.get(i))){
                        returnableList.add(arTempo.get(i));
                    }
                }
            }
        }
        nodos.get(nodo).setLastPos(returnableList);
        return returnableList;
    }
    private ArrayList<Integer> recursiveCalFirstPos(int nodo){
        ArrayList<Integer> returnableList = new ArrayList<>();
        if (nodos.get(nodo).getIsLeaf()){
            returnableList = nodos.get(nodo).getFirstPos();
        }else if(nodos.get(nodo).getContenido().equals("*")){
            returnableList = recursiveCalFirstPos(nodos.get(nodo).getLeftChild());
        }else if(nodos.get(nodo).getContenido().equals("?")){
            returnableList = recursiveCalFirstPos(nodos.get(nodo).getLeftChild());
        }else if (nodos.get(nodo).getContenido().equals("+")){
            returnableList = recursiveCalFirstPos(nodos.get(nodo).getLeftChild());
        }else if (nodos.get(nodo).getContenido().equals("|")){
            ArrayList<Integer> arTempo = new ArrayList<>();
            arTempo=recursiveCalFirstPos(nodos.get(nodo).getLeftChild());
            for (int i=0; i<arTempo.size(); i++){
                if(!returnableList.contains(arTempo.get(i))){
                    returnableList.add(arTempo.get(i));
                }
            }
            arTempo=recursiveCalFirstPos(nodos.get(nodo).getRightChild());
            for (int i=0; i<arTempo.size(); i++){
                if(!returnableList.contains(arTempo.get(i))){
                    returnableList.add(arTempo.get(i));
                }
            }
        }else if (nodos.get(nodo).getContenido().equals("concat")){
            ArrayList<Integer> arTempo = new ArrayList<>();
            if(recursiveGetIsNullable(nodos.get(nodo).getLeftChild())){
                arTempo=recursiveCalFirstPos(nodos.get(nodo).getLeftChild());
                for (int i=0; i<arTempo.size(); i++){
                    if(!returnableList.contains(arTempo.get(i))){
                        returnableList.add(arTempo.get(i));
                    }
                }
                arTempo=recursiveCalFirstPos(nodos.get(nodo).getRightChild());
                for (int i=0; i<arTempo.size(); i++){
                    if(!returnableList.contains(arTempo.get(i))){
                        returnableList.add(arTempo.get(i));
                    }
                }
            }else{
                arTempo=recursiveCalFirstPos(nodos.get(nodo).getLeftChild());
                for (int i=0; i<arTempo.size(); i++){
                    if(!returnableList.contains(arTempo.get(i))){
                        returnableList.add(arTempo.get(i));
                    }
                }
            }
        }
        nodos.get(nodo).setFirstPos(returnableList);
        return returnableList;
    }
    private boolean recursiveGetIsNullable (int nodo){
        if (nodos.get(nodo).getIsLeaf())
            return false;
        else if(nodos.get(nodo).getContenido().equals("*")){
            return true;
        }else if(nodos.get(nodo).getContenido().equals("?")){
            return true;
        }else if (nodos.get(nodo).getContenido().equals("+")){
            return recursiveGetIsNullable(nodos.get(nodo).getLeftChild());
        }else if (nodos.get(nodo).getContenido().equals("|")){
            return (recursiveGetIsNullable(nodos.get(nodo).getLeftChild())||recursiveGetIsNullable(nodos.get(nodo).getRightChild()));
        }else if (nodos.get(nodo).getContenido().equals("concat")){
            return (recursiveGetIsNullable(nodos.get(nodo).getLeftChild())&&recursiveGetIsNullable(nodos.get(nodo).getRightChild()));
        }else{
            return true;
        }
    }
    public NodoER getNodoIn(int pos){
        return nodos.get(pos);
    }
}
