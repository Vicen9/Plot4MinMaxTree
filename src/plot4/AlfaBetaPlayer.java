/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plot4;

/**
 *
 * @author José María Serrano
 * @version 1.7 Departamento de Informática. Universidad de Jáen
 * Última revisión: 2023-03-30
 *
 * Inteligencia Artificial. 2º Curso. Grado en Ingeniería Informática
 *
 * Clase AlfaBetaPlayer para representar al jugador CPU que usa una técnica de IA
 *
 * Esta clase es en la que tenemos que implementar y completar
 * el algoritmo MiniMax con Poda AlfaBeta
 *
 */
import java.util.ArrayList;

import static plot4.Main.PLAYER1;
import static plot4.Main.PLAYER2;


public class AlfaBetaPlayer extends Player {

    /**
     * @brief funcion que determina donde colocar la ficha este turno
     * @param tablero Tablero de juego
     * @param conecta Número de fichas consecutivas adyacentes necesarias para
     * ganar
     * @return columna donde dejar caer la ficha
     */
    private Nodo raiz;
    private final int PROFMAX = 15;
    private int alpha = Integer.MIN_VALUE;
    private int beta = Integer.MAX_VALUE;
    @Override
    public int turno(Grid tablero, int conecta) {

        raiz = new Nodo(tablero);
        int[] colum;


        colum = generaArbolMinMax(raiz,0,alpha,beta);
        return colum[1];


    } // turno



//Metodos nuestros

    //Apartado de Restrained

    private int heuristica(int player, Nodo actual){
        int valor = 0;

        if (player == PLAYER1){
            valor = cuentaPiezasConse(PLAYER1,actual);
            valor = valor * -1;
        }else {
            valor = cuentaPiezasConse(PLAYER2,actual);
        }

        //System.out.println("Heuristica :"+valor);
        return valor;
    }

    private int cuentaPiezasConse(int player, Nodo actual){

        int cont=0;
        int max = 0;
        int maxV=0;
        int maxH=0;
        int diaA=0;
        int diaD=0;
        int[][] tablero = actual.grid.getGrid();


        //Horizontal
        for (int i = 0; i < actual.grid.filas; i++) {
            for (int j = 0; j < actual.grid.columnas; j++) {
                if (tablero[i][j] == player){
                    cont++;
                }else {
                    cont = 0;
                }

                if (cont > maxH) maxH = cont;
            }

            cont=0;
        }

        //Vertical
        cont=0;
        for (int i = 0; i < actual.grid.columnas; i++) {
            for (int j = 0; j < actual.grid.filas; j++) {
                if (tablero[j][i] == player){
                    cont++;
                }else {
                    cont = 0;
                }
                if (cont > maxV) maxV = cont;
            }

            cont=0;
        }


        //Diagonal descendente
        cont=0;
        for (int k = 0; k <= actual.grid.filas + actual.grid.columnas - 2; k++) {
            int iInicio = Math.max(0, k - actual.grid.columnas + 1);
            int iFin = Math.min(actual.grid.filas - 1, k);

            for (int i = iInicio; i <= iFin; i++) {
                int j = actual.grid.columnas - 1 - (k - i);

                if (tablero[i][j] == player){
                    cont++;
                }else {
                    cont=0;
                }
                if (cont > diaD) diaD=cont;
            }

            cont=0;

        }

        //Diagonal ascendente
        cont=0;
        for (int k = 0; k <= actual.grid.filas + actual.grid.columnas - 2; k++) {
            int iInicio = Math.max(0, k - actual.grid.columnas + 1);
            int iFin = Math.min(actual.grid.filas - 1, k);

            for (int i = iInicio; i <= iFin; i++) {
                int j = k - i;
                if (tablero[i][j] == player){
                    cont++;
                }else {
                    cont=0;
                }
                if (cont > diaA) diaA = cont;
            }
            cont=0;
        }

        max = (maxH * maxH) + (maxV * maxV) + (diaA * diaA) + (diaD*diaD);

        //System.out.println("Hori: "+maxH + " Vert:" + maxV + " Dia1 "+diaA +" dia2"+diaD);
        //System.out.println("Tablero");
        //actual.grid.print();

        return max;
    }






