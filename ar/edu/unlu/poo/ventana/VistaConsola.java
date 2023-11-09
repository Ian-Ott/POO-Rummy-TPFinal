package ar.edu.unlu.poo.ventana;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.Carta;
import ar.edu.unlu.poo.modelo.ICarta;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class VistaConsola implements IVista{
    private final JFrame frame;
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
    private boolean finTurno = false;
    private ArrayList<Integer> posicionesSeleccionadas = new ArrayList<>();


    public VistaConsola() {
        this.frame = new JFrame("Rummy Beta - Version 0.0");
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
                int numero = textoIngresado.indexOf(controlador.getCartasSize());
                if (numero <= controlador.getCartasSize() && numero > 0){
                    agregarPosicion(numero);
                } else if (numero == 0) {
                    if (posibleRummy){
                        controlador.armarRummy(posicionesSeleccionadas);
                    } else if (posibleEscalera) {

                    } else if (posibleCombinacion) {
                    } else if (finTurno) {

                    }
                } else {

                }
            } else {
                if (textoIngresado.equals("1")){
                    seleccionarCartasRummy();
                } else if (textoIngresado.equals("2")) {

                }
                else if (textoIngresado.equals("3")) {

                } else if (textoIngresado.equals("4")) {

                } else if (textoIngresado.equals("5")) {

                }else if (textoIngresado.equals("0")) {

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

    private void agregarPosicion(int numero) {
        if (!posicionesSeleccionadas.contains(numero)){
            posicionesSeleccionadas.add(numero);
        }else {
            JOptionPane.showMessageDialog(null, "posicion ya seleccionada!!!");
        }
    }

    private void seleccionarCartasRummy() throws RemoteException {
        posibleRummy = true;
        mostrarCartas();
        txtAreaMuestra.setText(txtAreaMuestra.getText() +
                "\n seleccione las cartas segun su posicion (empezando por la 1)");
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
        txtAreaMuestra.setText("");
    }

    @Override
    public void actualizarCantJugadores() throws RemoteException, InterruptedException {
        pantallaEspera(controlador.esAnfitrion());
    }

    @Override
    public void cerrarPantallaEspera() {

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
            mostrarMenu();
            mostrarCartas();
        }
    }
}
