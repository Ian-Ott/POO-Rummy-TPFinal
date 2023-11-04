package ar.edu.unlu.poo.controlador;

import ar.edu.unlu.poo.modelo.*;
import ar.edu.unlu.poo.ventana.Consola;
import ar.edu.unlu.poo.ventana.IVista;
import ar.edu.unlu.poo.ventana.Ventana;
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
            }else if (cambio.equals("juego iniciado")) {
            vista.iniciarVentana(nombreJugador, esAnfitrion());
        } else if (cambio.equals("nuevo jugador")) {
                vista.actualizarCantJugadores();
            } else if (cambio.equals("nuevo turno")){
                vista.nuevoTurno();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Carta> obtenerCartas() throws RemoteException {
        ArrayList<Carta> cartasJugador = rummy.getCartasJugador(nombreJugador);
        Carta cartaAux;
        if (vista instanceof Ventana){
            for (int i = 0; i < cartasJugador.size(); i++) {
                cartaAux = cartasJugador.get(i);
                vista.agregarCarta(cartaAux.getNumero(), cartaAux.getPalo());
            }
        }
        return cartasJugador;
    }

    public int getCartasSize() throws RemoteException {
        return rummy.getCartasJugador(nombreJugador).size();
    }

    private boolean esAnfitrion() {
        return anfitrion;
    }

    public boolean juegoIniciado() throws RemoteException {
        return rummy.isJuegoIniciado();
    }

    public boolean iniciarJuego() throws RemoteException {
        boolean resultado = false;
        if (rummy.getJugadores().size() >= 3){
            rummy.iniciarJuego();
            resultado = true;
        }else {
            if (vista instanceof Consola){
                System.out.println("ERROR: faltan jugadores para iniciar el juego");
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

    public void terminarTurno(String nombreJugador, Carta carta) throws RemoteException {
        rummy.terminarTurno(carta,nombreJugador);
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
}
