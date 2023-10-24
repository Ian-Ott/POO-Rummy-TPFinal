package ar.edu.unlu.poo.app;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;


import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.IRummy;
import ar.edu.unlu.poo.modelo.Jugador;
import ar.edu.unlu.poo.modelo.Rummy;
import ar.edu.unlu.poo.ventana.IVista;
import ar.edu.unlu.poo.ventana.Ventana;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;

public class AppCliente {
    public static void main(String[] args) throws RemoteException {
        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchará peticiones el cliente", "IP del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchará peticiones el cliente", "Puerto del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                9999
        );
        String ipServidor = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la corre el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );
        String portServidor = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que corre el servidor", "Puerto del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                8888
        );
        IRummy modelo = Rummy.getInstancia();
        IVista vista = new Ventana();
        Controlador controlador = new Controlador(vista, modelo);
        vista.setControlador(controlador);
        String nombreJugador = (String) JOptionPane.showInputDialog("Seleccione su nombre de usuario");
        if (!controlador.juegoIniciado()){
            //crear nuevo jugador
            Jugador nuevoJugador = new Jugador(nombreJugador);
            //agregar jugador al rummy
            modelo.agregarJugador(nuevoJugador);
            Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
            //tal vez no deberia ser un while
            while (!controlador.juegoIniciado()){
                vista.pantallaEspera(false, modelo.getJugadores().size());
            }
            vista.iniciarVentana();
            try {
                c.iniciar(controlador);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (RMIMVCException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else {
            System.out.println("ERROR: El juego ya fue iniciado");
        }
    }
}
