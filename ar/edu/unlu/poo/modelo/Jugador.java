package ar.edu.unlu.poo.modelo;

import java.util.ArrayList;

public class Jugador {
    private ArrayList<Carta> cartasEnMano;
    private int puntosTotales;
    private boolean jefeMesa;
    private int cantApostada;
    private int bote;

    public void setBote(int bote) {
        this.bote = bote;
    }

    public int getBote() {
        return bote;
    }

    public void setCantApostada(int cantApostada) {
        this.cantApostada = cantApostada;
    }

    public int getCantApostada() {
        return cantApostada;
    }

    public void setJefeMesa(boolean jefeMesa) {
        this.jefeMesa = jefeMesa;
    }


    public boolean getJefeMesa(){
        return jefeMesa;
    }

    public void setPuntosTotales(int puntosTotales) {
        this.puntosTotales = puntosTotales;
    }

    public int getPuntosTotales() {
        return puntosTotales;
    }

    public ArrayList<Carta> getCartasEnMano() {
        return cartasEnMano;
    }

    public void agregarCartasEnMano(Carta nuevaCarta) {
        this.cartasEnMano.add(nuevaCarta);
    }

    public Carta tirarCarta(Palo palo, int numero){
        //idea provisional
        Carta cartaATirar = null;
        for (int i = 0; i < cartasEnMano.size();i++){
            if (cartasEnMano.get(i).palo.equals(palo) && cartasEnMano.get(i).numero == numero){
                cartaATirar = cartasEnMano.remove(i);
            }
        }
        return cartaATirar;
    }
}
