package ar.edu.unlu.poo.modelo;

import java.util.ArrayList;

public interface IJugador {
    void setAgregoJugada(boolean agregoJugada);

    boolean getAgregoJugada();

    String getNombre();

    void setPuntosDePartida(int puntosDePartida);

    void sumarPuntosDePartida(int puntos);

    int getPuntosDePartida();

    void restarFichasTotales(int fichas);

    void sumarFichasTotales(int fichas);

    int getFichasTotales();

    void setCantApostada(int cantApostada);

    int getCantApostada();

    void setJefeMesa(boolean jefeMesa);

    boolean getJefeMesa();

    void sumarPuntosTotalesXP(int puntosTotalesXP);

    int getPuntosTotalesXP();

    ArrayList<Carta> getCartasEnMano();

    void agregarCartasEnMano(Carta nuevaCarta);

    void setEliminado(boolean eliminado);

    boolean isEliminado();

    void setHizoRummy(boolean hizoRummy);

    boolean getHizoRummy();

    void setFichasGanadasPartida(int fichasGanadasPartida);

    int getFichasGanadasPartida();

    Carta tirarCarta(Integer posicion);

    @Override
    String toString();

    void devolverCarta(Carta carta);
}
