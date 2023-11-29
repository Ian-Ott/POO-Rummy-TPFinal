package ar.edu.unlu.poo.modelo;

import java.io.Serializable;

public class As extends Carta implements Serializable {
    public As(Palo palo) {
        super(1, palo);
        puntos = 15;
    }

    @Override
    public String toString() {
        return " |AS "+ palo + "|";
    }
}