    //Fin metodos auxiliares


    //Método usado para generar los hijos a partir de un nodo raiz dado
    void generaHijos(Nodo raiz, int profundidad){
        for (int i = 0; i < raiz.grid.getColumnas(); i++) {
            if ( !raiz.grid.fullColumn(i) ){
                Grid gridCopia;
                Nodo temp;
                if (profundidad % 2 == 0){ //Caso en que estemos en max
                    gridCopia = new Grid(raiz.grid);
                    gridCopia.set(i,PLAYER2);
                    temp = new Nodo(gridCopia);
                    temp.col=i;
                    raiz.hijos.add(temp);//Añadimos el hijo al vector de hijos
                }else { //Caso en que sea min
                    gridCopia = new Grid(raiz.grid);
                    gridCopia.set(i,PLAYER1);
                    temp = new Nodo(gridCopia);
                    temp.col=i;
                    raiz.hijos.add(temp); //Añadimos el hijo al vector de hijos
                }


            }//if
        }//for

    }

    private int[] generaArbolMinMax(Nodo raiz, int profundidad,int alpha, int beta){
        int[] salida = new int[2];


        if (raiz.grid.checkWin() == PLAYER1){ //Valoramos el caso de que gana el j1
            salida[0]=-1000;
            salida[1]=raiz.col;
            return salida;
        } else if (raiz.grid.checkWin() == PLAYER2) { //Valoramos que gana el j2
            salida[0]=1000;
            salida[1]=raiz.col;
            return salida;
        }


        generaHijos(raiz,profundidad); //Generamos los hijos

        if (raiz.hijos.size() == 0){ //Caso de empate ya que no generamos hijos ni es ganador nadie
            salida[0]=0;
            salida[1]=raiz.col;
            return salida;
        }

        //No es nodo hoja
        if (profundidad == PROFMAX){
            if (profundidad % 2 == 0){
                int val = heuristica(PLAYER1,raiz);
                salida[0] = val;
                salida[1] = raiz.col;

                return salida;
            }else {
                int val = heuristica(PLAYER2,raiz);
                salida[0] = val;
                salida[1] = raiz.col;
                return salida;
            }
        }



        int [] hijoPos = new int[2];
        if (profundidad % 2 == 0){
            hijoPos[0] = Integer.MIN_VALUE;
        }else {
            hijoPos[0] = Integer.MAX_VALUE;
        }

        for (int i = 0; i < raiz.hijos.size(); i++) {//Llamada recursiva por cada hijo guardando el valor para luego quedarnos con el correspondiente
            int[] hijo = generaArbolMinMax(raiz.hijos.get(i),profundidad+1,alpha,beta);
            if (profundidad % 2 == 0){ //MAX
                if (hijo[0] > hijoPos[0]){
                    hijoPos[0]=hijo[0];
                    hijoPos[1]= raiz.hijos.get(i).col;
                }

                //Actualizamos alpha con el mayor por estar en max
                if (hijo[0] > alpha){
                    alpha = hijo[0];
                    if (alpha >= beta){ //Poda alpha
                        //System.out.println("Poda alpha");
                        break;
                    }
                }

            }else { //MIN

                if (hijo[0] < hijoPos[0]){
                    hijoPos[0]=hijo[0];
                    hijoPos[1]= raiz.hijos.get(i).col;
                }

                //Actualizamos beta con el menor por estar en min
                if (hijo[0] < beta){
                    beta = hijo[0];
                    if (alpha >= beta){ //Poda beta
                        //System.out.println("Poda beta");
                        break;
                    }
                }

            }

        }//for




        return hijoPos;
    }

    //FIN METODOS NUESTROS
    //Clase nodo
    private class Nodo{ //Clase nodo para el árbol
        Grid grid;
        ArrayList<Nodo> hijos;

        int valorUtil;

        int col=-1;

        public Nodo() {
            hijos=new ArrayList<>();
            valorUtil=0;
        }

        public Nodo(Grid grid) {
            this.grid = grid;
            hijos = new ArrayList<>();
            valorUtil=0;
        }
    }

} // AlfaBetaPlayer
