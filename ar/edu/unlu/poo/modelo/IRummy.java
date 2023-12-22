package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.exceptions.*;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRummy extends IObservableRemoto {
    void agregarJugador(Jugador nuevoJugador) throws RemoteException;
    boolean isJuegoIniciado() throws RemoteException;
    void repartirCartasJugadores(int cantidadCartas) throws RemoteException;

    void iniciarJuego() throws RemoteException;

    void iniciarNuevaRonda() throws RemoteException;

    void reiniciarEstados() throws RemoteException;

    void sacarCartaMazo(String jugador) throws RemoteException;

    void agarrarCartaBocaArriba(String jugador) throws RemoteException;

    void comprobarRummy(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador) throws RemoteException, FaltanCartas, NoEsJugada, NoPuedeHacerRummy;

    void comprobarCombinacion(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador) throws RemoteException, FaltanCartas, NoEsJugada;

    void mezclarMazo() throws RemoteException;


    void agregarCartaAJugada(ArrayList<Integer> posicionesSeleccionadas, int posicionJugada, String nombreJugador) throws RemoteException, JugadaLLena, NoSeAgregaronAJugada;

    void agregarCartaOrdenada(ArrayList<Carta> jugada, Carta cartaElegida)throws RemoteException;


    void terminarTurno(Integer carta1, String jugadorActual) throws RemoteException, FaltanCartas;

    Jugador buscarJugadorIzquierda(Jugador jugadorActual)throws RemoteException;


    void comprobarEscalera(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador) throws RemoteException, FaltanCartas, NoEsJugada;


    void finalizarPartida(String jugador)throws RemoteException;

    void pedidoAnularPartidaAmistosamente() throws RemoteException;

    void anularPartida(boolean anular) throws RemoteException;

    void contarPuntosPartida() throws RemoteException;


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
    Carta getCartaBocaArriba() throws RemoteException, NoHayCartaBocaArriba;

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

    ArrayList<IJugador> getTablaPosiciones()throws RemoteException;
    //void cerrarJuego() throws RemoteException;

    ArrayList<IJugador> obtenerJugadoresPorPuntos(ArrayList<IJugador> jugadores) throws RemoteException;

    void eliminarJugador(String nombreJugador) throws RemoteException;

    boolean isJefeMesa(String nombreJugador) throws RemoteException, JugadorInexistente;

    void modificarCompetitivo() throws RemoteException;

    boolean getEstadoCompetitivo() throws RemoteException;

    void cambiarTiempoTurno(int tiempoEstablecido) throws RemoteException;

    int getCantidadTiempoTurno() throws RemoteException;

    String getNombreJefeMesa() throws RemoteException;

    ArrayList<IJugador> getIJugadores() throws RemoteException;

    void juegoAutomatico(String nombreJugador) throws RemoteException;

    boolean isJugadorEnAutomatico(String nombreJugador) throws RemoteException;

    void desactivarJuegoAutomatico(String nombreJugador) throws RemoteException;

    int getPuntosJugador(String nombreJugador) throws RemoteException;

    ArrayList<String> obtenerListadoPartidaGuardada() throws RemoteException;

    void sobreescribirPartida(int posicionPartida, String nombrePartidaAGuardar)throws RemoteException;

    void guardarPartida(String nombrePartida)throws RemoteException;

    void cargarPartida(String nombreAnfitrion, int posicionPartida)throws RemoteException;

    boolean isPartidaCargada() throws RemoteException;

    String activarJugadorSiguiente() throws RemoteException;

    void modificarOpcionPublico() throws RemoteException;

    boolean isPublicoPermitido() throws RemoteException;

    void mostrarMensajeEnChat(String txtIngresado) throws RemoteException;

    int cantJugadoresActivos() throws RemoteException;
}
