package ar.edu.unlu.poo.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Tapete implements Serializable, ITapete {
    private ArrayList<Jugada> jugada;

    public Tapete(){
        jugada = new ArrayList<>();
    }

    @Override
    public void agregarJugada(Jugada jugada) {
        this.jugada.add(jugada);
    }

    @Override
    public ArrayList<Jugada> getJugada() {
        return jugada;
    }

    @Override
    public String toString() {
        String acumulador = "\n";
        for (int i = 0; i < jugada.size(); i++) {
            acumulador += "Jugada " + i + ":\n" + jugada.get(i);
        }
        return acumulador;
    }
}
