/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plot4;

import java.util.ArrayList;

import static plot4.Main.PLAYER1;
import static plot4.Main.PLAYER2;

/**
 *
 * @author José María Serrano
 * @version 1.7 Departamento de Informática. Universidad de Jáen 
 * Última revisión: 2023-03-30
 *
 * Inteligencia Artificial. 2º Curso. Grado en Ingeniería Informática
 *
 * Clase MiniMaxPlayer para representar al jugador CPU que usa una técnica de IA
 *
 * Esta clase es en la que tenemos que implementar y completar el algoritmo
 * MiniMax
 *
 */
public class MiniMaxPlayer extends Player {

    /**
     * @brief funcion que determina donde colocar la ficha este turno
     * @param tablero Tablero de juego
     * @param conecta Número de fichas consecutivas adyacentes necesarias para
     * ganar
     * @return Devuelve si ha ganado algun jugador
     */
    //ATRIBUTOS
    private Nodo raiz;

    @Override
    public int turno(Grid tablero, int conecta) {
        raiz = new Nodo(tablero);
        int[] colum;


        colum = generaArbolMinMax(raiz,1);
         return colum[1];

    } // turno

//Metodos nuestros


    //Metodos auxiliares


    //Este método muestra la matriz
    void muestraMatriz(int c, int f, int[][]m){
        for (int i = 0; i < f; i++) {
            for (int j = 0; j < c; j++) {
                System.out.printf("%d ",m[i][j]);
            }
            System.out.printf("\n");
        }

    }


    //Fin metodos auxiliares


    //Método usado para generar los hijos a partir de un nodo raiz dado
     void generaHijos(Nodo raiz, int profundidad){
        for (int i = 0; i < raiz.grid.getColumnas(); i++) {
            if ( !raiz.grid.fullColumn(i) ){
                Grid gridCopia;
                Nodo temp;
                if (profundidad % 2 == 0){ //Caso en que estemos en min
                    gridCopia = new Grid(raiz.grid);
                    gridCopia.set(i,PLAYER1);
                    temp = new Nodo(gridCopia);
                    temp.col=i;
                    raiz.hijos.add(temp);//Añadimos el hijo al vector de hijos
                }else { //Caso en que sea MAX
                    gridCopia = new Grid(raiz.grid);
                    gridCopia.set(i,PLAYER2);
                    temp = new Nodo(gridCopia);
                    temp.col=i;
                    raiz.hijos.add(temp); //Añadimos el hijo al vector de hijos
                }


            }//if
        }//for

    }

    private int[] generaArbolMinMax(Nodo raiz, int profundidad){
        int[] salida = new int[2];


        //Linea para visualizar el arbol por capas, util para depuración
        //if (profundidad == 2){
        //    System.out.println("Profundidad "+profundidad);
        //    muestraMatriz(raiz.grid.columnas,raiz.grid.filas,raiz.grid.getGrid());
        //}

        if (raiz.grid.checkWin() == PLAYER1){ //Valoramos el caso de que gana el j1
            salida[0]=PLAYER1;
            salida[1]=raiz.col;
            return salida;
        } else if (raiz.grid.checkWin() == PLAYER2) { //Valoramos que gana el j2
            salida[0]=PLAYER2;
            salida[1]=raiz.col;
            return salida;
        }


        generaHijos(raiz,profundidad); //Generamos los hijos

        if (raiz.hijos.size() == 0){ //Caso de empate ya que no generamos hijos ni es ganador nadie
            salida[0]=0;
            salida[1]=raiz.col;
            return salida;
        }


        int[] min = new int[2];
        min[0]=100; //Inicialización de variables

        int[] max = new int[2];
        max[0]=-100;

        for (int i = 0; i < raiz.hijos.size(); i++) {//Llamada recursiva por cada hijo guardando el valor para luego quedarnos con el correspondiente
            int[] hijo = generaArbolMinMax(raiz.hijos.get(i),profundidad+1);
            if (profundidad % 2 == 0){ //MIN
                if (hijo[0] < min[0]){
                    min[0]=hijo[0];
                    min[1]=hijo[1];
                }

            }else { //MAX

                if (hijo[0] > max[0]){
                    max[0]=hijo[0];
                    max[1]=hijo[1];
                }

            }

        }//for

        if (profundidad % 2 == 0) return min;
        else return max;
    }

    //FIN METODOS NUESTROS
    //Clase nodo
    private class Nodo{ //Clase nodo para el árbol
         Grid grid;
         ArrayList<Nodo> hijos;

         int col=-1;

        public Nodo() {
            hijos=new ArrayList<>();
        }

        public Nodo(Grid grid) {
            this.grid = grid;
            hijos = new ArrayList<>();
        }
    }


} // MiniMaxPlayer
