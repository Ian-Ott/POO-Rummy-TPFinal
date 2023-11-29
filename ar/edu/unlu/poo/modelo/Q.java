package ar.edu.unlu.poo.modelo;

import java.io.Serializable;

public class Q extends Carta implements Serializable {
    public Q(Palo palo) {
        super(12, palo);
        puntos = 10;
    }

    @Override
    public String toString() {
        return " |Q "+ palo + "|";
    }
}
