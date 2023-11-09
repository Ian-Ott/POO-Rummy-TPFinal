package ar.edu.unlu.poo.ventana;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.Carta;
import ar.edu.unlu.poo.modelo.Palo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class VistaConsola implements IVista{
    private final JFrame frame;
    private JPanel panelConsola;
    private JTextArea txtAreaMuestra;
    private JTextField txtConsola;
    private JButton enterButton;

    private Controlador controlador;
    private boolean jugadorAgregado = false;

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

    private void procesarTexto() throws RemoteException {
        String textoIngresado = txtConsola.getText().toLowerCase();
        if (controlador.juegoIniciado()){

        }else {
            if (textoIngresado.equals("1") || textoIngresado.equals("iniciar partida")){
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

    public void opcionIncorrecta(){
        JOptionPane.showMessageDialog(null, "Opcion incorrecta!!!");
    }

    @Override
    public void iniciarVentana(String nombreJugador, boolean b) throws RemoteException {

    }

    private void mostrarMenu() {
        limpiarPantalla();
        txtAreaMuestra.setText("----------------------------------------------------------" +
                "\n1-ver cartas en mano" +
                "\n2-crear jugada" +
                "\n3-tomar carta del mazo" +
                "\n4-ver carta en mesa/tomar carta en mesa" +
                "\n5-ver jugadas en mesa / agregar carta a una jugada" +
                "\n6-ver cartas restantes jugadores" +
                "\n9-Opciones de mesa" +
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
                "\nesperando a que se unan jugadores (se necesitan entre 3-4 jugadores para jugar) " +
                "\nCantidad de jugadores:" + controlador.cantJugadores() +
                "\n__________________________________________");
    }

    private void mostrarEsperaAnfitrion() throws RemoteException {
        txtAreaMuestra.setText("__________________________________________" +
                "\nesperando a que se unan jugadores (se necesitan entre 3-4 jugadores para jugar) " +
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
        mostrarMenu();
    }

    @Override
    public void esperarTurno() throws RemoteException {
        limpiarPantalla();
        if (!controlador.esTurnoJugador()){
            txtAreaMuestra.setText("______________________________________________" +
                    "\nHa iniciado un nuevo turno, pero no es suyo. Espere su siguiente turno..." +
                    "\n______________________________________________");
        }
    }

    @Override
    public void darControl() throws RemoteException {

    }

    @Override
    public void actualizarCartas(ArrayList<Carta> cartasJugador) {
        for (int i = 0; i < cartasJugador.size(); i++) {
            txtAreaMuestra.setText("\n" + cartasJugador.get(i));
        }
    }

    @Override
    public void nuevoTurno() throws RemoteException {
        if (controlador.esTurnoJugador()){
            iniciarTurno();
        }else {
            esperarTurno();
        }
    }
}
