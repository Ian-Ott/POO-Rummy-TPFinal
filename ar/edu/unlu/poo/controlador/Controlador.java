package ar.edu.unlu.poo.controlador;

import ar.edu.unlu.poo.modelo.*;
import ar.edu.unlu.poo.ventana.IVista;
import ar.edu.unlu.poo.ventana.VistaConsola;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Controlador implements IControladorRemoto {
    private IRummy rummy;
    private IVista vista;
    //Rummy modelo = new Rummy();
    private ArrayList<Carta> cartasActuales = new ArrayList<>();
    private String nombreJugador;
    private boolean anfitrion;
    public Controlador(IVista vista) throws RemoteException {
        this.vista = vista;
    }

    public void crearJugada(Jugador jugadorActual) throws RemoteException{
        boolean exito = false;
        if (cartasActuales.size() == 3){
            exito = rummy.crearJugada(cartasActuales.get(0), cartasActuales.get(1), cartasActuales.get(2));
        } else if (cartasActuales.size() == 4) {
            exito = rummy.crearJugada(cartasActuales.get(0), cartasActuales.get(1), cartasActuales.get(2), cartasActuales.get(3));
        }
        if (exito){
            for (int i = 0; i < cartasActuales.size(); i++){
                jugadorActual.usarCarta(cartasActuales.remove(i));
            }
        }else {
            //elimino las cartas elegidas asi se pueden seleccionar nuevas
            cartasActuales = new ArrayList<>();
        }
    }

    public void cartaSeleccionada(Carta carta){
        if (cartasActuales.size() <= 4){
            cartasActuales.add(carta);
        }
    }

    /*public void agregameComoObservador(Observer observador){
        rummy.addObserver(observador);
    }*/

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.rummy = (IRummy) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto modelo, Object cambio) throws RemoteException {
        try{
            if (cambio.toString().equals("cartas repartidas")){
                obtenerCartas();
            } else if (cambio.equals("juego iniciado")) {
                vista.iniciarVentana(nombreJugador, anfitrion);
            } else if (cambio.equals("nuevo jugador")) {
                vista.actualizarCantJugadores();
            }
            else if (cambio.equals("nuevo turno")){
                vista.nuevoTurno();
            } else if (cambio.equals("continuar turno jugador")) {
                vista.continuarTurnoActual();
            } else if (cambio.equals("jugada agregada")) {
                vista.actualizarJugadas();
            } else if (cambio.equals("fin de partida")) {
                vista.finalizarPartida();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean esAnfitrion() {
        return anfitrion;
    }

    public void setAnfitrion(boolean anfitrion) {
        this.anfitrion = anfitrion;
    }

    public ArrayList<ICarta> obtenerCartas() throws RemoteException {
        ArrayList<Carta> cartasJugadorAux = rummy.getCartasJugador(nombreJugador);
        ArrayList<ICarta> cartasJugador = cambiarTipoCartas(cartasJugadorAux);
        if (vista instanceof VistaConsola){
            vista.actualizarCartas(cartasJugador);
        }
        return  cartasJugador;
    }

    private ArrayList<ICarta> cambiarTipoCartas(ArrayList<Carta> cartasJugadorAux) {
        //pasa a crear un arrayList conteniendo las cartas que recibe pero pasadas a Icarta
        return new ArrayList<>(cartasJugadorAux);
    }

    public int getCartasSize() throws RemoteException {
        return rummy.getCartasJugador(nombreJugador).size();
    }

    public ICarta getCartaDescarte() throws RemoteException {
        return rummy.getCartaBocaArriba();
    }


    public boolean juegoIniciado() throws RemoteException {
        return rummy.isJuegoIniciado();
    }

    public boolean iniciarJuego() throws RemoteException {
        boolean resultado = false;
        if (rummy.getJugadores().size() >= 2){
            rummy.iniciarJuego();
            resultado = true;
        }else {
            if (vista instanceof VistaConsola){
                JOptionPane.showMessageDialog(null,"ERROR: faltan jugadores para iniciar el juego");
            }
            //posible dialogo de mensaje
        }
        return resultado;
    }

    public void nuevoJugador(boolean anfitrion) throws RemoteException {
        String nombreJugador = (String) JOptionPane.showInputDialog("Seleccione su nombre de usuario");
        Jugador nuevoJugador = new Jugador(nombreJugador);
        this.nombreJugador = nombreJugador;
        nuevoJugador.setJefeMesa(anfitrion);
        rummy.agregarJugador(nuevoJugador);
    }

    public boolean primerJugador() throws RemoteException {
        boolean resultado = false;
        if (rummy.getJugadores().isEmpty()){
            resultado = true;
        }
        return resultado;
    }

    public int cantJugadores() throws RemoteException {
        return rummy.getJugadoresSize();
    }

    public ArrayList<String> nombreOponentes(String nombreJugador) {
        try{
            return rummy.getNombreOponentes(nombreJugador);
        }catch(RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    public void terminarTurno(ArrayList<Integer> posicionesSeleccionadas) throws RemoteException {
        if (posicionesSeleccionadas.size() == 1){
            rummy.terminarTurno(posicionesSeleccionadas,nombreJugador);
        } else if (posicionesSeleccionadas.isEmpty()) {
            rummy.finalizarPartida(nombreJugador);
        }
    }

    public boolean esTurnoJugador() throws RemoteException {
        boolean resultado = false;
        if (rummy.getNombreTurnoActual().equals(nombreJugador)){
             resultado = true;
        }
        return resultado;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void tomarCartaMazo() throws RemoteException {
        rummy.sacarCartaMazo(nombreJugador);
    }


    public void tomarCartaDescarte() throws RemoteException {
        rummy.agarrarCartaBocaArriba(nombreJugador);
    }

    public void armarRummy(ArrayList<Integer> posicionesSeleccionadas) throws RemoteException {
        rummy.comprobarRummy(posicionesSeleccionadas, nombreJugador);
    }

    public void armarEscalera(ArrayList<Integer> posicionesSeleccionadas) throws RemoteException {
        rummy.comprobarEscalera(posicionesSeleccionadas, nombreJugador);
    }

    public void armarCombinacionIguales(ArrayList<Integer> posicionesSeleccionadas) throws RemoteException {
        rummy.comprobarCombinacion(posicionesSeleccionadas,nombreJugador);
    }

    public void agregarCartasAJugada(ArrayList<Integer> posicionesSeleccionadas, int posicionJugada) throws RemoteException {
        rummy.agregarCartaAJugada(posicionesSeleccionadas, posicionJugada, nombreJugador);
    }

    public ITapete obtenerJugadas() {
        return rummy.getJugadas();
    }
}
