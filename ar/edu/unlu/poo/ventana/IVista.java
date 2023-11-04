package ar.edu.unlu.poo.ventana;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.Carta;
import ar.edu.unlu.poo.modelo.Palo;

import java.rmi.RemoteException;

public interface IVista {
    void iniciarVentana(String nombreJugador, boolean b) throws RemoteException;

    void actualizarCarta(Carta cambio);

    void setControlador(Controlador controlador);

    void pantallaEspera(boolean anfitrion) throws InterruptedException, RemoteException;

    void actualizarCantJugadores() throws RemoteException, InterruptedException;

    void cerrarPantallaEspera();

    void agregarCarta(int numero, Palo palo);

    void agregarCartaOtroJugador(String nombreJugador);

    void nuevoTurno() throws RemoteException;
}
