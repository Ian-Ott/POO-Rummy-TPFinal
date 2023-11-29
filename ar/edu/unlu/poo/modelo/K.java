package ar.edu.unlu.poo.modelo;

import java.io.Serializable;

public class K extends Carta implements Serializable {
    public K( Palo palo) {
        super(13, palo);
        puntos = 10;
    }

    @Override
    public String toString() {
        return " |K "+ palo + "|";
    }
}
