package ar.edu.unlu.poo.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Jugador implements Serializable, IJugador {
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
    private boolean enAutomatico;

    private boolean activo;

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
        enAutomatico = false;
        cantApostada = 0;
        activo = true;
    }


    @Override
    public void setAgregoJugada(boolean agregoJugada) {
        this.agregoJugada = agregoJugada;
    }


    @Override
    public boolean getAgregoJugada(){
        return agregoJugada;
    }


    @Override
    public String getNombre() {
        return nombre;
    }



    @Override
    public void setPuntosDePartida(int puntosDePartida) {
        this.puntosDePartida = puntosDePartida;
    }


    @Override
    public void sumarPuntosDePartida(int puntos) {
        this.puntosDePartida += puntosDePartida;
    }


    @Override
    public int getPuntosDePartida() {
        return puntosDePartida;
    }


    @Override
    public void restarFichasTotales(int fichas) {
        this.fichasTotales -= fichas;
    }


    @Override
    public void sumarFichasTotales(int fichas) {
        this.fichasTotales += fichas;
    }


    @Override
    public int getFichasTotales() {
        return fichasTotales;
    }


    @Override
    public void setCantApostada(int cantApostada) {
        this.cantApostada = cantApostada;
    }


    @Override
    public int getCantApostada() {
        return cantApostada;
    }


    @Override
    public void setJefeMesa(boolean jefeMesa) {
        this.jefeMesa = jefeMesa;
    }



    @Override
    public boolean getJefeMesa(){
        return jefeMesa;
    }


    @Override
    public void sumarPuntosTotalesXP(int puntosTotalesXP) {
        this.puntosTotalesXP += puntosTotalesXP;
    }


    @Override
    public int getPuntosTotalesXP() {
        return puntosTotalesXP;
    }


    @Override
    public ArrayList<Carta> getCartasEnMano() {
        return cartasEnMano;
    }


    @Override
    public void agregarCartasEnMano(Carta nuevaCarta) {
        this.cartasEnMano.add(nuevaCarta);
    }


    @Override
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }


    @Override
    public boolean isEliminado(){return eliminado;}


    @Override
    public void setHizoRummy(boolean hizoRummy) {
        this.hizoRummy = hizoRummy;
    }


    @Override
    public boolean getHizoRummy(){return hizoRummy;}


    @Override
    public void setFichasGanadasPartida(int fichasGanadasPartida) {
        this.fichasGanadasPartida = fichasGanadasPartida;
    }


    @Override
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
    public void setModoAutomatico(boolean estado){
        enAutomatico = estado;
    }

    public boolean isEnAutomatico() {
        return enAutomatico;
    }

    @Override
    public Carta tirarCarta(Integer posicion){
        if (posicion < cartasEnMano.size()){
            return cartasEnMano.remove((int)posicion);
        }
        return null; //cambiar por excepcion
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isActivo() {
        return activo;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + "puntos de partida actuales: " + puntosDePartida;
    }

    /*public void usarCarta(Carta cartaUsada) {
        cartasEnMano.remove(cartaUsada);
    }*/

    @Override
    public void devolverCarta(Carta carta) {
        this.cartasEnMano.add(carta);
    }
}
