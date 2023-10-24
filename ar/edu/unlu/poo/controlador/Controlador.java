package ar.edu.unlu.poo.controlador;

import ar.edu.unlu.poo.modelo.Carta;
import ar.edu.unlu.poo.modelo.IRummy;
import ar.edu.unlu.poo.modelo.Jugador;
import ar.edu.unlu.poo.modelo.Observer;
import ar.edu.unlu.poo.ventana.IVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Controlador implements IControladorRemoto {
    IRummy rummy;
    IVista vista;
    ArrayList<Carta> cartasActuales = new ArrayList<>();
    public Controlador(IVista vista, IRummy modelo){
        this.rummy = modelo;
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
        if (cambio instanceof Carta){
            cartaSeleccionada((Carta) cambio);
            vista.actualizarCarta((Carta) cambio);
        }
    }

    public boolean juegoIniciado() throws RemoteException {
        return rummy.isJuegoIniciado();
    }

    public void iniciarJuego() throws RemoteException {
        if (rummy.getJugadores().size() >= 3){
            rummy.iniciarJuego();
        }else {
            //posible dialogo de mensaje
        }
    }
}
