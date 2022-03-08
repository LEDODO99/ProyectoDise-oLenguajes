package com.diseno;

import java.util.ArrayList;

public class Lector{
    private final String EPSILON = "epsilon";
    private Grafo noDeterminista;
    private boolean afterOr=false;
    private int parenthesisDepth=0;
    private boolean exitParenthesis=false;
    private ArrayList<Integer> firstNodes;
    private ArrayList<Integer> lastNodes;
    private ArrayList<String> symbols;

    private ArbolER arbolER;

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

    private void reiniciarVariables(){
        firstNodes=new ArrayList<>();
        lastNodes=new ArrayList<>();
        noDeterminista = new Grafo();
        subConjuntos = new Grafo();
        afdDirecto = new Grafo();
        afterOr=false;
        exitParenthesis=false;
        parenthesisDepth=0;
        firstNodes.add(0);
        lastNodes.add(0);
        symbols = new ArrayList<>();
        arbolER = new ArbolER();
    }
    public boolean generarAutomatas(String expresionReg){
        reiniciarVariables();
        algoritmoThomson(expresionReg);
        algoritmoSubconjuntos();
        algoritmoDirecto(expresionReg);
        return true;
    }
    public boolean validarCadena(String expresionEva){
        boolean validoSub=validarSubConjuntos(expresionEva);
        return true;
        //return (validarSubConjuntos(expresionEva)&&validarAFDDirecto(expresionReg));
    }

    private void algoritmoDirecto(String expresionReg){
        for (int i=0; i<expresionReg.length();i++){
            char caracterDeExp = expresionReg.charAt(i);
            if (caracterDeExp=='('){
                directOpenPar();
            }else if(caracterDeExp==')'){
                directClosePar();
            }else if(caracterDeExp=='*'){
                directStar();
            }else if(caracterDeExp=='+'){
                directPlus();
            }else if(caracterDeExp=='?'){
                directQuestion();
            }else if(caracterDeExp=='|'){
                directOr();
            }else{
            }
        }
    
    }
    private void directOpenPar(){

    }
    private void directClosePar(){

    }
    private void directStar(){

    }
    private void directPlus(){

    }
    private void directQuestion(){

    }
    private void directOr(){

    }



    private void algoritmoSubconjuntos(){
        ArrayList<ArrayList<Integer>> subconjuntos = new ArrayList<>();
        ArrayList<Integer> cadenaAgregar = new ArrayList<>();
        cadenaAgregar.add(0);
        cadenaAgregar=epsilonChain(cadenaAgregar);
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
        firstNodes.add(lastNodes.get(lastNodes.size()-1));
        parenthesisDepth++;
    }
    private void thomsonClosePar(){
        exitParenthesis=true;
        parenthesisDepth--;
    }
    private void thomsonStar(){
        noDeterminista.getNodo(firstNodes.get(firstNodes.size()-1)).addTransicion(lastNodes.get(lastNodes.size()-1), EPSILON);
        noDeterminista.getNodo(lastNodes.get(lastNodes.size()-1)).addTransicion(firstNodes.get(firstNodes.size()-1), EPSILON);
    }
    private void thomsonPlus(){
        noDeterminista.getNodo(lastNodes.get(lastNodes.size()-1)).addTransicion(firstNodes.get(firstNodes.size()-1), EPSILON);
        
    }
    private void thomsonOr(){
        afterOr=true;
    }
    private void thomsonQuestion(){
        noDeterminista.getNodo(firstNodes.get(firstNodes.size()-1)).addTransicion(lastNodes.get(lastNodes.size()-1), EPSILON);
        
    }
    private void thomsonChar(char caracter){
        if (afterOr){
            afterOr=false;
            noDeterminista.addNode(firstNodes.get(firstNodes.size()-1), EPSILON);
            noDeterminista.addNode(noDeterminista.getCantidadNodos()-1, Character.toString(caracter));
            noDeterminista.addTransicion(noDeterminista.getCantidadNodos()-1, lastNodes.get(lastNodes.size()-1), EPSILON);
        }else{
            if(exitParenthesis){
                firstNodes.remove(firstNodes.size()-1);
                exitParenthesis=false;
            }
            firstNodes.set(firstNodes.size()-1, lastNodes.get(lastNodes.size()-1));
            noDeterminista.addNode(firstNodes.get(firstNodes.size()-1), EPSILON);
            noDeterminista.addNode(noDeterminista.getCantidadNodos()-1, Character.toString(caracter));
            noDeterminista.addNode(noDeterminista.getCantidadNodos()-1, EPSILON);
            lastNodes.set(lastNodes.size()-1, noDeterminista.getCantidadNodos()-1);

        }
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
                if(nodoEvaluando.getTransiciones().get(j).getParametro().equals(Character.toString(cadena.charAt(j)))){
                    nodoActual = nodoEvaluando.getTransiciones().get(j).getObjetivo();
                }
            }
        }
        return afdDirecto.getNodo(nodoActual).isIsFinal();
    }

}