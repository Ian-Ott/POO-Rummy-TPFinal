package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRummy extends IObservableRemoto {
    void agregarJugador(Jugador nuevoJugador) throws RemoteException;
    boolean isJuegoIniciado() throws RemoteException;
    void repartirCartasJugadores(int cantidadCartas) throws RemoteException;

    void iniciarJuego() throws RemoteException;

    void sacarCartaMazo(String jugador) throws RemoteException;

    void agarrarCartaBocaArriba(String jugador) throws RemoteException;

    void comprobarRummy(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador) throws RemoteException;

    void comprobarCombinacion(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador)throws RemoteException;

    void mezclarMazo() throws RemoteException;


    boolean esEscalera(ArrayList<Carta> nuevaJugada)throws RemoteException;

    void acomodarValoresExtremos(ArrayList<Carta> nuevaJugada)throws RemoteException;

    void agregarCartaAJugada(ArrayList<Integer> posicionesSeleccionadas, int posicionJugada, String nombreJugador)throws RemoteException;

    void agregarCartaOrdenada(ArrayList<Carta> jugada, Carta cartaElegida)throws RemoteException;

    ArrayList<Carta> buscarJugada(int posicionDeLaJugada) throws RemoteException;

    void terminarTurno(ArrayList<Integer> carta1, String jugadorActual) throws RemoteException;

    Jugador buscarJugadorIzquierda(Jugador jugadorActual)throws RemoteException;


    void comprobarEscalera(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador)throws RemoteException;


    void finalizarPartida(String jugador)throws RemoteException;

    void contarPuntosPartida() throws RemoteException;

    void agregarPuntosClasificacion()throws RemoteException;

    void siguienteTurno(Jugador jugadorIzquierda)throws RemoteException;

    static IRummy getInstancia() throws RemoteException {
        return Rummy.getInstancia();
    }

    int getJugadoresSize()throws RemoteException;

    ArrayList<String> getNombreOponentes(String nombreJugador)throws RemoteException;

    ArrayList<String> getNombreJugadores()throws RemoteException;

    ArrayList<Carta> getCartasJugador(String nombreJugador) throws RemoteException;

    ArrayList<Jugador> getJugadores()throws RemoteException;
    String getNombreTurnoActual()throws RemoteException;
    Carta getCartaBocaArriba()throws RemoteException;

    ITapete getMesaJugadas()throws RemoteException;

    int getCantCartasOponente(String oponente) throws RemoteException;

    void modoExpres() throws RemoteException;

    void modoPuntos() throws RemoteException;


    //boolean esAnfitrion(String nombreJugador);

    //IRummy getInstancia();
}
