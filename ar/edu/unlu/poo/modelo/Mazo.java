package ar.edu.unlu.poo.modelo;

import java.util.ArrayList;

import static java.lang.Math.random;

public class Mazo {
    private ArrayList<Carta> cartasEnMesa;
    private ArrayList<Carta> mazoDeCartas;

    public Mazo(){
        cartasEnMesa = new ArrayList<>();
        mazoDeCartas = new ArrayList<>();
        for (int i = 1; i <= 13; i++){
            nueva_carta(Palo.PICAS, i);
        }
        for (int i = 1; i <= 13; i++){
            nueva_carta(Palo.CORAZONES, i);
        }
        for (int i = 1; i <= 13; i++){
            nueva_carta(Palo.ROMBOS, i);
        }
        for (int i = 1; i <= 13; i++){
            nueva_carta(Palo.TREBOLES, i);
        }
    }

    private void nueva_carta(Palo palo, int numero) {
        Carta nuevaCarta;
        if (numero == 1){
            nuevaCarta = new As(palo);
        } else if (numero >= 2 && numero <= 10) {
            nuevaCarta = new Carta(numero, palo);
        } else if (numero == 11) {
            nuevaCarta = new J(palo);
        } else if (numero == 12){
            nuevaCarta = new Q(palo);
        } else if (numero == 13) {
            nuevaCarta = new K(palo);
        }else {return;}
        mazoDeCartas.add(nuevaCarta);
    }

    public void repartir(int cantidadXJugador, Jugador jugador){
        Carta cartaAux;
        if (mazoDeCartas.isEmpty() && !cartasEnMesa.isEmpty()){
            mezclarMazo();
        }
        for (int i = 0; i < cantidadXJugador; i++){
            cartaAux = mazoDeCartas.remove((int) (random() * (mazoDeCartas.size() - 1)));
            jugador.agregarCartasEnMano(cartaAux);
        }
    }
    public void sacarCartaMazo(Jugador jugadorQueSolicita){
        if (mazoDeCartas.isEmpty() && !cartasEnMesa.isEmpty()){
            mezclarMazo();
        }
        Carta cartaAux = mazoDeCartas.remove((int) (random() * (mazoDeCartas.size() - 1)));
        jugadorQueSolicita.agregarCartasEnMano(cartaAux);
    }

    public void sacarCartaBocaArriba(Jugador jugador){
        Carta cartaAux = cartasEnMesa.remove(cartasEnMesa.size() - 1);
        jugador.agregarCartasEnMano(cartaAux);
    }

    public void mezclarMazo(){
        Carta cartaAux;
        while(!cartasEnMesa.isEmpty()){
            cartaAux = cartasEnMesa.remove((int) (random() * (cartasEnMesa.size() - 1)));
            mazoDeCartas.add(cartaAux);
        }
    }
}
