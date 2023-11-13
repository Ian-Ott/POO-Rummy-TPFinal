package ar.edu.unlu.poo.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Tapete implements Serializable {
    private ArrayList<Jugada> jugada;

    public Tapete(){
        jugada = new ArrayList<>();
    }

    public void agregarJugada(Jugada jugada) {
        this.jugada.add(jugada);
    }

    public ArrayList<Jugada> getJugada() {
        return jugada;
    }

}
