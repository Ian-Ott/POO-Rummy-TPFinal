package ar.edu.unlu.poo.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Tapete implements Serializable, ITapete {
    private ArrayList<Jugada> jugada;

    private int boteApuesta;

    public Tapete(){
        jugada = new ArrayList<>();
        boteApuesta = 0;
    }

    public void agregarApuesta(int apuesta) {
        this.boteApuesta += apuesta;
    }

    public void sacarApuesta(int apuesta) {
        this.boteApuesta -= apuesta;
    }

    public int getBoteApuesta() {
        return boteApuesta;
    }

    public int otorgarFichasAlGanador(){
        //le otorga al jugador el 80% de las fichas en el bote
        // y el resto se mantiene en el bote para partidas siguientes
        int fichasGanadas = (int) (boteApuesta * 0.8);
        boteApuesta = (int) (boteApuesta * 0.2);
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
