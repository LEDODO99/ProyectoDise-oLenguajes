package com.diseno;

import java.beans.Expression;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

import org.apache.commons.logging.Log;


/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Lector lector = new Lector();
        String menu = "1";
        while (menu.equals("1")){
            System.out.println("Ingrese la expresion regular");
            String expReg = scanner.nextLine();
            System.out.println("Ingrese la expresión a validar");
            String expVal = scanner.nextLine();
            long time1;
            long time2;
            boolean automataGenerado = false;
            time1 = System.nanoTime();
            automataGenerado = lector.generarThomson(expReg);
            time2 = System.nanoTime();
            if (automataGenerado){
                System.out.println("AFN Thomson generado apropiadamanete en "+ ((float)(time2-time1))/1000.0 +" microsegundos");
            } else{
                System.out.println("Error en generación de Automatas o cadena erronea");
            }
            time1 = System.nanoTime();
            automataGenerado = lector.generarSubConjuntos();
            time2 = System.nanoTime();
            if (automataGenerado){
                System.out.println("AFD Subconjuntos generado apropiadamanete en "+ ((float)(time2-time1))/1000.0 +" microsegundos");
            } else{
                System.out.println("Error en generación de Automatas o cadena erronea");
            }
            time1 = System.nanoTime();
            automataGenerado = lector.generarDirecto(expReg);
            time2 = System.nanoTime();
            if (automataGenerado){
                System.out.println("AFD Directo generado apropiadamanete en "+ ((float)(time2-time1))/1000.0 +" microsegundos");
            } else{
                System.out.println("Error en generación de Automatas o cadena erronea");
            }
            drawGraphviz("noDeterminista.dot", lector.getNFA(), "noDeterminista.png");
            drawGraphviz("subConjuntos.dot", lector.getSubconjuntos(), "subConjuntos.png");
            drawGraphviz("directo2.dot", lector.getAfdDirecto(), "directo2.png");
            drawTreeviz("arbol.dot", lector.getArbol(), lector.getRaizArbol(), "arbol.png");
            boolean cadenaValida;
            cadenaValida = lector.validarCadenaThomson(expVal);
            if(cadenaValida){
                System.out.println("Cadena Valida en AFN Thomson");
            }else{
                System.out.println("Cadena No Valida en AFN Thomson");
            }
            cadenaValida = lector.validarCadenaSub(expVal);
            if(cadenaValida){
                System.out.println("Cadena Valida en AFD Subconjuntos");
            }else{
                System.out.println("Cadena No Valida en AFD Subconjuntos");
            }
            cadenaValida = lector.validarCadenaDirect(expVal);
            if(cadenaValida){
                System.out.println("Cadena Valida en AFD Directo");
            }else{
                System.out.println("Cadena No Valida en AFD Directo");
            }
            System.out.println("Ejecución finalizada, presione enter para regresar al menu");
            scanner.nextLine();
            System.out.println("\n\n\n\n\nMENU: ingrese la opcion que desea\n_________________________________________\n(1) Realizar una nueva validacion\n(2) Salir");
            menu = scanner.nextLine();
        }
        System.out.println("Gracias por utilizar el programa\nPara salir presione la tecla ENTER\n...");
        scanner.nextLine();

    }
    private static String createGraphString(Grafo grafo){
        String texto="digraph G\n"
        + "{\n";
        for (int i=0; i<grafo.getCantidadNodos(); i++){
            for (int j=0; j<grafo.getNodo(i).getTransiciones().size();j++){
                texto=texto + i +" -> " + grafo.getNodo(i).getTransiciones().get(j).getObjetivo() +" [ fontcolor=orange, label=\""+grafo.getNodo(i).getTransiciones().get(j).getParametro()+"\"];";
            }
            if (grafo.getNodo(i).isIsFinal()){
                texto = texto + i + "[style=filled, fillcolor=yellow];\n";
            }
        }
        texto=texto+"\n}";
        return texto;
    }

    private static void createFile(String name, String content){
        FileWriter fw = null;
        PrintWriter pw = null;
        try{
            fw = new FileWriter(name);
            pw = new PrintWriter(fw);
            pw.write(content);
            pw.close();
            fw.close();
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        } finally {
            if (pw!=null){
                pw.close();
            }
        }
    }

    public static void drawGraphviz(String name, Grafo grafo, String imageName){
        try{
            createFile(name, createGraphString(grafo));
            ProcessBuilder pb;
            pb = new ProcessBuilder("dot", "-Tpng", "-o", imageName, name);

            pb.redirectErrorStream(true);
            pb.start();
        }catch (Exception e ){
            e.printStackTrace();
        }
    }
    public static void drawTreeviz(String name, ArbolER grafo, int raiz, String imageName){
        try{
            createFile(name, createTreeString(grafo,raiz));
            ProcessBuilder pb;
            pb = new ProcessBuilder("dot", "-Tpng", "-o", imageName, name);

            pb.redirectErrorStream(true);
            pb.start();
        }catch (Exception e ){
            e.printStackTrace();
        }
    }
    private static String createTreeString(ArbolER grafo, int raiz){
        String texto="digraph G\n"
        + "{\n";
        
        for (int i=0; i<grafo.getCantidadNodos(); i++){
            if(!grafo.getNodoIn(i).getIsLeaf()){
                texto=texto + i +" -> " + grafo.getNodoIn(i).getLeftChild() +";\n";
                if (!(grafo.getNodoIn(i).getContenido().equals("+")||grafo.getNodoIn(i).getContenido().equals("*")||grafo.getNodoIn(i).getContenido().equals("?"))){
                    texto=texto + i +" -> " + grafo.getNodoIn(i).getRightChild() +";\n";
                }
            }
            texto = texto + i +" [label = \""+grafo.getNodoIn(i).getContenido()+"\"];\n";
        }
        texto=texto+"\n}";
        return texto;
    }
}
