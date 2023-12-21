package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.IJugador;

import java.util.ArrayList;

public interface IVista {

    void setControlador(Controlador controlador);

    void pantallaEspera();

    void actualizarCantJugadores();


    void iniciarTurno();

    void nuevoTurno();

    void continuarTurnoActual();

    void finalizarPartida();


    void actualizarJugadas();

    void cerrarPartida();

    void mostrarApuestaCancelada();

    void avisarSobreApuesta();


    void mostrarResultadosPuntosRonda(ArrayList<IJugador> jugadores);

    void finalizarPartidaAmistosamente();

    void eleccionAnularPartida();

    void obtenerNombre();

    void cerrarJuego();

    void mostrarTablaPosiciones(ArrayList<IJugador> jugadores);

    void mostrarErrorConexion();

    void errorCantidadJugadores();

    void mostrarJugadorSalioDelJuego();

    void avisarCambiosOpcionesMesa();

    void activarSoloChat();

    void mostrarNuevoMensaje(String cambio);

    void mostrarErrorJugadaLLena();

    void mostrarErrorCartaNoAgregada();

    void mostrarErrorNoEsJugada();

    void mostrarErrorCartasInsuficientes();

    void mostrarErrorRummyNoDisponible();
}
