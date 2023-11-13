package ar.edu.unlu.poo.modelo;

import java.util.ArrayList;

public class Jugada {
    private ArrayList<Carta> cartasJugada;
    private String nombreCreadorJugada;

    public void setCartasJugada(ArrayList<Carta> cartasJugada) {
        this.cartasJugada = cartasJugada;
    }

    public void agregarCartaAJugada(Carta nuevaCarta) {
        this.cartasJugada.add(nuevaCarta);
    }

    public ArrayList<Carta> getCartasJugada() {
        return cartasJugada;
    }

    public void setNombreCreadorJugada(String nombreCreadorJugada) {
        this.nombreCreadorJugada = nombreCreadorJugada;
    }

    public String getNombreCreadorJugada() {
        return nombreCreadorJugada;
    }
}
