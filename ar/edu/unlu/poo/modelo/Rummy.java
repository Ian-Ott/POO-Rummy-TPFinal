package ar.edu.unlu.poo.modelo;

import java.util.ArrayList;

public class Rummy {
    private Mazo mazoDeJuego;
    private ArrayList<Jugador> jugadores;
    private ArrayList<Carta> tapete;

    public Rummy(){
        jugadores = new ArrayList<>();
        mazoDeJuego = new Mazo();
        if (jugadores.size() == 2){
        repartirCartasJugadores(10);
        } else if (jugadores.size() >= 3) {
            repartirCartasJugadores(7);
        }
    }

    private void repartirCartasJugadores(int cantidadCartas) {
        for (int i = 0; i < jugadores.size(); i++){
            mazoDeJuego.repartir(cantidadCartas, jugadores.get(i));
        }
    }

    private void sacarCartaMazo(Jugador jugador){
        mazoDeJuego.sacarCartaMazo(jugador);
    }

    private void agarrarCartaBocaArriba(Jugador jugador){

    }

    private void mezclarMazo(){
        //cuando el mazo se quede sin cartas se van a agregar de manera random al mazo las cartas que quedaron boca arriba
    }
}
