package com.diseno;

import java.util.ArrayList;

public class Lector{
    private final String EPSILON = "epsilon";
    private Grafo noDeterminista;
    private boolean isfirst=true;
    private int parenthesisDepth=0;
    private ArrayList<Integer> firstNodes;
    private ArrayList<Integer> lastNodes;
    private ArrayList<String> symbols;
    private ArrayList<Boolean> orDepth;

    private ArbolER arbolER;
    private ArrayList<Integer>raices;
    private ArrayList<Boolean>parenthesisIsLeft;
    private ArrayList<Boolean>parenthesHasOr;

    private Grafo subConjuntos;
    private Grafo afdDirecto;
    
    public Lector() {
        noDeterminista = new Grafo();
        subConjuntos = new Grafo();
        afdDirecto = new Grafo();
    }

    public Grafo getNFA(){
        return noDeterminista;
    }

    public Grafo getSubconjuntos(){
        return subConjuntos;
    }

    public Grafo getAfdDirecto(){
        return afdDirecto;
    }

    private void reiniciarVariablesThomson(){
        firstNodes=new ArrayList<>();
        lastNodes=new ArrayList<>();
        orDepth=new ArrayList<>();
        noDeterminista = new Grafo();
        subConjuntos = new Grafo();
        isfirst=true;
        parenthesisDepth=0;
        firstNodes.add(0);
        lastNodes.add(0);
        orDepth.add(false);
        symbols = new ArrayList<>();
    }
    private void reiniciarVariablesDirect(){
        afdDirecto = new Grafo();
        arbolER = new ArbolER();
        raices = new ArrayList<>();
        raices.add(0);
        parenthesisIsLeft=new ArrayList<>();
        parenthesHasOr = new ArrayList<>();
        parenthesHasOr.add(false);
        isfirst=true;
    }
    public boolean generarThomson(String expresionReg){
        reiniciarVariablesThomson();
        try{
        algoritmoThomson(expresionReg);
        return true;
        }catch(Exception e){
            return false;
        }
    }
    public boolean generarSubConjuntos(){
        try{
        algoritmoSubconjuntos();
        return true;
        }catch(Exception e){
            return false;
        }
    }
    public boolean generarDirecto(String expresionReg){
        reiniciarVariablesDirect();
        try{
        algoritmoDirecto(expresionReg);
        directSubconj();
        return true;
        }catch(Exception e){
            return false;
        }
    }
    public boolean validarCadenaSub (String expresionEva){
        return validarSubConjuntos(expresionEva);
    }
    public boolean validarCadenaDirect (String expresionEva){
        return validarAFDDirecto(expresionEva);
    }
    public boolean validarCadenaThomson (String expresionEva){
        return validarAFNTHomson(expresionEva);
    }

    private boolean validarAFNTHomson(String expresionEva){
        ArrayList<Integer> cadenaAgregar = new ArrayList<>();
        cadenaAgregar.add(0);
        cadenaAgregar=epsilonChain(cadenaAgregar);
        for (int i=0; i<expresionEva.length(); i++){
            cadenaAgregar=epsilonChain(characterChain(cadenaAgregar,Character.toString(expresionEva.charAt(i))));
        }
        return checkIfEnd(cadenaAgregar);
    }

