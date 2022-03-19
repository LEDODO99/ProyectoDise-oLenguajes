package com.diseno;

import java.beans.Expression;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;


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
            if(lector.generarAutomatas(expReg)){
                System.out.println("Automatas generados apropiadamente");
                drawGraphviz("noDeterminista.dot", lector.getNFA(), "noDeterminista.png");
                drawGraphviz("subConjuntos.dot", lector.getSubconjuntos(), "subConjuntos.png");
                if(lector.validarCadena(expVal))
                    System.out.println("Expresion valida!");
                else
                    System.out.println("Expresion no válida");
            }
            
            System.out.println("\n\n\nMENU: ingrese la opcion que desea\n_________________________________________\n(1) Realizar una nueva validacion\n(2) Salir");
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
}
