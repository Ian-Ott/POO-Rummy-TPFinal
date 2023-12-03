package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class VistaGrafica implements IVista{
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JList listaAbajo;
    private JPanel panelPartida;
    private JList<String> listaDerecha;
    private JList listaIzquierda;
    private JList listaArriba;

    private JCheckBox partidaCompetitivaCheck;
    private JCheckBox modoExpresCheckBox;
    private JCheckBox modoPorPuntosCheckBox;
    private JCheckBox checkBoxChat;
    private JComboBox seleccionTiempo;
    private JTextField seleccioneLasOpcionesDeTextField;
    private JTextArea txtInfoInicio;
    private JButton iniciarPartidaButton;
    private JProgressBar cantidadJugadoresBar;
    private JPanel panelInicio;
    private JTextArea datosJugadorArriba;
    private JTextArea datosJugadorIzquierda;
    private JTextArea datosJugadorActual;
    private JTextArea datosJugadorDerecha;
    private JPanel panelCentral;
    private JList listaJugadas;
    private JPanel panelOpciones;
    private JButton cartaBocaArribaButton;
    private JButton mazoButton;
    private JButton agregarCartaAJugadaButton;
    private JButton terminarTurnoButton;
    private JTextArea txtTurno;
    private JTextArea txtAsistenteAyuda;
    private JCheckBox asistenteCheckBox;
    private JButton cancelarSeleccionButton;
    private JButton agregarNuevaJugadaButton;
    private JComboBox seleccionJugada;
    private JPanel panelAsistente;
    private JTextArea txtNombre;
    private JButton iniciarSesionButton;
    private JPanel panelUsuario;
    private JPanel panelPrincipal;
    private JPanel panelVacio;
    private JCheckBox a;
    private DefaultListModel<String> listaModeloAbajo;
    private DefaultListModel<String> listaModeloArriba;
    private DefaultListModel<String> listaModeloDerecha;
    private DefaultListModel<String> listaModeloIzquierda;
    private Controlador controlador;
    ArrayList<Integer> cartasSeleccionadasPosicion = new ArrayList<>();

    public VistaGrafica() throws RemoteException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame();
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setContentPane(panelPrincipal);
                frame.pack();
                listaModeloAbajo = new DefaultListModel<>();
                listaModeloDerecha = new DefaultListModel<>();
                listaModeloArriba = new DefaultListModel<>();
                listaModeloIzquierda = new DefaultListModel<>();
                listaAbajo.setModel(listaModeloAbajo);
                listaDerecha.setModel(listaModeloDerecha);
                listaIzquierda.setModel(listaModeloIzquierda);
                listaArriba.setModel(listaModeloArriba);
        /*Rummy moddelo = new Rummy();
        moddelo.agregarJugador(new Jugador("a"), true);
        moddelo.agregarJugador(new Jugador("un jugador"), false);
        moddelo.iniciarJuego();
        mostrarResultadosBusqueda(moddelo.getCartasJugador("pepito"));*/
                frame.setVisible(true);
                tabbedPane.remove(panelPartida);
                iniciarPartidaButton.setVisible(false);
                panelUsuario.setVisible(true);
                iniciarSesionButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!txtNombre.getText().isEmpty()) {
                            txtNombre.setVisible(false);
                            iniciarSesionButton.setVisible(false);
                            txtNombre.setEnabled(false);
                            iniciarSesionButton.setEnabled(false);
                            //panelUsuario.setBackground(new Color(17048));
                            //panelUsuario.setBorder(null);
                            //panelUsuario.repaint();
                            ocultarPanelUsuario();
                            panelVacio.setVisible(false);
                            panelUsuario.setVisible(false);
                            panelUsuario.repaint();
                            iniciarPartidaButton.setVisible(true);
                            controlador.nuevoJugador(controlador.esAnfitrion(), txtNombre.getText());
                        }else {
                            txtNombre.setText("");
                            //error en el asistente
                        }
                    }
                });
                iniciarPartidaButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tabbedPane.remove(panelInicio);
                        panelPartida.setToolTipText("Partida");
                        tabbedPane.add(panelPartida,0);
                        controlador.iniciarJuego();
                        tabbedPane.add(panelInicio);
                        tabbedPane.remove(panelPartida);
                    }
                });
                ListSelectionListener listenerCartas = new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        JList<String> cartas = (JList<String>) e.getSource();
                        cartasSeleccionadasPosicion.add(cartas.getSelectedIndex());
                        //verificar si esto funciona y explicarlo
                    }
                };
                listaAbajo.addListSelectionListener(listenerCartas);
                cancelarSeleccionButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cartasSeleccionadasPosicion.clear();
                    }
                });
                agregarNuevaJugadaButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (seleccionJugada.getModel().getSelectedItem().equals("Hacer una Escalera")){
                            controlador.armarEscalera(cartasSeleccionadasPosicion);
                        } else if (seleccionJugada.getModel().getSelectedItem().equals("Combinacion de Cartas Iguales")){
                            controlador.armarCombinacionIguales(cartasSeleccionadasPosicion);
                        } else if (seleccionJugada.getModel().getSelectedItem().equals("Hacer Rummy")) {
                            controlador.armarRummy(cartasSeleccionadasPosicion);
                        }else {
                            //podria setear un texto al asistente
                        }
                    }
                });
                agregarCartaAJugadaButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //controlador.agregarCartasAJugada(cartasSeleccionadasPosicion,);
                        //agregar un checkbox por cada jugada?
                    }
                });
                asistenteCheckBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (asistenteCheckBox.isSelected()){
                            panelAsistente.setVisible(true);
                        }else {
                            panelAsistente.setVisible(false);
                        }
                    }
                });
                terminarTurnoButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controlador.terminarTurno(cartasSeleccionadasPosicion);
                    }
                });
                mazoButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controlador.tomarCartaMazo();
                    }
                });
                cartaBocaArribaButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controlador.tomarCartaDescarte();
                    }
                });
                seleccionTiempo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (seleccionTiempo.getModel().getSelectedItem().equals("60 segundos por turno")) {
                            controlador.setTiempoTurno(60);
                        } else if (seleccionTiempo.getModel().getSelectedItem().equals("120 segundos por turno")) {
                            controlador.setTiempoTurno(120);
                        } else if (seleccionTiempo.getModel().getSelectedItem().equals("Tiempo Por Turnos Desactivado")) {
                            controlador.setTiempoTurno(0);
                        }
                    }
                });
                partidaCompetitivaCheck.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (partidaCompetitivaCheck.isSelected()){
                            controlador.modificarPartidasCompetitivas();
                        }else {
                            controlador.modificarPartidasCompetitivas();
                        }
                    }
                });
                checkBoxChat.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (checkBoxChat.isSelected()){
                            controlador.modificarOpcionChat();
                        }else {
                            controlador.modificarOpcionChat();
                        }
                    }
                });
                modoExpresCheckBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (modoExpresCheckBox.isSelected()){
                            controlador.activarModoExpres();
                            modoPorPuntosCheckBox.setSelected(false);
                        }else {
                            controlador.activarModoPuntos();
                            modoPorPuntosCheckBox.setSelected(true);
                        }
                    }
                });
                modoPorPuntosCheckBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (modoPorPuntosCheckBox.isSelected()){
                            controlador.activarModoPuntos();
                            modoExpresCheckBox.setSelected(false);
                        }else {
                            controlador.activarModoExpres();
                            modoExpresCheckBox.setSelected(true);
                        }
                    }
                });
            }
        });

    }

    private void ocultarPanelUsuario() {
        panelUsuario.setBorder(null);
        panelUsuario.setBackground(new Color(4,18,48));
        panelUsuario.setVisible(false);
    }

    private void asignarNombresJugadores() {
        //si un jugador es eliminado deberia de llamar a esta funcion para cambiar los nombres
        //hacer un posible limpiar datos de texto?
        if (controlador.esAnfitrion()){
            datosJugadorActual.setText(controlador.getNombreJugador() + "*");
        }else {
            datosJugadorActual.setText(controlador.getNombreJugador());
        }
        ArrayList<String> oponentes = controlador.nombreOponentes(controlador.getNombreJugador());
        String oponenteActual;
        for (int i = 0; i < controlador.cantJugadores() - 1;i++){
            oponenteActual = verificarAnfitrion(oponentes.get(i));
            if (i == 0){
                datosJugadorDerecha.setText(oponenteActual);
            } else if (i == 1) {
                datosJugadorArriba.setText(oponenteActual);
            } else if (i == 2) {
                datosJugadorIzquierda.setText(oponenteActual);
            }
        }
    }

    private String verificarAnfitrion(String oponenteActual) {
        //IDEA:si el jugador contiene una estrella de string entonces le agrego una imagen con forma de estrella a ese jugador
        // y luego borro la estrella de string
        if (controlador.getNombreAnfitrion().equals(oponenteActual)){
            oponenteActual += "*";
        }
        return oponenteActual;
    }

    private String obtenerNombreAnfitrion() {
        return controlador.getNombreAnfitrion();
    }

   /* public void mostrarResultadosBusqueda(List<Carta> cartasJugador) {


        //cbLibroDevolucion.removeAllItems();
        for (Libro libro: librosEncontrados) {
            if (libro.hayPrestados()) {
                cbLibroDevolucion.addItem(libro);
            }
        }
    }*/
    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void pantallaEspera() {
        obtenerNombre();
        panelInicio.setVisible(true);
        if (controlador.esAnfitrion()){
            txtInfoInicio.setText("Esperando a que se unan jugadores (se necesitan entre 2-4 jugadores para empezar a jugar) " +
                    "\nCantidad de jugadores:" + controlador.cantJugadores() +
                    "\nCuando este la cantidad necesaria presione el boton para iniciar.");
            actualizarBarra();
        }else {
            txtInfoInicio.setText("\nesperando a que se unan jugadores (se necesitan entre 2-4 jugadores para empezar a jugar) " +
                    "\nCantidad de jugadores:" + controlador.cantJugadores());
            actualizarBarra();
            iniciarPartidaButton.setVisible(false);
        }
    }

    private void actualizarBarra() {
        cantidadJugadoresBar.setMaximum(4);
        cantidadJugadoresBar.setMinimum(0);
        cantidadJugadoresBar.setValue(controlador.cantJugadores());
        cantidadJugadoresBar.setString("Jugadores");
    }

    @Override
    public void actualizarCantJugadores() {
        pantallaEspera();
    }

    @Override
    public void iniciarTurno() {
        asignarNombresJugadores();
        if (controlador.esTurnoJugador()){
            txtTurno.setText("Bienvenido, " + controlador.getNombreJugador() +". Es su turno");
            listaAbajo.setEnabled(true);
            mazoButton.setEnabled(true);
            cartaBocaArribaButton.setEnabled(true);
            agregarCartaAJugadaButton.setEnabled(true);
            terminarTurnoButton.setEnabled(true);
        }else {
            esperarTurno();
        }
    }

    @Override
    public void esperarTurno() {
        txtTurno.setText("Espere su turno. Ahora mismo es el turno de " + controlador.getNombreTurnoActual());
        listaAbajo.setEnabled(false);
        mazoButton.setEnabled(false);
        cartaBocaArribaButton.setEnabled(false);
        agregarCartaAJugadaButton.setEnabled(false);
        terminarTurnoButton.setEnabled(false);
    }

    @Override
    public void actualizarCartas(ArrayList<ICarta> cartasJugador) {

    }

    @Override
    public void nuevoTurno() {
        actualizarCartas();
    }

    private void actualizarCartas() {
        ArrayList<String> oponentes = controlador.nombreOponentes(controlador.getNombreJugador());
        String oponenteActual;
        for (int i = 0; i < controlador.cantJugadores() - 1;i++){
            oponenteActual = oponentes.get(i);
            if (i == 0){
                actualizarCartasJugadorDerecha(controlador.cantCartasOponente(oponenteActual));
            } else if (i == 1) {
                actulizarCartasJugadorArriba(controlador.cantCartasOponente(oponenteActual));
            } else if (i == 2) {
                actualizarCartasJugadorIzquierda(controlador.cantCartasOponente(oponenteActual));
            }
        }
        actualizarCartasJugadorActual();
    }

    private void actualizarCartasJugadorActual() {
        ArrayList<ICarta> cartasJugador = controlador.obtenerCartas();
        listaModeloAbajo.clear();
        for (ICarta carta: cartasJugador) {
            //JLabel a = new JLabel();
            //a.setText(carta.toString());
            listaModeloAbajo.addElement(carta.toString());
        }
    }

    private void actualizarCartasJugadorIzquierda(int cantidadCartas) {
        listaModeloIzquierda.clear();
        for (int i = 0; i < cantidadCartas; i++) {
            listaModeloIzquierda.addElement("|carta dada vuelta|");
        }
    }

    private void actulizarCartasJugadorArriba(int cantidadCartas) {
        listaModeloArriba.clear();
        for (int i = 0; i < cantidadCartas; i++) {
            listaModeloArriba.addElement("|carta dada vuelta|");
        }
    }

    private void actualizarCartasJugadorDerecha(int cantidadCartas) {
        listaModeloDerecha.clear();
        for (int i = 0; i < cantidadCartas; i++) {
            listaModeloDerecha.addElement("|carta dada vuelta|");
        }
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
        panelUsuario.setVisible(true);
        txtNombre.setEnabled(true);
        iniciarSesionButton.setEnabled(true);
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
