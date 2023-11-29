package ar.edu.unlu.poo.modelo;

import java.io.Serializable;

public class J extends Carta implements Serializable {
    public J(Palo palo) {
        super(11,palo);
        puntos = 10;
    }

    @Override
    public String toString() {
        return " |J "+ palo + "|";
    }
}