    private void algoritmoDirecto(String expresionReg){
        for (int i=0; i<expresionReg.length();i++){
            char caracterDeExp = expresionReg.charAt(i);
            if (caracterDeExp=='('){
                directOpenPar();
            }else if(caracterDeExp==')'){
                try{
                    directClosePar(expresionReg.charAt(i+1));
                }catch(Exception e){
                    directClosePar('¿');
                }
            }else if(caracterDeExp=='|'){
                directOr();
            }else if((caracterDeExp=='*')||(caracterDeExp=='+')||(caracterDeExp=='?')){

            }
            else{
                try{
                    directChar(Character.toString(caracterDeExp), expresionReg.charAt(i+1));
                }catch (Exception e){
                    directChar(Character.toString(caracterDeExp), '¿');
                }
            }
        }
        if (parenthesHasOr.get(0)){
            arbolER.addRightChild(raices.get(raices.size()-2), raices.get(raices.size()-1));
            raices.remove(raices.size()-1);
        }
        arbolER.addNodo("#", true);
        arbolER.addNodo("concat", false);
        arbolER.addRightChild(arbolER.getCantidadNodos()-1, arbolER.getCantidadNodos()-2);
        arbolER.addLeftChild(arbolER.getCantidadNodos()-1, raices.get(raices.size()-1));
        raices.set(raices.size()-1, arbolER.getCantidadNodos()-1);
    }
    private void directOpenPar(){
        parenthesHasOr.add(false);
        if (isfirst){
            parenthesisIsLeft.add(true);
        }else{
            parenthesisIsLeft.add(false);
            isfirst=true;
            arbolER.addNodo("concat", false);
            arbolER.addLeftChild(arbolER.getCantidadNodos()-1, raices.get(raices.size()-1));
            raices.set(raices.size()-1, arbolER.getCantidadNodos()-1);
        }
    }
    private void directClosePar(char simboloSiguiente){
        if (parenthesHasOr.get(parenthesHasOr.size()-1)){
            arbolER.addRightChild(raices.get(raices.size()-2), raices.get(raices.size()-1));
            raices.remove(raices.size()-1);
        }
        if(simboloSiguiente=='*'||simboloSiguiente=='?'||simboloSiguiente=='+'){
            arbolER.addNodo(Character.toString(simboloSiguiente), false);
            arbolER.addLeftChild(arbolER.getCantidadNodos()-1, raices.get(raices.size()-1));
            raices.set(raices.size()-1, arbolER.getCantidadNodos()-1);
        }
        if(!parenthesisIsLeft.get(parenthesisIsLeft.size()-1)){
            arbolER.addRightChild(raices.get(raices.size()-2), raices.get(raices.size()-1));
            raices.remove(raices.size()-1);
        }
        parenthesisIsLeft.remove(parenthesisIsLeft.size()-1);
        parenthesHasOr.remove(parenthesHasOr.size()-1);
    }
    private void directOr(){
        arbolER.addNodo("|", false);
        arbolER.addLeftChild(arbolER.getCantidadNodos()-1, raices.get(raices.size()-1));
        parenthesHasOr.set(parenthesHasOr.size()-1, true);
        raices.set(raices.size()-1, arbolER.getCantidadNodos()-1);
        isfirst=true;
    }
    private void directChar(String simbolo, char simboloSiguiente){
        if (isfirst){
            isfirst=false;
            raices.add(arbolER.getCantidadNodos());
            if(simboloSiguiente=='*'||simboloSiguiente=='?'||simboloSiguiente=='+'){
                arbolER.addNodo(simbolo, true);
                arbolER.addNodo(Character.toString(simboloSiguiente), false);
                arbolER.addLeftChild(arbolER.getCantidadNodos()-1, arbolER.getCantidadNodos()-2);
            }else{
                arbolER.addNodo(simbolo, true);
            }
        }else{
            if(simboloSiguiente=='*'||simboloSiguiente=='?'||simboloSiguiente=='+'){
                arbolER.addNodo(simbolo, true);
                arbolER.addNodo(Character.toString(simboloSiguiente), false);
                arbolER.addLeftChild(arbolER.getCantidadNodos()-1, arbolER.getCantidadNodos()-2);
            }else{
                arbolER.addNodo(simbolo, true);
            }
            arbolER.addNodo("concat", false);
            arbolER.addRightChild(arbolER.getCantidadNodos()-1, arbolER.getCantidadNodos()-2);
            arbolER.addLeftChild(arbolER.getCantidadNodos()-1, raices.get(raices.size()-1));
            raices.set(raices.size()-1, arbolER.getCantidadNodos()-1);
        }
    }
    private void directSubconj(){
        arbolER.calculateNullableFirstPosLastPos();
        arbolER.calculateFollowPos();
        ArrayList<ArrayList<Integer>> subconjuntos = new ArrayList<>();
        ArrayList<Integer> cadenaAgregar = arbolER.getNodos().get(raices.get(raices.size()-1)).getFirstPos();
        afdDirecto.getNodo(afdDirecto.getCantidadNodos()-1).setIsFinal(chechIfDirectEnd(cadenaAgregar));
        subconjuntos.add(cadenaAgregar);
        for (int i=0; i<subconjuntos.size();i++){
            for (int j=0; j<symbols.size(); j++){

                cadenaAgregar=obtenerFollowposses(subconjuntos.get(i), symbols.get(j));
                if (cadenaAgregar.size()>0){
                    int checkIn = checkIfChainBefore(subconjuntos, cadenaAgregar);
                    if (checkIn!=-1){
                        afdDirecto.addTransicion(i, checkIn, symbols.get(j));
                    }else{
                        subconjuntos.add(cadenaAgregar);
                        afdDirecto.addNode(i, symbols.get(j));
                        afdDirecto.getNodo(afdDirecto.getCantidadNodos()-1).setIsFinal(chechIfDirectEnd(cadenaAgregar));;
                    }
                }
            }
        }
    }
    private boolean chechIfDirectEnd (ArrayList<Integer> cadenaAgregar){
        return cadenaAgregar.contains(arbolER.getLeaves().get(arbolER.getLeaves().size()-1));
    }
    private ArrayList<Integer> obtenerFollowposses (ArrayList<Integer> lista,String simbolo){
        ArrayList<Integer> estados = new ArrayList<>();
        arbolER.getLeaves();
        arbolER.getLeafContents();
        arbolER.getFolloposes();
        for (int i=0; i<lista.size(); i++){
            if (arbolER.getLeafContents().get(arbolER.getLeaves().indexOf(lista.get(i))).equals(simbolo)){
                for (int j = 0; j < arbolER.getFolloposes().get(arbolER.getLeaves().indexOf(lista.get(i))).size();j++){
                    int objetivo = arbolER.getFolloposes().get(arbolER.getLeaves().indexOf(lista.get(i))).get(j);
                    if(!estados.contains(objetivo)){
                        estados.add(objetivo);
                    }
                }
            }
        }
        return estados;
    }


