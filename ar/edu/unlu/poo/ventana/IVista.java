package ar.edu.unlu.poo.ventana;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.Carta;
import ar.edu.unlu.poo.modelo.ICarta;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IVista {
    void iniciarVentana(String nombreJugador, boolean b) throws RemoteException;

    void actualizarCarta(Carta cambio);

    void setControlador(Controlador controlador);

    void pantallaEspera(boolean anfitrion) throws InterruptedException, RemoteException;

    void actualizarCantJugadores() throws RemoteException, InterruptedException;

    void cerrarPantallaEspera();


    void iniciarTurno() throws RemoteException;

    void esperarTurno() throws RemoteException;

    void darControl() throws RemoteException;


    void actualizarCartas(ArrayList<ICarta> cartasJugador);

    void nuevoTurno() throws RemoteException;

    void continuarTurnoActual() throws RemoteException;

    void finalizarPartida();


    void actualizarJugadas();
}
