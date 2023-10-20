package ar.edu.unlu.poo.modelo;

public class Carta {
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

    public void setEsta_enMazo(boolean esta_enMazo) {
        this.esta_enMazo = esta_enMazo;
    }

    public boolean getEsta_enMazo(){
        return esta_enMazo;
    }
    public int getPuntos() {
        return puntos;
    }
}
