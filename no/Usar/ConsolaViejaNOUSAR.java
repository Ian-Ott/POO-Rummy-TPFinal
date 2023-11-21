package no.Usar;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.Carta;
import ar.edu.unlu.poo.modelo.ICarta;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsolaViejaNOUSAR {
    private Controlador controlador;

    private boolean jugadorAgregado = false;
    private Scanner sc;
    private boolean escanerEnUso = false;


    public void iniciarVentana(String nombreJugador, boolean b) throws RemoteException {
        int opcion = 1;
        int opcionCarta;
        Scanner sc = new Scanner(System.in);
        if (!controlador.esTurnoJugador()){
            esperarTurno();
        }
        while(opcion != 0){
            mostrarCartas();
            mostrarMenu();
            opcion = sc.nextInt();
        switch (opcion) {
            case 0:
                opcionCarta = seleccionarCarta();
                //controlador.terminarTurno(nombreJugador, controlador.obtenerCartas().get(opcionCarta - 1));
                esperarTurno();
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

    public void esperarTurno() throws RemoteException {
        if (!controlador.esTurnoJugador()){
            System.out.println("______________________________________________");
            System.out.println("Ha iniciado un nuevo turno, pero no es suyo. Espere su siguiente turno...");
        }
    }


    public void darControl() throws RemoteException {
        if (controlador.esAnfitrion()){
            mostrarEsperaAnfitrion();
        }
    }

    public void actualizarCartas(ArrayList<ICarta> cartasJugador) {

    }

    public void nuevoTurno() throws RemoteException {

    }


    public void continuarTurnoActual() throws RemoteException {

    }


    public void finalizarPartida() {

    }


    public void actualizarJugadas() {

    }


    public void cerrarPartida() {

    }

    private void mostrarCartas() throws RemoteException {
        ArrayList<ICarta> cartasActuales = controlador.obtenerCartas();
        System.out.println("\nTus Cartas en mano:");
        System.out.println("__________________________________________________________");
        ICarta cartaAux;
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


    public void actualizarCarta(Carta cambio) {

    }

    public void iniciarTurno() throws RemoteException {
        if (controlador.esTurnoJugador()){
            iniciarVentana(controlador.getNombreJugador(), controlador.esAnfitrion());
            //cambiarlo por mostrar opciones turno o algo parecido y en ventana tambien
        }
    }





    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }


    public void pantallaEspera(boolean anfitrion) throws InterruptedException, RemoteException {
        if (!jugadorAgregado){
            jugadorAgregado = true;
            controlador.nuevoJugador(anfitrion);
        }else {
            if (!anfitrion){
                mostrarEspera();
            }
        }
    }

    private void mostrarEspera() throws RemoteException {
        System.out.println("__________________________________________");
        System.out.println("esperando a que se unan jugadores (se necesitan entre 3-4 jugadores para jugar) \nCantidad de jugadores:" + controlador.cantJugadores());
        System.out.println("__________________________________________");
    }

    private void mostrarEsperaAnfitrion() throws RemoteException {
        if (sc != null){
            sc.reset();
        }
        sc = new Scanner(System.in);
        int opcion = 0;
        mostrarMenuAnfitrion();
        while(opcion != 1){
            opcion = sc.nextInt();
            escanerEnUso = true;
            if (opcion != 1){
                mostrarMenuAnfitrion(); //vuelvo a invocar el metodo porque si utilizo un while
                // todos los observadores no son avisados cuando hay un cambio
                System.out.println("Opcion incorrecta!!!!! Solo seleccione la opcion 1 cuando este todo listo.");
                escanerEnUso = false;
            }else {
                escanerEnUso = false;
                System.out.println("Iniciando juego....");
                controlador.iniciarJuego();
            }
        }
    }

    private void mostrarMenuAnfitrion() throws RemoteException {

    }


    public void actualizarCantJugadores() throws RemoteException, InterruptedException {
        if (!controlador.esAnfitrion()){
            mostrarEspera();
        }
    }


    public void cerrarPantallaEspera() {

    }






    private int seleccionarCarta() throws RemoteException {
        int opcion = 0;
        mostrarCartas();
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
