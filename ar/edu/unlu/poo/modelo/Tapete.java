package ar.edu.unlu.poo.modelo;

import java.util.ArrayList;

public class Tapete {
    private ArrayList<Carta> jugada;

    public Tapete(ArrayList<Carta> nuevaJugada){
        jugada = nuevaJugada;
    }

    public ArrayList<Carta> getJugada() {
        return jugada;
    }

    public void agregarCartaAJugada(Carta nuevaCarta) {
        this.jugada.add(nuevaCarta);
    }
}
