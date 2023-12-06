package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.exceptions.JugadorInexistente;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRummy extends IObservableRemoto {
    void agregarJugador(Jugador nuevoJugador, boolean anfitrion) throws RemoteException;
    boolean isJuegoIniciado() throws RemoteException;
    void repartirCartasJugadores(int cantidadCartas) throws RemoteException;

    void iniciarJuego() throws RemoteException;

    void iniciarOtraRonda() throws RemoteException;

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

    void pedidoAnularPartidaAmistosamente() throws RemoteException;

    void anularPartida(boolean anular) throws RemoteException;

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

    int getCantidadFichas(String nombreJugador) throws RemoteException;

    void apostarFichas(int cantFichas) throws RemoteException;

    void cancelarApuestas()throws RemoteException;

    boolean puedenApostarJugadores(int apuesta) throws RemoteException;

    int getCantApostada(String nombreJugador)throws RemoteException;

    boolean isApuestasActivas()throws RemoteException;

    void apostarFichasJugador(String nombreJugador) throws RemoteException;

    int getCantidadTotalApuesta()throws RemoteException;

    String getModoActual() throws RemoteException;

    String getNombreGanador() throws RemoteException;

    int getPuntosGanador()throws RemoteException;

    String getJugador(int posicion) throws RemoteException;

    boolean estaEliminado(String nombreJugador)throws RemoteException;

    int contarEliminados() throws RemoteException;

    void reengancharJugador(String nombreJugador)throws RemoteException;

    void nuevoJuego() throws RemoteException;

    //void cerrarJuego() throws RemoteException;

    ArrayList<IJugador> obtenerJugadoresPorPuntos(ArrayList<IJugador> jugadores) throws RemoteException;

    void eliminarJugador(String nombreJugador) throws RemoteException;

    boolean isJefeMesa(String nombreJugador) throws RemoteException, JugadorInexistente;

    void modificarCompetitivo() throws RemoteException;

    boolean getEstadoCompetitivo() throws RemoteException;

    void cambiarTiempoTurno(int tiempoEstablecido) throws RemoteException;

    int getCantidadTiempoTurno() throws RemoteException;

    String getNombreJefeMesa() throws RemoteException;
    //boolean esAnfitrion(String nombreJugador);

    //IRummy getInstancia();
}
