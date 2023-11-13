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

    void mezclarMazo() throws RemoteException;

    void crearTapeteConJugada(ArrayList<Carta> jugada)throws RemoteException;


    boolean esEscalera(ArrayList<Carta> nuevaJugada)throws RemoteException;

    void acomodarValoresExtremos(ArrayList<Carta> nuevaJugada)throws RemoteException;

    ArrayList<Carta> generarPosibleEscalera(Carta carta1, Carta carta2, Carta carta3)throws RemoteException;

    boolean crearJugada(Carta carta1, Carta carta2, Carta carta3) throws RemoteException;

    boolean tieneExtremos(Carta carta1, Carta carta2, Carta carta3) throws RemoteException;

    boolean comprobarValores(Carta carta1, Carta carta2, Carta carta3, Carta carta4) throws RemoteException;

    /*private void agregarJugadaATapete(ArrayList<Carta> jugada){

        }*/
    void agregarCartaAJugada(Carta cartaElegida, Carta cartaDeLaJugada)throws RemoteException;

    void agregarCartaOrdenada(ArrayList<Carta> jugada, Carta cartaElegida)throws RemoteException;

    ArrayList<Carta> buscarJugada(Carta cartaDeLaJugada)throws RemoteException;

    void terminarTurno(Carta carta1, String jugadorActual) throws RemoteException;

    Jugador buscarJugadorIzquierda(Jugador jugadorActual)throws RemoteException;

    void cancelarAccion() throws RemoteException;

    void accionLista(Carta carta1, Carta carta2, Carta carta3, Carta carta4) throws RemoteException;

    boolean comprobarEscalera(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador)throws RemoteException;

    boolean crearJugada(Carta carta1, Carta carta2, Carta carta3, Carta carta4)throws RemoteException;

    boolean tieneExtremos(Carta carta1, Carta carta2, Carta carta3, Carta carta4)throws RemoteException;

    ArrayList<Carta> generarPosibleEscalera(Carta carta1, Carta carta2, Carta carta3, Carta carta4)throws RemoteException;

    void finalizarPartida(Jugador jugador)throws RemoteException;

    void sumarPuntos() throws RemoteException;

    void agregarPuntosClasificacion()throws RemoteException;

    void siguienteTurno(Jugador jugadorIzquierda)throws RemoteException;

    static IRummy getInstancia() throws RemoteException {
        return Rummy.getInstancia();
    }

    int getJugadoresSize()throws RemoteException;

    ArrayList<String> getNombreOponentes(String nombreJugador)throws RemoteException;

    ArrayList<Carta> getCartasJugador(String nombreJugador) throws RemoteException;

    ArrayList<Jugador> getJugadores()throws RemoteException;
    String getNombreTurnoActual()throws RemoteException;
    public Carta getCartaBocaArriba()throws RemoteException;

    void comprobarRummy(ArrayList<Integer> posicionesSeleccionadas, Jugador jugador) throws RemoteException;

    //boolean esAnfitrion(String nombreJugador);

    //IRummy getInstancia();
}
