package ar.edu.unlu.poo.modelo;

import java.io.Serializable;

public class Carta implements Serializable, ICarta {
    protected int puntos;
    protected Palo palo;
    protected  boolean esta_enMazo;
    protected int numero;

    public Carta(int numero,Palo palo){
        puntos = numero;
        this.numero = numero;
        this.palo = palo;
        esta_enMazo = true;
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
    public void setEsta_enMazo(boolean esta_enMazo) {
        this.esta_enMazo = esta_enMazo;
    }

    @Override
    public boolean getEsta_enMazo(){
        return esta_enMazo;
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