    private void algoritmoSubconjuntos(){
        ArrayList<ArrayList<Integer>> subconjuntos = new ArrayList<>();
        ArrayList<Integer> cadenaAgregar = new ArrayList<>();
        cadenaAgregar.add(0);
        cadenaAgregar=epsilonChain(cadenaAgregar);
        subConjuntos.getNodo(subConjuntos.getCantidadNodos()-1).setIsFinal(checkIfEnd(cadenaAgregar));
        subconjuntos.add(cadenaAgregar);
        for (int i=0; i<subconjuntos.size();i++){
            for (int j=0; j<symbols.size(); j++){
                cadenaAgregar=epsilonChain(characterChain(subconjuntos.get(i), symbols.get(j)));
                if (cadenaAgregar.size()>0){
                    int checkIn = checkIfChainBefore(subconjuntos, cadenaAgregar);
                    if (checkIn!=-1){
                        subConjuntos.addTransicion(i, checkIn, symbols.get(j));
                    }else{
                        subconjuntos.add(cadenaAgregar);
                        subConjuntos.addNode(i, symbols.get(j));
                        subConjuntos.getNodo(subConjuntos.getCantidadNodos()-1).setIsFinal(checkIfEnd(cadenaAgregar));;
                    }
                }
            }
        }
    }
    private boolean checkIfEnd(ArrayList<Integer> cadenaAgregar){
        for (int i=0; i<cadenaAgregar.size();i++){
            if(noDeterminista.getNodo(cadenaAgregar.get(i)).isIsFinal())
                return true;
        }
        return false;
    }
    private int checkIfChainBefore(ArrayList<ArrayList<Integer>> subconjuntos, ArrayList<Integer> cadenaAgregar){
        boolean isIn=true;
        for (int i=0; i<subconjuntos.size();i++){
            isIn=true;
            for (int j=0; j<cadenaAgregar.size();j++){
                if(!subconjuntos.get(i).contains(cadenaAgregar.get(j))){
                    isIn=false;
                }
            }
            if(isIn){
                return i;
            }
        }
        return -1;
    }
    private ArrayList<Integer> characterChain(ArrayList<Integer> lista, String character){
        ArrayList<Integer> estados = new ArrayList<>();
        for (int i=0; i<lista.size();i++){
            for (int j=0; j<noDeterminista.getNodo(lista.get(i)).getTransiciones().size();j++){
                if (noDeterminista.getNodo(lista.get(i)).getTransiciones().get(j).getParametro().equals(character)){
                    int objetivo= noDeterminista.getNodo(lista.get(i)).getTransiciones().get(j).getObjetivo();
                    if(!estados.contains(objetivo)){
                        estados.add(objetivo);
                    }
                }
            }
        }
        return estados;
    }
    private ArrayList<Integer> epsilonChain(ArrayList<Integer> lista){
        ArrayList<Integer> estados = new ArrayList<>();
        for (int i=0; i<lista.size();i++){
            if(!estados.contains(lista.get(i))){
                estados.add(lista.get(i));
            }
            for (int j=0; j<noDeterminista.getNodo(lista.get(i)).getTransiciones().size();j++){
                if (noDeterminista.getNodo(lista.get(i)).getTransiciones().get(j).getParametro().equals(EPSILON)){
                    int objetivo= noDeterminista.getNodo(lista.get(i)).getTransiciones().get(j).getObjetivo();
                    if(!estados.contains(objetivo)){
                        lista.add(objetivo);
                        estados.add(objetivo);
                    }
                }
            }
        }
        return estados;
    }



