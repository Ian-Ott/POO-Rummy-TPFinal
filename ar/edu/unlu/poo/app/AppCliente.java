package ar.edu.unlu.poo.app;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;


import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.IVista;
import ar.edu.unlu.poo.vistas.VistaConsola;
import ar.edu.unlu.poo.vistas.VistaGrafica;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;

public class AppCliente {
    AppServidor server;
    public static void main(String[] args) throws RemoteException, InterruptedException, RMIMVCException {
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
        ArrayList<String> vistasDisponibles = new ArrayList<>();
        vistasDisponibles.add("Consola");
        vistasDisponibles.add("Ventana Grafica");
        String visualizacion = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la vista que quiere para ver el juego", "Visualizacion del juego",
                JOptionPane.QUESTION_MESSAGE,
                null,
                vistasDisponibles.toArray(),
                null
        );
        try {
            IVista vista = null;
            Controlador controlador;
            if (visualizacion.equals("Ventana Grafica")){
                 vista = new VistaGrafica();
            } else{
                vista = new VistaConsola();
            }
            controlador = new Controlador(vista);
            vista.setControlador(controlador);
            Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
            c.iniciar(controlador);
            if (!controlador.juegoIniciado()){
                //controlador.nuevoJugador(nuevoJugador);
                if(!controlador.juegoIniciado() && controlador.cantJugadores() < 4){
                    /*if (controlador.primerJugador()){
                        controlador.setAnfitrion(true);
                        vista.pantallaEspera();
                    }else {
                        controlador.setAnfitrion(false);
                        vista.pantallaEspera();
                    }*/
                    vista.pantallaEspera();
                }else {
                    System.out.println("ERROR: maximo de jugadores alcanzado");
                    System.exit(0);
                }
            } else if (controlador.publicoPermitido()) {
                vista.activarSoloChat();
            } else {
                System.out.println("ERROR: El juego ya fue iniciado");
                System.exit(0);
            }

            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (RMIMVCException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


    }
}
