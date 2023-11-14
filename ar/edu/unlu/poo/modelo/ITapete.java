package ar.edu.unlu.poo.modelo;

import java.util.ArrayList;

public interface ITapete {
    void agregarJugada(Jugada jugada);

    ArrayList<Jugada> getJugada();

    String toString();
}
