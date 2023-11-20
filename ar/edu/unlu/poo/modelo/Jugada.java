package ar.edu.unlu.poo.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Jugada implements Serializable {
    private ArrayList<Carta> cartasJugada;
    private String nombreCreadorJugada;
    private boolean jugadaLlena;

    public void setCartasJugada(ArrayList<Carta> cartasJugada) {
        this.cartasJugada = cartasJugada;
    }

    public void agregarCartaAJugada(Carta nuevaCarta) {
        this.cartasJugada.add(nuevaCarta);
    }

    public void setJugadaLlena(boolean jugadaLlena) {
        this.jugadaLlena = jugadaLlena;
    }

    public boolean isJugadaLlena(){return jugadaLlena;}

    public ArrayList<Carta> getCartasJugada() {
        return cartasJugada;
    }

    public void setNombreCreadorJugada(String nombreCreadorJugada) {
        this.nombreCreadorJugada = nombreCreadorJugada;
    }

    public String getNombreCreadorJugada() {
        return nombreCreadorJugada;
    }

    @Override
    public String toString() {
        String acumulador = "\n";
        for (int i = 0; i < cartasJugada.size(); i++) {
            acumulador += cartasJugada.get(i);
        }
        return acumulador;
    }
}
