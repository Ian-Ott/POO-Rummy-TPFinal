package ar.edu.unlu.poo.ventana;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.Carta;
import ar.edu.unlu.poo.modelo.ICarta;
import ar.edu.unlu.poo.modelo.ITapete;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class VistaConsola implements IVista{
    private JFrame frame;
    private JPanel panelConsola;
    private JTextArea txtAreaMuestra;
    private JTextField txtConsola;
    private JButton enterButton;

    private Controlador controlador;
    private boolean jugadorAgregado = false;
    private boolean primerasOpciones = false;//nombre temporal

    private boolean seleccionCartas = false;
    private boolean posibleRummy = false;
    private boolean posibleEscalera = false;
    private boolean posibleCombinacion = false;

    private boolean posibleCartaParaJugada = false;
    private boolean finTurno = false;
    private ArrayList<Integer> posicionesSeleccionadas = new ArrayList<>();

    private boolean seleccionJugada = false;

    private boolean jugadasSinVer = false;

    private int posicionJugada;


    public VistaConsola() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame("Rummy Beta - Version 0.0");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setContentPane(panelConsola);
                frame.pack();
                frame.setVisible(true);
                txtAreaMuestra.setEditable(false);
                enterButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            procesarTexto();
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
        });

    }

    private void procesarTexto() throws RemoteException { //achicar metodo
        String textoIngresado = txtConsola.getText().toLowerCase();
        if (controlador.juegoIniciado()){
            if (primerasOpciones){
                if (textoIngresado.equals("1") || textoIngresado.contains("tomar del mazo")){
                    controlador.tomarCartaMazo();
                } else if (textoIngresado.equals("2") || textoIngresado.contains("tomar descartada")) {
                    controlador.tomarCartaDescarte();
                }else {
                    opcionIncorrecta();
                }
            } else if (seleccionCartas) {
                int numero;
                numero = Integer.parseInt(textoIngresado);
                if (numero == 0){
                    seleccionCartas = false;
                    if (posibleRummy){
                        posibleRummy = false;
                        controlador.armarRummy(posicionesSeleccionadas);
                        posicionesSeleccionadas.clear();
                    } else if (posibleEscalera) {
                        posibleEscalera = false;
                        controlador.armarEscalera(posicionesSeleccionadas);
                        posicionesSeleccionadas.clear();
                    } else if (posibleCombinacion) {
                        posibleCombinacion = false;
                        controlador.armarCombinacionIguales(posicionesSeleccionadas);
                        posicionesSeleccionadas.clear();
                    } else if (posibleCartaParaJugada) {
                        posibleCartaParaJugada = false;
                        if (posicionesSeleccionadas.isEmpty()){
                            continuarTurnoActual();
                        }else {
                            controlador.agregarCartasAJugada(posicionesSeleccionadas, posicionJugada);
                        }
                        posicionesSeleccionadas.clear();
                    } else if (finTurno) {
                        finTurno = false;
                        controlador.terminarTurno(posicionesSeleccionadas);
                        posicionesSeleccionadas.clear();
                    }else {
                        //excepcion
                    }
                } else if (numero <= controlador.getCartasSize() && numero > 0) {
                    agregarPosicion(numero - 1);
                    txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nPosicion " + numero + " seleccionada!!!");

                } else {
                    //excepcion
                }
            } else if (seleccionJugada) {
                int numero = textoIngresado.indexOf(controlador.getCartasSize());
                if (numero <= controlador.getCartasSize() && numero > 0){
                    posicionJugada = numero;
                    mostrarSeleccionCartas();
                    posibleCartaParaJugada = true;
                } else if (numero == 0) {
                    continuarTurnoActual();
                }
                seleccionJugada = false;
            } else {
                if (textoIngresado.equals("1")){
                    mostrarSeleccionCartas();
                    posibleRummy = true;
                } else if (textoIngresado.equals("2")) {
                    mostrarSeleccionCartas();
                    posibleEscalera = true;
                }
                else if (textoIngresado.equals("3")) {
                    posibleCombinacion = true;
                    mostrarSeleccionCartas();
                } else if (textoIngresado.equals("4")) {
                    jugadasSinVer = false;
                    mostrarJugadasEnMesa();
                } else if (textoIngresado.equals("5")) {
                    mostrarCantidadCartas();
                } else if (textoIngresado.equals("6")) {

                } else if (textoIngresado.equals("0")) {
                    finTurno = true;
                    terminarTurno();
                }else {
                    opcionIncorrecta();
                }
            }

        }else {
            if (textoIngresado.equals("1") || textoIngresado.contains("iniciar partida")){
                if (controlador.esAnfitrion()){
                    controlador.iniciarJuego();
                }else {
                    opcionIncorrecta();
                }
            }else {
                opcionIncorrecta();
            }
        }
    }

    private void mostrarCantidadCartas() {
    }

    private void agregarPosicion(int numero) {
        if (!posicionesSeleccionadas.contains(numero)){
            posicionesSeleccionadas.add(numero);
        }else {
            JOptionPane.showMessageDialog(null, "posicion ya seleccionada!!!");
        }
    }

    private void mostrarSeleccionCartas() throws RemoteException {
        mostrarCartas();
        txtAreaMuestra.setText(txtAreaMuestra.getText() +
                "\n seleccione las cartas segun su posicion (empezando por la 1) \nuna vez finalizado presione 0 para continuar");
        seleccionCartas = true;
    }

    public void opcionIncorrecta(){
        JOptionPane.showMessageDialog(null, "Opcion incorrecta!!!");
    }

    @Override
    public void iniciarVentana(String nombreJugador, boolean b) throws RemoteException {

    }

    private void mostrarMenu() {
        limpiarPantalla();
        txtAreaMuestra.setText("----------------------------------------------------------" +
                "\n1-hacer Rummy" +
                "\n2-hacer escalera" +
                "\n3-hacer combinaciones de numeros iguales" +
                "\n4-ver jugadas en mesa / agregar carta a una jugada" +
                "\n5-ver cartas restantes jugadores" +
                "\n6-Opciones de mesa (solo disponible para el jefe de mesa)" +
                "\n0-terminar turno" +
                "\n----------------------------------------------------------");
    }

    @Override
    public void actualizarCarta(Carta cambio) {

    }

    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void pantallaEspera(boolean anfitrion) throws InterruptedException, RemoteException {
        if (!jugadorAgregado){
            jugadorAgregado = true;
            controlador.nuevoJugador(anfitrion);
        }else {
            limpiarPantalla();
            if (!anfitrion){
                mostrarEspera();
            }else {
                mostrarEsperaAnfitrion();
            }
        }
    }

    private void mostrarEspera() throws RemoteException {
        txtAreaMuestra.setText("\n__________________________________________" +
                "\nesperando a que se unan jugadores (se necesitan entre 2-4 jugadores para jugar) " +
                "\nCantidad de jugadores:" + controlador.cantJugadores() +
                "\n__________________________________________");
    }

    private void mostrarEsperaAnfitrion() throws RemoteException {
        txtAreaMuestra.setText("__________________________________________" +
                "\nesperando a que se unan jugadores (se necesitan entre 2-4 jugadores para jugar) " +
                "\nCantidad de jugadores:" + controlador.cantJugadores() +
                "\nCuando este la cantidad de jugadores necesaria seleccione la opcion:" +
                "\n1-Iniciar Partida" +
                "\n__________________________________________");
    }

    private void limpiarPantalla(){
        txtAreaMuestra.setText(" ");
    }

    @Override
    public void actualizarCantJugadores() throws RemoteException, InterruptedException {
        pantallaEspera(controlador.esAnfitrion());
    }

    @Override
    public void cerrarPantallaEspera() {

    }

    private void mostrarJugadasEnMesa() throws RemoteException {
        ITapete jugadasEnMesa = controlador.obtenerJugadas();
        txtAreaMuestra.setText("\n");
        txtAreaMuestra.setText(txtAreaMuestra.getText() + jugadasEnMesa);
        txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nSeleccione la jugada que quiera con un numero (empezando con el de arriba que es la 1) y sino presione 0 para cancelar");
        seleccionJugada = true;
    }
    private void terminarTurno() throws RemoteException {
        txtAreaMuestra.setText("Para finalizar su turno, seleccione una carta para descartar (en el caso de que no tenga cartas escriba un 0)");
        mostrarSeleccionCartas();
    }




    @Override
    public void iniciarTurno() throws RemoteException {
        primerasOpciones = true;
        mostrarPrimerasOpciones();
        txtConsola.setEnabled(true);
    }

    private void mostrarPrimerasOpciones() throws RemoteException {
        limpiarPantalla();
        txtAreaMuestra.setText("----------------------------------------------------------" +
                "\n1-tomar Carta del mazo" +
                "\n2-tomar carta descartada" +
                "\nCarta disponible en la pila de descartes:" + controlador.getCartaDescarte()+
                "\nTus cartas:\n");
        mostrarCartas();
        if (jugadasSinVer){
            txtAreaMuestra.setText(txtAreaMuestra.getText() +
                    "Hay nuevas jugadas disponibles en la mesa!!!");
        }
    }


    private void mostrarCartas() throws RemoteException {
        ArrayList<ICarta> cartasActuales = controlador.obtenerCartas();
        txtAreaMuestra.setText(txtAreaMuestra.getText() + "\n");
        for (int i = 0; i < cartasActuales.size(); i++) {
            txtAreaMuestra.setText(txtAreaMuestra.getText() + cartasActuales.get(i));
            if (i == 10 || i == 20){
                txtAreaMuestra.setText(txtAreaMuestra.getText() + "\n");
                //esto sirve para que no pase el tamaño de la consola
            }
        }
    }

    @Override
    public void esperarTurno() throws RemoteException {
        limpiarPantalla();
        if (!controlador.esTurnoJugador()){
            txtAreaMuestra.setText("______________________________________________" +
                    "\nHa iniciado un nuevo turno, pero no es suyo. Espere su siguiente turno..." +
                    "\n______________________________________________");
            txtConsola.setEnabled(false);
        }
    }

    @Override
    public void darControl() throws RemoteException {

    }

    @Override
    public void actualizarCartas(ArrayList<ICarta> cartasJugador) {
        /*for (int i = 0; i < cartasJugador.size(); i++) {
            txtAreaMuestra.setText("\n" + cartasJugador.get(i));
        }*/
    }

    @Override
    public void nuevoTurno() throws RemoteException {
        if (controlador.esTurnoJugador()){
            iniciarTurno();
        }else {
            esperarTurno();
        }
    }

    @Override
    public void continuarTurnoActual() throws RemoteException {
        if (controlador.esTurnoJugador()){
            primerasOpciones = false;
            limpiarPantalla();
            mostrarMenu();
            mostrarCartas();
        }
    }

    @Override
    public void finalizarPartida() {
        limpiarPantalla();
        txtAreaMuestra.setText("La partida ha finalizado!!! El ganador es..." +
                " con x puntos");
        mostrarTablaPosiciones();
    }

    @Override
    public void actualizarJugadas() throws RemoteException {
        jugadasSinVer = true;
        continuarTurnoActual();
    }

    private void mostrarTablaPosiciones() {
        txtAreaMuestra.setText(txtAreaMuestra.getText() +
                "\nTabla de posiciones:");
    }
}
