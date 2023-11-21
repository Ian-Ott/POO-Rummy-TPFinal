package ar.edu.unlu.poo.ventana;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.Carta;
import ar.edu.unlu.poo.modelo.ICarta;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IVista {
    void iniciarVentana(String nombreJugador, boolean b);

    void setControlador(Controlador controlador);

    void pantallaEspera(boolean anfitrion);

    void actualizarCantJugadores();


    void iniciarTurno();

    void esperarTurno();

    void actualizarCartas(ArrayList<ICarta> cartasJugador);

    void nuevoTurno();

    void continuarTurnoActual();

    void finalizarPartida();


    void actualizarJugadas();

    void cerrarPartida();

    void mostrarErrorApuesta();

    void avisarSobreApuesta();

    void mostrarResultadosPuntos();

    void finalizarPartidaAmistosamente();

    void eleccionAnularPartida();

    void obtenerNombre();
}
