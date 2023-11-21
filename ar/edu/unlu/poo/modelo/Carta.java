package ar.edu.unlu.poo.modelo;

import java.io.Serializable;

public class Carta implements Serializable, ICarta {
    protected int puntos;
    protected Palo palo;
    protected int numero;

    public Carta(int numero,Palo palo){
        puntos = numero;
        this.numero = numero;
        this.palo = palo;
    }

    @Override
    public int getNumero() {
        return numero;
    }

    @Override
    public Palo getPalo() {
        return palo;
    }

    @Override
    public int getPuntos() {
        return puntos;
    }

    @Override
    public String toString(){
        return " |" + numero + " "+ palo + "|";
    }
}
