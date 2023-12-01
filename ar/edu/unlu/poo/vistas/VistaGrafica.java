package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.*;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class VistaGrafica implements IVista{
    private JFrame frame;
    private JTabbedPane tabbedPane1;
    private JList listaAbajo;
    private JPanel panelVentana;
    private JList listaDerecha;
    private JList listaIzquierda;
    private JList listaArriba;
    private JCheckBox modoExpresCheckBox;
    private JCheckBox modoPorPuntosCheckBox;
    private JCheckBox CheckBoxChat;
    private JComboBox comboBox1;
    private JTextField seleccioneLasOpcionesDeTextField;
    private JTextField txtInfoInicio;
    private JButton iniciarPartidaButton;
    private JProgressBar cantidadJugadoresBar;
    private JCheckBox a;
    private DefaultListModel<String> listaModelo;
    private Controlador controlador;

    public VistaGrafica() throws RemoteException {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(panelVentana);
        frame.pack();
        listaModelo = new DefaultListModel<>();
        listaAbajo.setModel(listaModelo);
        Rummy moddelo = new Rummy();
        moddelo.agregarJugador(new Jugador("a"), true);
        moddelo.agregarJugador(new Jugador("un jugador"), false);
        moddelo.iniciarJuego();
        mostrarResultadosBusqueda(moddelo.getCartasJugador("pepito"));
        frame.setVisible(true);
    }

    public void mostrarResultadosBusqueda(List<Carta> cartasJugador) {
        listaModelo.clear();

        for (Carta carta: cartasJugador) {
            //JLabel a = new JLabel();
            //a.setText(carta.toString());
            listaModelo.addElement(carta.toString());
        }

        //cbLibroDevolucion.removeAllItems();
        /*for (Libro libro: librosEncontrados) {
            if (libro.hayPrestados()) {
                cbLibroDevolucion.addItem(libro);
            }
        }*/
    }

    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void pantallaEspera() {

    }

    @Override
    public void actualizarCantJugadores() {

    }

    @Override
    public void iniciarTurno() {

    }

    @Override
    public void esperarTurno() {

    }

    @Override
    public void actualizarCartas(ArrayList<ICarta> cartasJugador) {

    }

    @Override
    public void nuevoTurno() {

    }

    @Override
    public void continuarTurnoActual() {

    }

    @Override
    public void finalizarPartida() {

    }

    @Override
    public void actualizarJugadas() {

    }

    @Override
    public void cerrarPartida() {

    }

    @Override
    public void mostrarErrorApuesta() {

    }

    @Override
    public void avisarSobreApuesta() {

    }

    @Override
    public void mostrarResultadosPuntos() {

    }

    @Override
    public void finalizarPartidaAmistosamente() {

    }

    @Override
    public void eleccionAnularPartida() {

    }

    @Override
    public void obtenerNombre() {

    }

    @Override
    public void solicitarCerrarVentana() {

    }

    @Override
    public void mostrarTablaPosiciones(ArrayList<IJugador> jugadores) {

    }

    @Override
    public void mostrarErrorConexion() {

    }
}