    private void algoritmoThomson(String expresionReg){
        for (int i=0; i<expresionReg.length();i++){
            if (expresionReg.charAt(i)=='('){
                thomsonOpenPar();
            }else if(expresionReg.charAt(i)==')'){
                thomsonClosePar();
            }else if(expresionReg.charAt(i)=='*'){
                thomsonStar();
            }else if(expresionReg.charAt(i)=='+'){
                thomsonPlus();
            }else if(expresionReg.charAt(i)=='?'){
                thomsonQuestion();
            }else if(expresionReg.charAt(i)=='|'){
                thomsonOr();
            }else{
                addSymbolToLanguage(expresionReg.charAt(i));
                thomsonChar(expresionReg.charAt(i));
            }
        }
        if(orDepth.get(0)){
            noDeterminista.addTransicion(lastNodes.get(lastNodes.size()-1), lastNodes.get(0), EPSILON);
            lastNodes.remove(lastNodes.size()-1);
        }
        noDeterminista.getNodo(lastNodes.get(0)).setIsFinal(true);
        
    }
    private void addSymbolToLanguage(char caracter){
        boolean isAlreadyThere = false;
        for (int i=0; i<symbols.size();i++){
            if (Character.toString(caracter).equals(symbols.get(i))){
                isAlreadyThere=true;
                break;
            }
        }
        if (!isAlreadyThere){
            symbols.add(Character.toString(caracter));
        }
    }
    private void thomsonOpenPar(){
        firstNodes.add(noDeterminista.getCantidadNodos());
        noDeterminista.addNode(lastNodes.get(lastNodes.size()-1), EPSILON);
        lastNodes.set(lastNodes.size()-1, noDeterminista.getCantidadNodos()-1);
        isfirst=true;
        orDepth.add(false);
        parenthesisDepth++;
    }
    private void thomsonClosePar(){
        if(parenthesisDepth>0){
            if (orDepth.get(orDepth.size()-1)){
                noDeterminista.addTransicion(lastNodes.get(lastNodes.size()-1), lastNodes.get(lastNodes.size()-2), EPSILON);
                lastNodes.remove(lastNodes.size()-1);
            }
            firstNodes.remove(firstNodes.size()-1);
            isfirst=true;
            orDepth.remove(orDepth.size()-1);
        }
    }
    private void thomsonStar(){
        noDeterminista.getNodo(firstNodes.get(firstNodes.size()-1)).addTransicion(lastNodes.get(lastNodes.size()-1), EPSILON);
        noDeterminista.getNodo(lastNodes.get(lastNodes.size()-1)).addTransicion(firstNodes.get(firstNodes.size()-1), EPSILON);
    }
    private void thomsonPlus(){
        noDeterminista.getNodo(lastNodes.get(lastNodes.size()-1)).addTransicion(firstNodes.get(firstNodes.size()-1), EPSILON);
        
    }
    private void thomsonOr(){
        isfirst=true;
        orDepth.set(orDepth.size()-1, true);
        firstNodes.remove(firstNodes.size()-1);
        lastNodes.add(firstNodes.get(firstNodes.size()-1));
    }
    private void thomsonQuestion(){
        noDeterminista.getNodo(firstNodes.get(firstNodes.size()-1)).addTransicion(lastNodes.get(lastNodes.size()-1), EPSILON);
        
    }
    private void thomsonChar(char caracter){
        if (isfirst){
            firstNodes.add(noDeterminista.getCantidadNodos());
            isfirst=false;
        }else{
            firstNodes.set(firstNodes.size()-1, noDeterminista.getCantidadNodos());
        }
        noDeterminista.addNode(lastNodes.get(lastNodes.size()-1), EPSILON);
        noDeterminista.addNode(noDeterminista.getCantidadNodos()-1, Character.toString(caracter));
        noDeterminista.addNode(noDeterminista.getCantidadNodos()-1, EPSILON);
        lastNodes.set(lastNodes.size()-1, noDeterminista.getCantidadNodos()-1);
    }
    
    private boolean validarSubConjuntos(String cadena){
        int nodoActual=0;
        NodoGrafo nodoEvaluando;
        for (int i=0; i<cadena.length();i++){
            nodoEvaluando = subConjuntos.getNodo(nodoActual);
            for (int j=0; j<nodoEvaluando.getTransiciones().size();j++){
                if(nodoEvaluando.getTransiciones().get(j).getParametro().equals(Character.toString(cadena.charAt(i)))){
                    nodoActual = nodoEvaluando.getTransiciones().get(j).getObjetivo();
                }
            }
        }
        return subConjuntos.getNodo(nodoActual).isIsFinal();
    }

    private boolean validarAFDDirecto(String cadena){
        int nodoActual=0;
        NodoGrafo nodoEvaluando;
        for (int i=0; i<cadena.length();i++){
            nodoEvaluando = afdDirecto.getNodo(nodoActual);
            for (int j=0; j<nodoEvaluando.getTransiciones().size();j++){
                if(nodoEvaluando.getTransiciones().get(j).getParametro().equals(Character.toString(cadena.charAt(i)))){
                    nodoActual = nodoEvaluando.getTransiciones().get(j).getObjetivo();
                }
            }
        }
        return afdDirecto.getNodo(nodoActual).isIsFinal();
    }

    public int getRaizArbol(){
        return raices.get(raices.size()-1);
    }
    public ArbolER getArbol(){
        return arbolER;
    }
}