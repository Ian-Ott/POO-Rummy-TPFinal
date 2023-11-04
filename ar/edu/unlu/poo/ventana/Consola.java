package ar.edu.unlu.poo.ventana;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.Carta;
import ar.edu.unlu.poo.modelo.Palo;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class Consola implements IVista {
    private Controlador controlador;
    private boolean anfitrion;
    private boolean jugadorAgregado = false;

    @Override
    public void iniciarVentana(String nombreJugador, boolean b) throws RemoteException {
        int opcion = 1;
        int opcionCarta;
        Scanner sc = new Scanner(System.in);
        if (!controlador.esTurnoJugador()){
            mostrarEsperaTurno();
        }
        while(opcion != 0){
            mostrarCartas();
            mostrarMenu();
            opcion = sc.nextInt();
        switch (opcion) {
            case 0:
                opcionCarta = seleccionarCarta();
                controlador.terminarTurno(nombreJugador, controlador.obtenerCartas().get(opcionCarta - 1));
                mostrarEsperaTurno();
            case 1:

                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:

                break;
            case 13:
                break;
            default:
                System.out.println("OPCION INCORRECTA: Seleccione un numero entre 0-13");
        }
        }
    }

    private void mostrarEsperaTurno() {
        System.out.println("______________________________________________");
        System.out.println("Espere su siguiente turno...");
    }

    private void mostrarCartas() throws RemoteException {
        ArrayList<Carta> cartasActuales = controlador.obtenerCartas();
        System.out.println("\nTus Cartas en mano:");
        System.out.println("__________________________________________________________");
        Carta cartaAux;
        for (int i = 0; i < cartasActuales.size(); i++) {
            cartaAux = cartasActuales.get(i);
            System.out.print("|" + cartaAux.getNumero() + cartaAux.getPalo());
        }
        System.out.print("|");
        System.out.println("___________________________________________________________");
    }

    public void mostrarMenu() {
        System.out.println("----------------------------------------------------------");
        System.out.println("\t\tRummy v-0.0");
        System.out.println("----------------------------------------------------------");
        System.out.println("1-ver cartas en mano");
        System.out.println("2-crear jugada");
        System.out.println("3-tomar carta del mazo");
        System.out.println("4-ver carta en mesa/tomar carta en mesa");
        System.out.println("5-ver jugadas en mesa / agregar carta a una jugada");
        System.out.println("6-ver cartas restantes jugadores");
        System.out.println("9-Opciones de mesa");
        System.out.println("0-terminar turno");
    }

    @Override
    public void actualizarCarta(Carta cambio) {

    }

    public void nuevoTurno() throws RemoteException {
        if (controlador.esTurnoJugador()){
            iniciarVentana(controlador.getNombreJugador(), anfitrion);
        }
    }
    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void pantallaEspera(boolean anfitrion) throws InterruptedException, RemoteException {
        this.anfitrion = anfitrion;
        if (!jugadorAgregado){
            jugadorAgregado = true;
            controlador.nuevoJugador(anfitrion);
        }
        if (anfitrion) {
            mostrarEsperaAnfitrion();
        }else {
            mostrarEspera();
        }
    }

    private void mostrarEspera() throws RemoteException {
        System.out.println("__________________________________________");
        System.out.println("esperando a que se unan jugadores (se necesitan entre 3-4 jugadores para jugar) \nCantidad de jugadores:" + controlador.cantJugadores());
        System.out.println("__________________________________________");
    }

    private void mostrarEsperaAnfitrion() throws RemoteException {
        int opcion = 0;
        Scanner sc = new Scanner(System.in);
        mostrarMenuAnfitrion();
        while (opcion != 1){
            opcion = sc.nextInt();
            if (opcion != 1){
                mostrarMenuAnfitrion();
                System.out.println("Opcion incorrecta!!!!! Solo seleccione la opcion 1 cuando este todo listo.");
            }else {
                System.out.println("Iniciando juego....");
                controlador.iniciarJuego();
            }
        }
    }

    private void mostrarMenuAnfitrion() throws RemoteException {
        System.out.println("__________________________________________");
        System.out.println("esperando a que se unan jugadores (se necesitan entre 3-4 jugadores para jugar) \nCantidad de jugadores:" + controlador.cantJugadores());
        System.out.println("Cuando este la cantidad de jugadores necesaria seleccione la opcion:");
        System.out.println("1-Iniciar Partida");
        System.out.println("__________________________________________");
    }

    @Override
    public void actualizarCantJugadores() throws RemoteException, InterruptedException {
        pantallaEspera(anfitrion);
    }

    @Override
    public void cerrarPantallaEspera() {

    }

    @Override
    public void agregarCarta(int numero, Palo palo) {

    }

    @Override
    public void agregarCartaOtroJugador(String nombreJugador) {

    }


    private int seleccionarCarta() throws RemoteException {
        int opcion = 0;
        mostrarCartas();
        Scanner sc = new Scanner(System.in);
        while (opcion < 0 || opcion > controlador.getCartasSize()) {
            System.out.println("Seleccione una carta segun la posicion en la que se muestra (empezando por la 1):");
            opcion = sc.nextInt();
            if (opcion < 0 || opcion > controlador.getCartasSize()) {
                mostrarCartas();
                System.out.println("ERROR: Posicion fuera de rango");
                System.out.println("---------------------------------------------------------------");
            }
        }
        return opcion;
    }
}
