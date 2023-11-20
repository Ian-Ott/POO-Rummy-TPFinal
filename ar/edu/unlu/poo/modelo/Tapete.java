package ar.edu.unlu.poo.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Tapete implements Serializable, ITapete {
    private ArrayList<Jugada> jugada;

    private int boteApuestas;

    public Tapete(){
        jugada = new ArrayList<>();
        boteApuestas = 0;
    }

    public void agregarApuesta(int apuesta) {
        this.boteApuestas += apuesta;
    }

    public void sacarApuesta(int apuesta) {
        this.boteApuestas -= apuesta;
    }

    public int getBoteApuestas() {
        return boteApuestas;
    }

    public int otorgarFichasAlGanador(){
        //le otorga al jugador el 80% de las fichas en el bote
        // y el resto se mantiene en el bote para partidas siguientes
        int fichasGanadas = (int) (boteApuestas * 0.8);
        boteApuestas = (int) (boteApuestas * 0.2);
        return fichasGanadas;
    }

    @Override
    public void agregarJugada(Jugada jugada) {
        this.jugada.add(jugada);
    }

    @Override
    public ArrayList<Jugada> getJugada() {
        return jugada;
    }

    @Override
    public String toString() {
        String acumulador = "\n";
        if (!jugada.isEmpty()){
            for (int i = 0; i < jugada.size(); i++) {
                acumulador += "\nJugada " + (i+1) + ":\n" + jugada.get(i);
            }
        }else {acumulador += "No hay jugadas en la mesa";}
        return acumulador;
    }
}
