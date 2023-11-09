package ar.edu.unlu.poo.modelo;

public interface ICarta {
    int getNumero();

    Palo getPalo();

    void setEsta_enMazo(boolean esta_enMazo);

    boolean getEsta_enMazo();

    int getPuntos();

    String toString();
}
