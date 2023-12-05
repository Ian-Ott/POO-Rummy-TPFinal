package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.ICarta;
import ar.edu.unlu.poo.modelo.IJugador;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IVista {

    void setControlador(Controlador controlador);

    void pantallaEspera();

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

    void solicitarCerrarVentana();

    void mostrarTablaPosiciones(ArrayList<IJugador> jugadores);

    void mostrarErrorConexion();

    void errorCantidadJugadores();
}
