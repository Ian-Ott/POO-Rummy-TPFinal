package ar.edu.unlu.poo.app;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;


import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.ventana.IVista;
import ar.edu.unlu.poo.ventana.VistaConsola;
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
        vistasDisponibles.add("Ventana Grafica");
        vistasDisponibles.add("Consola");
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
                 //vista = new Ventana();
            } else if (visualizacion.equals("Consola")) {
                vista = new VistaConsola();
            }
            controlador = new Controlador(vista);
            assert vista != null;
            vista.setControlador(controlador);
            //agregar jugador al rummy
            Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
            //vista.iniciarVentana();

                c.iniciar(controlador);
            if (!controlador.juegoIniciado()){
                //controlador.nuevoJugador(nuevoJugador);
                if(!controlador.juegoIniciado() && controlador.cantJugadores() < 4){
                    if (controlador.primerJugador()){
                        controlador.setAnfitrion(true);
                        vista.pantallaEspera(controlador.esAnfitrion());
                    }else {
                        controlador.setAnfitrion(false);
                        vista.pantallaEspera(controlador.esAnfitrion());
                    }
                }else {
                    System.out.println("ERROR: maximo de jugadores alcanzado");
                    System.exit(0);
                }
            }else {
                System.out.println("ERROR: El juego ya fue iniciado");
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
