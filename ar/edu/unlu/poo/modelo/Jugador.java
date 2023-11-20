package ar.edu.unlu.poo.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Jugador implements Serializable {
    private String nombre;
    private ArrayList<Carta> cartasEnMano;
    private int puntosTotalesXP;
    private int puntosDePartida;
    private boolean jefeMesa;
    private int cantApostada;
    private int fichasGanadasPartida;
    private int fichasTotales;
    private boolean agregoJugada;
    private boolean eliminado;

    private boolean hizoRummy;

    public Jugador(String nombreJugador) {
        cartasEnMano = new ArrayList<>();
        fichasTotales = 1500000;
        //les agrego fichas para apostar para que funcione el sistema de apuestas
        nombre = nombreJugador;
        agregoJugada = false;
        eliminado = false;
        hizoRummy = false;
        fichasGanadasPartida = 0;
        puntosTotalesXP = 0;
    }


    public void setAgregoJugada(boolean agregoJugada) {
        this.agregoJugada = agregoJugada;
    }


    public boolean getAgregoJugada(){
        return agregoJugada;
    }


    public String getNombre() {
        return nombre;
    }



    public void setPuntosDePartida(int puntosDePartida) {
        this.puntosDePartida = puntosDePartida;
    }


    public void sumarPuntosDePartida(int puntos) {
        this.puntosDePartida += puntosDePartida;
    }


    public int getPuntosDePartida() {
        return puntosDePartida;
    }


    public void restarFichasTotales(int fichas) {
        this.fichasTotales -= fichas;
    }


    public void sumarFichasTotales(int fichas) {
        this.fichasTotales += fichas;
    }


    public int getFichasTotales() {
        return fichasTotales;
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


    public void sumarPuntosTotalesXP(int puntosTotalesXP) {
        this.puntosTotalesXP += puntosTotalesXP;
    }


    public int getPuntosTotalesXP() {
        return puntosTotalesXP;
    }


    public ArrayList<Carta> getCartasEnMano() {
        return cartasEnMano;
    }


    public void agregarCartasEnMano(Carta nuevaCarta) {
        this.cartasEnMano.add(nuevaCarta);
    }


    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }


    public boolean isEliminado(){return eliminado;}


    public void setHizoRummy(boolean hizoRummy) {
        this.hizoRummy = hizoRummy;
    }


    public boolean getHizoRummy(){return hizoRummy;}


    public void setFichasGanadasPartida(int fichasGanadasPartida) {
        this.fichasGanadasPartida = fichasGanadasPartida;
    }


    public int getFichasGanadasPartida() {
        return fichasGanadasPartida;
    }

    //antiguo tirar carta
    /*public Carta tirarCarta(Palo palo, int numero){
        //idea provisional
        Carta cartaATirar = null;
        for (int i = 0; i < cartasEnMano.size();i++){
            if (cartasEnMano.get(i).palo.equals(palo) && cartasEnMano.get(i).numero == numero){
                cartaATirar = cartasEnMano.remove(i);
            }
        }
        return cartaATirar;
    }*/

    public Carta tirarCarta(Integer posicion){
        //idea provisional
        if (posicion < cartasEnMano.size()){
            return cartasEnMano.remove((int)posicion);
        }
        return null; //cambiar por excepcion
    }


    @Override
    public String toString() {
        return "Nombre: " + nombre + "puntos de partida actuales: " + puntosDePartida;
    }

    /*public void usarCarta(Carta cartaUsada) {
        cartasEnMano.remove(cartaUsada);
    }*/

    public void devolverCarta(Carta carta) {
        this.cartasEnMano.add(carta);
    }
}
