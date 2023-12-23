package ar.edu.unlu.poo;

import ar.edu.unlu.poo.modelo.Rummy;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class PartidaGuardada implements Serializable {
    private String nombrePartida;
    private LocalDate fechaGuardado;
    private LocalTime horaGuardado;
    private Rummy juegoGuardado;
    private String anfitrion;

    public PartidaGuardada(String nombrePartida, Rummy partidaActual, String anfitrion){
        this.nombrePartida = nombrePartida;
        fechaGuardado = LocalDate.now();
        horaGuardado = LocalTime.now();
        juegoGuardado = partidaActual;
        this.anfitrion = anfitrion;
    }

    public Rummy getJuegoGuardado() {
        return juegoGuardado;
    }

    @Override
    public String toString() {
        return nombrePartida + " |Fecha: " + fechaGuardado + " | Hora: " + horaGuardado + " |" + " Anfitrion de partida: " + anfitrion;
    }
}
