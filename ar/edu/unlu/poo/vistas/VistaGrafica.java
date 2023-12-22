package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.exceptions.NoHayCartaBocaArriba;
import ar.edu.unlu.poo.modelo.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class VistaGrafica implements IVista{
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JList<ImageIcon> listaAbajo;
    private JPanel panelPartida;
    private JList<ImageIcon> listaDerecha;
    private JList<ImageIcon> listaIzquierda;
    private JList<ImageIcon> listaArriba;

    private JCheckBox partidaCompetitivaCheck;
    private JCheckBox modoExpresCheckBox;
    private JCheckBox modoPorPuntosCheckBox;
    private JCheckBox checkBoxChat;
    private JComboBox seleccionTiempo;
    private JTextField txtOpcionMesa;
    private JTextArea txtInfoInicio;
    private JButton iniciarPartidaButton;
    private JProgressBar cantidadJugadoresBar;
    private JPanel panelInicio;
    private JTextPane datosJugadorArriba;
    private JTextPane datosJugadorIzquierda;
    private JTextPane datosJugadorActual;
    private JTextPane datosJugadorDerecha;
    private JPanel panelCentral;
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
    private JTextArea txtNombre;
    private JButton iniciarSesionButton;
    private JPanel panelUsuario;
    private JPanel panelPrincipal;
    private JPanel panelVacio;
    private JPanel panelAbajo;
    private JPanel panelJugadas;
    private JPanel panelOpcionesMesa;
    private JPanel panelApuesta;
    private JSpinner spinnerApuesta;
    private JButton apostarButton;
    private JButton anularPartidaButton;
    private JButton iniciarNuevaPartidaButton;
    private JButton salirButton;
    private JPanel panelTablaPosiciones;
    private JPanel panelArriba;
    private JPanel panelDerecha;
    private JPanel panelIzquierda;
    private JButton reengancharseButton;
    private JPanel panelFinRonda;
    private JTextArea puntosJugador4;
    private JTextArea puntosJugador3;
    private JTextArea puntosJugador1;
    private JTextArea puntosJugador2;
    private JButton iniciarNuevaRondaButton;
    private JTextArea nombreJugadorPuntos1;
    private JTextArea nombreJugadorPuntos2;
    private JTextArea nombreJugadorPuntos3;
    private JTextArea nombreJugadorPuntos4;
    private JTextArea txtPuntosPartida;
    private JTextArea txtnombreJugadorTablaPosiciones;
    private JTextArea txtPuntosJugadorTablaPosiciones;
    private JTextArea puntosDeXPTextArea;
    private JTextArea nombreTextArea;
    private JPanel txt;
    private JTextArea txtTemporizador;
    private JButton desactivarJuegoAutomaticoButton;
    private JScrollPane scrollpanelAsistente;
    private JPanel panelPartidaGuardada;
    private JList<String> listaPartidaGuardada;
    private DefaultListModel<String> listaModeloPartidaGuardada;
    private JButton sobreescribirPartidaButton;
    private JButton cargarPartidaButton;
    private JButton guardarPartidaButton;
    private JPanel panelChat;
    private JTextArea txtMensajeChat;
    private JTextField txtEscribirChat;
    private JButton enviarButton;
    private JPanel panelBotonesPartidaGuardada;
    private JPanel panelDerechaInicio;
    private JTextField txtNombrePartida;
    private JPanel panelTxtNombrePartida;
    private DefaultListModel<ImageIcon> listaModeloAbajo;
    private DefaultListModel<ImageIcon> listaModeloArriba;
    private DefaultListModel<ImageIcon> listaModeloDerecha;
    private DefaultListModel<ImageIcon> listaModeloIzquierda;
    private Controlador controlador;
    private ArrayList<Integer> cartasSeleccionadasPosicion = new ArrayList<>();
    private ArrayList<JCheckBox> listaCheckJugada;
    private boolean seleccionAnularPartida;
    Timer temporizador;
    Timer tiempoTurno;
    int tiempoRestante;
    TimerTask juegoAutomatico;
    TimerTask mostrarTiempoActual;

    private boolean modoChat;
    private enum EstadoChat{
        OBTENER_NOMBRE, MODO_CHAT
    }

    private EstadoChat estadoActualChat;

    private int partidaSeleccionada = -1;

    public VistaGrafica() throws RemoteException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame();
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setContentPane(panelPrincipal);
                frame.pack();
                tabbedPane.setIconAt(2,new ImageIcon("ar/edu/unlu/poo/images/opcionesMesaIcon.jpg"));
                listaModeloAbajo = new DefaultListModel<>();
                listaModeloDerecha = new DefaultListModel<>();
                listaModeloArriba = new DefaultListModel<>();
                listaModeloIzquierda = new DefaultListModel<>();
                listaModeloAbajo.setSize(30);
                listaAbajo.setModel(listaModeloAbajo);
                listaDerecha.setModel(listaModeloDerecha);
                listaIzquierda.setModel(listaModeloIzquierda);
                listaArriba.setModel(listaModeloArriba);
                listaAbajo.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                listaAbajo.setVisibleRowCount(0);
                listaArriba.setLayoutOrientation(JList.VERTICAL_WRAP);
                listaArriba.setVisibleRowCount(0);
                //listaAbajo.setFixedCellWidth();
                //listaArriba.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                panelAbajo.setLayout(new BoxLayout(panelAbajo, BoxLayout.X_AXIS));
                panelArriba.setLayout(new BoxLayout(panelArriba, BoxLayout.X_AXIS));
                /*JScrollPane scrollPane = new JScrollPane(panelAbajo);
                panelPartida.add(scrollPane, BorderLayout.SOUTH);*/
        /*Rummy moddelo = new Rummy();
        moddelo.agregarJugador(new Jugador("a"), true);
        moddelo.agregarJugador(new Jugador("un jugador"), false);
        moddelo.iniciarJuego();
        mostrarResultadosBusqueda(moddelo.getCartasJugador("pepito"));*/
                frame.setVisible(true);
                tabbedPane.remove(panelPartida);
                tabbedPane.remove(panelOpcionesMesa);
                panelOpcionesMesa.setName("Opciones de Mesa");
                iniciarPartidaButton.setVisible(false);
                panelUsuario.setVisible(true);
                listaCheckJugada = new ArrayList<>();

                spinnerApuesta.setForeground(new Color(24,224,229));
                spinnerApuesta.setBackground(new Color(4,21,80));
                spinnerApuesta.repaint();

                iniciarNuevaPartidaButton.setVisible(false);
                salirButton.setVisible(false);
                seleccionAnularPartida = false;

                reengancharseButton.setEnabled(false);


                tabbedPane.remove(panelTablaPosiciones);
                tabbedPane.remove(panelFinRonda);
                //panelJugadas.setLayout(new BoxLayout(panelJugadas, BoxLayout.LINE_AXIS));
                temporizador = new Timer();
                tiempoTurno = new Timer();
                tiempoRestante = 0;
                txtTemporizador.setEditable(false);

                desactivarJuegoAutomaticoButton.setVisible(false);
                tabbedPane.remove(panelPartidaGuardada);
                tabbedPane.remove(panelChat);
                panelPartidaGuardada.setName("Partidas");
                panelChat.setName("Chat Publico");
                listaModeloPartidaGuardada = new DefaultListModel<>();

                listaPartidaGuardada.setModel(listaModeloPartidaGuardada);
                iniciarSesionButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!txtNombre.getText().isEmpty()) {
                            if (!controlador.estaEnElJuego(txtNombre.getText())){
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
                                cargarPartidaButton.setVisible(true);
                                controlador.nuevoJugador(txtNombre.getText());
                            }else {
                                txtNombre.setText("");
                                errorNombreJugador();
                            }
                        }else {
                            txtNombre.setText("");
                            //error en el asistente
                        }
                    }
                });
                apostarButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if ((int)spinnerApuesta.getValue() == 0){
                            if (controlador.apuestasActivadas()) {
                                controlador.cancelarApuesta();
                            }else {
                                mostrarMensajeAsistente("No hay una apuesta activa.");
                            }
                        } else if ((int)spinnerApuesta.getValue() >= 250 && (int) spinnerApuesta.getValue() <= controlador.cantFichas()) {
                            if (!controlador.apuestasActivadas()) {
                                controlador.apostar((int) spinnerApuesta.getValue());
                            }else {
                                mostrarMensajeAsistente("ya hay una apuesta activa!!");
                            }
                        }else {
                            mostrarMensajeAsistente("La apuesta no esta dentro del rango permitido.");
                        }
                    }
                });
                iniciarPartidaButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controlador.iniciarJuego();
                    }
                });
                ListSelectionListener listenerCartas = new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {

                        JList<ImageIcon> cartas = (JList<ImageIcon>) e.getSource();
                        if (e.getValueIsAdjusting()) {
                            int indiceSeleccionado = cartas.getSelectedIndex();
                            if (indiceSeleccionado != -1 && !cartasSeleccionadasPosicion.contains(indiceSeleccionado)) {
                                System.out.println("seleccionado : " + indiceSeleccionado);
                                String mensajeActual = " Se selecciono la carta " + (indiceSeleccionado + 1);
                                mostrarMensajeAsistente(mensajeActual);
                                cartasSeleccionadasPosicion.add(indiceSeleccionado);
                            }
                        }
                        listaAbajo.clearSelection();
                        //verificar si esto funciona y explicarlo
                    }
                };
                listaAbajo.addListSelectionListener(listenerCartas);
                cancelarSeleccionButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mostrarMensajeAsistente("Se deseleccionaron las cartas.");
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
                        if (jugadasSeleccionadas() == 1){
                            int posicionJugada = obtenerPosicionJugada();
                            controlador.agregarCartasAJugada(cartasSeleccionadasPosicion, posicionJugada);
                        }
                        //controlador.agregarCartasAJugada(cartasSeleccionadasPosicion,);
                        //agregar un checkbox por cada jugada?
                    }
                });
                asistenteCheckBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (asistenteCheckBox.isSelected()){
                            scrollpanelAsistente.setName("Asistente");
                            tabbedPane.add(scrollpanelAsistente);
                        }else {
                            tabbedPane.remove(scrollpanelAsistente);
                        }
                    }
                });
                terminarTurnoButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        guardarPartidaButton.setEnabled(false);
                        if (!cartasSeleccionadasPosicion.isEmpty()) {
                            controlador.terminarTurno(cartasSeleccionadasPosicion);
                        }else {
                            mostrarMensajeAsistente("por favor seleccione una carta antes de terminar el turno.");
                        }
                    }
                });
                mazoButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mazoButton.setEnabled(false);
                        cartaBocaArribaButton.setEnabled(false);
                        cartaBocaArribaButton.setVisible(true);
                        controlador.tomarCartaMazo();
                    }
                });
                cartaBocaArribaButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mazoButton.setEnabled(false);
                        cartaBocaArribaButton.setEnabled(false);
                        controlador.tomarCartaDescarte();
                    }
                });
                anularPartidaButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (seleccionAnularPartida) {
                            anularPartidaButton.setEnabled(false);
                            controlador.tomarDecisionDePartida("Y");
                        }else {
                            controlador.solicitarAnularPartida();
                        }
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
        iniciarNuevaPartidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salirButton.setVisible(false);
                iniciarNuevaPartidaButton.setVisible(false);
                pantallaEspera();
            }
        });
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarJuego();
            }
        });

        iniciarNuevaRondaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.remove(panelFinRonda);
                controlador.iniciarNuevaRonda();
            }
        });
        desactivarJuegoAutomaticoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarMensajeAsistente("Juego Automatico Desactivado!!!");
                desactivarJuegoAutomaticoButton.setVisible(false);
                tabbedPane.remove(panelInicio);
                controlador.desactivarJuegoAutomatico();
            }
        });
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                //sin uso
            }

            @Override
            public void windowClosing(WindowEvent e) {
                controlador.eliminarJugador();
                System.out.println("salio por aca 2");
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                //sin uso
            }

            @Override
            public void windowIconified(WindowEvent e) {
                //sin uso
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                //sin uso
            }

            @Override
            public void windowActivated(WindowEvent e) {
                //sin uso
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                //sin uso
            }
        });
        enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (estadoActualChat.equals(EstadoChat.OBTENER_NOMBRE)){
                    if (!txtEscribirChat.getText().isEmpty()){
                        escribirEnChat("Bienvenido, " + txtEscribirChat.getText() + " Si quieres obtener la lista de comandos solo escribe /ayuda");
                        controlador.nuevoEspectador(txtEscribirChat.getText());
                        estadoActualChat = EstadoChat.MODO_CHAT;
                    }else {
                        escribirEnChat("Error el nombre no es valido");
                    }
                }else {
                    if (txtEscribirChat.getText().equals("/ayuda")){
                        mostrarListaComandos();
                    }else if (txtEscribirChat.getText().equals("/mostrarCartasJugadores")){
                        mostrarNuevoMensaje("Jugador " + controlador.getNombreAnfitrion() + ": " +controlador.cantCartasOponente(controlador.getNombreAnfitrion()));
                        for (String oponenteAnfitrion: controlador.nombreOponentes(controlador.getNombreAnfitrion())) {
                            mostrarNuevoMensaje("Jugador " + controlador.getNombreAnfitrion() + ": " + controlador.cantCartasOponente(oponenteAnfitrion));
                        }
                    } else if (txtEscribirChat.getText().equals("/mostrarJugadas")) {
                        ITapete jugadasEnMesa = controlador.obtenerJugadas();
                        mostrarNuevoMensaje(String.valueOf(jugadasEnMesa));
                    } else if (txtEscribirChat.getText().equals("/mostrarNombreTurnoActual")) {
                        mostrarNuevoMensaje("Jugador con el turno Actual: " + controlador.getNombreTurnoActual());
                    }else {
                        controlador.mostrarMensaje(txtEscribirChat.getText());
                    }
                }
            }
        });

        ListSelectionListener listenerPartidasSeleccionadas = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                JList<String> partidas = (JList<String>) e.getSource();
                if (e.getValueIsAdjusting()) {
                    int indiceSeleccionado = partidas.getSelectedIndex();
                    if (indiceSeleccionado != -1 && partidaSeleccionada != indiceSeleccionado) {
                        System.out.println("seleccionada partida : " + indiceSeleccionado);
                        String mensajeActual = "|-Se selecciono la partida " + (indiceSeleccionado + 1);
                        mostrarMensajeAsistente(mensajeActual);
                        partidaSeleccionada = indiceSeleccionado;
                        cargarPartidaButton.setEnabled(true);
                        sobreescribirPartidaButton.setEnabled(true);
                    }
                }
                listaPartidaGuardada.clearSelection();
                //verificar si esto funciona y explicarlo
            }
        };
        listaPartidaGuardada.addListSelectionListener(listenerPartidasSeleccionadas);
        cargarPartidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (seEncuentra(cargarPartidaButton, panelDerechaInicio)){
                    panelDerechaInicio.remove(cargarPartidaButton);
                    panelBotonesPartidaGuardada.add(cargarPartidaButton);
                    panelBotonesPartidaGuardada.remove(sobreescribirPartidaButton);
                    tabbedPane.add(panelPartidaGuardada);
                    panelBotonesPartidaGuardada.remove(panelTxtNombrePartida);
                    mostrarPartidasGuardadas();
                    cargarPartidaButton.setEnabled(false);
                }else {
                    if (partidaSeleccionada != -1 && partidaSeleccionada < controlador.getPartidasDisponibles().size()) {
                        tabbedPane.remove(panelPartidaGuardada);
                        panelPartidaGuardada.remove(cargarPartidaButton);
                        controlador.cargarPartida(partidaSeleccionada);
                    }
                }
            }
        });
        guardarPartidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (seEncuentra(guardarPartidaButton, panelOpciones)){
                    panelOpciones.remove(guardarPartidaButton);
                    panelBotonesPartidaGuardada.add(guardarPartidaButton);
                    panelBotonesPartidaGuardada.add(sobreescribirPartidaButton);
                    tabbedPane.add(panelPartidaGuardada);
                    panelBotonesPartidaGuardada.add(panelTxtNombrePartida);
                    mostrarPartidasGuardadas();
                    sobreescribirPartidaButton.setEnabled(false);
                }else {
                    if (txtNombrePartida.getText() != null) {
                        tabbedPane.remove(panelPartidaGuardada);
                        panelPartidaGuardada.remove(guardarPartidaButton);
                        controlador.guardarPartida(txtNombrePartida.getText());
                        txtNombrePartida.setText("");
                        panelOpciones.add(guardarPartidaButton);
                    }
                }
            }
        });
        sobreescribirPartidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtNombrePartida.getText() != null) {
                    if (partidaSeleccionada != -1 && partidaSeleccionada < controlador.getPartidasDisponibles().size()) {
                        tabbedPane.remove(panelPartidaGuardada);
                        panelPartidaGuardada.remove(guardarPartidaButton);
                        controlador.sobreescribirPartidaGuardada(0,txtNombrePartida.getText());
                        txtNombrePartida.setText("");
                    }

                }
            }
        });
    }

    private void mostrarPartidasGuardadas() {
        ArrayList<String> partidasDisponibles = controlador.getPartidasDisponibles();
        if (listaModeloPartidaGuardada.size() != partidasDisponibles.size() || listaModeloPartidaGuardada.isEmpty()){
            listaModeloPartidaGuardada.clear();
            for (int i = 0; i < 8; i++) {
                if (i < partidasDisponibles.size()){
                    listaModeloPartidaGuardada.addElement(partidasDisponibles.get(i));
                }else {
                    listaModeloPartidaGuardada.addElement((i + 1) + "- <Espacio disponible para guardar partida>");
                }
            }
        }
    }

    private boolean seEncuentra(JButton botonBuscado, JPanel panelDondeBuscar) {
        Component[] componentes;
        componentes = panelDondeBuscar.getComponents();
        boolean resultado = false;
        for (Component componenteActual:componentes) {
            if (componenteActual.equals(botonBuscado)){
                resultado = true;
            }
        }
        if (!resultado){
            System.out.println("no se encontro el boton");
        }
        return resultado;
    }

    private void mostrarListaComandos() {
            escribirEnChat("_____________________________________________________________");
            escribirEnChat("Lista de Comandos:");
            escribirEnChat("/mostrarCartasJugadores\t/mostrarJugadas\t/mostrarNombreTurnoActual");
            escribirEnChat("-Recuerda usar siempre / cuando quieras escribir un comando");
            escribirEnChat("_____________________________________________________________");
    }

    private void escribirEnChat(String txtIngresado) {
        txtMensajeChat.append(txtIngresado + "\n");
    }

    private void mostrarMensajeAsistente(String txtIngresado) {
        txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() + "\n\n|Fecha:" + LocalDate.now() + "| Hora:" +LocalTime.now()+ "| - " + txtIngresado);
    }

    private int obtenerPosicionJugada() {
        JCheckBox checkBoxActual;
        int posicionJugada = -1;
        for (int i = 0; i < listaCheckJugada.size(); i++) {
           checkBoxActual = listaCheckJugada.get(i);
           if (checkBoxActual.isSelected()){
               posicionJugada = i;
               checkBoxActual.setSelected(false);
           }
        }
        return posicionJugada;
    }

    private int jugadasSeleccionadas() {
        int cantidadJugadasSeleccionadas = 0;
        for (JCheckBox checkBox: listaCheckJugada) {
            if (checkBox.isSelected()){
                cantidadJugadasSeleccionadas++;
            }
        }
        return  cantidadJugadasSeleccionadas;
    }

    private void errorNombreJugador() {
        txtInfoInicio.setText("Error ya hay alguien con ese nombre en el juego y su nombre no debe estar vacio!!!");
        mostrarMensajeAsistente("El nombre no fue aceptado es posible que ese nombre ya se encuentre en el juego o que escribiste un nombre vacio");
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
            datosJugadorActual.setText(controlador.getNombreJugador());
            datosJugadorActual.insertIcon(new ImageIcon("ar/edu/unlu/poo/images/starIcon.jpg"));
        }else {
            datosJugadorActual.setText(controlador.getNombreJugador());
        }
        /*txtAsistenteAyuda.setText(txtTurno.getText() + "\n| Modo " + controlador.getModoJuego() + " |"+
                "\nTus fichas: " + controlador.cantFichas() + " Tu apuesta: " + controlador.getcantidadApostada() +
                "\nCantidad de fichas en el bote de apuestas: " + controlador.getcantidadFichasBote() +
                "\nJugadores Restantes en la partida: " + controlador.getCantDisponibles());*/
        ArrayList<String> oponentes = controlador.nombreOponentes(controlador.getNombreJugador());
        String oponenteActual;
        for (int i = 0; i < controlador.cantJugadores() - 1;i++){
            oponenteActual = verificarAnfitrion(oponentes.get(i));
            if (i == 0){
                cargarDatosJugadorDerecha(oponenteActual);
            } else if (i == 1) {
                cargarDatosJugadorArriba(oponenteActual);
            } else if (i == 2) {
                cargarDatosJugadorIzquierda(oponenteActual);
            }
        }
    }

    private void cargarDatosJugadorIzquierda(String oponenteActual) {
        if (oponenteActual.contains("*")){
            oponenteActual = oponenteActual.replace("*", "");
            datosJugadorIzquierda.setText(" " + oponenteActual);
            datosJugadorIzquierda.insertIcon(new ImageIcon("ar/edu/unlu/poo/images/starIcon.jpg"));
        }else {datosJugadorIzquierda.setText(oponenteActual);}
    }

    private void cargarDatosJugadorArriba(String oponenteActual) {
        if (oponenteActual.contains("*")){
        oponenteActual = oponenteActual.replace("*", "");
        datosJugadorArriba.setText(" " + oponenteActual);
        datosJugadorArriba.insertIcon(new ImageIcon("ar/edu/unlu/poo/images/starIcon.jpg"));
        }else {datosJugadorArriba.setText(oponenteActual);}
    }

    private void cargarDatosJugadorDerecha(String oponenteActual) {
        if (oponenteActual.contains("*")){
            oponenteActual = oponenteActual.replace("*", "");
            datosJugadorDerecha.setText(" " + oponenteActual);
            datosJugadorDerecha.insertIcon(new ImageIcon("ar/edu/unlu/poo/images/starIcon.jpg"));
        }else {datosJugadorDerecha.setText(oponenteActual);}
    }

    private String verificarAnfitrion(String oponenteActual) {
        //IDEA:si el jugador contiene una estrella de string entonces le agrego una imagen con forma de estrella a ese jugador
        // y luego borro la estrella de string
        if (controlador.getNombreAnfitrion().equals(oponenteActual)){
            oponenteActual += " *";
        }
        return oponenteActual;
    }

    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void pantallaEspera() {
        System.out.println(modoChat);
        if (!modoChat) {
            panelJugadas.removeAll();
            listaCheckJugada.clear();
            if (controlador.getNombreJugador() == null) {
                obtenerNombre();
                System.out.println("paso por nombre");
                apostarButton.setVisible(false);
                spinnerApuesta.setVisible(false);
            } else if (controlador.esAnfitrion()){
                apostarButton.setVisible(true);
                spinnerApuesta.setVisible(true);
                activarOpcionesMesa();
                tabbedPane.add(panelOpcionesMesa,1);
                tabbedPane.setIconAt(1, new ImageIcon("ar/edu/unlu/poo/images/opcionesMesaIcon.jpg"));
                //esto sucede solo si se esta iniciando una nueva partida en el mismo servidor
                if (controlador.partidaCargada()) {
                    mostrarEsperaCargarPartidaAnfitrion();
                }else {
                    mostrarEsperaAnfitrion();
                }
                comprobarApuesta();
                iniciarPartidaButton.setVisible(true);
            } else {
                desactivarOpcionesMesa();
                apostarButton.setVisible(true);
                spinnerApuesta.setVisible(true);
                tabbedPane.add(panelOpcionesMesa,1);
                tabbedPane.setIconAt(1, new ImageIcon("ar/edu/unlu/poo/images/opcionesMesaIcon.jpg"));
                if (controlador.partidaCargada()) {
                    mostrarEsperaCargarPartida();
                } else {
                    mostrarEspera();
                }
                comprobarApuesta();
            }
            panelInicio.setVisible(true);
        }else {
            mostrarNuevoMensaje("Se esta esperando a que se inicie la partida");
        }
    }

    private void verificarEstadosOpcionesMesa() {
        if (controlador.getTiempoPorTurno() == 60){
            seleccionTiempo.setSelectedIndex(1);
        } else if (controlador.getTiempoPorTurno() == 120) {
            seleccionTiempo.setSelectedIndex(2);
        }else {
            seleccionTiempo.setSelectedIndex(3);
        }
        if (controlador.getEstadoCompetitivo()){
            partidaCompetitivaCheck.setSelected(true);
        }else {
            partidaCompetitivaCheck.setSelected(false);
        }
        if (controlador.publicoPermitido()){
            checkBoxChat.setSelected(true);
        }else {
            checkBoxChat.setSelected(false);
        }
        if (controlador.getModoJuego().equals("EXPRES")){
            modoExpresCheckBox.setSelected(true);
            modoPorPuntosCheckBox.setSelected(false);
        }else {
            modoExpresCheckBox.setSelected(false);
            modoPorPuntosCheckBox.setSelected(true);
        }
    }

    private void desactivarOpcionesMesa() {
        seleccionTiempo.setVisible(false);
        partidaCompetitivaCheck.setVisible(false);
        modoExpresCheckBox.setVisible(false);
        modoPorPuntosCheckBox.setVisible(false);
        checkBoxChat.setVisible(false);
        txtOpcionMesa.setText("No podes modificar las opciones de mesa por no ser anfitrion.");
    }

    private void activarOpcionesMesa() {
        seleccionTiempo.setVisible(true);
        partidaCompetitivaCheck.setVisible(true);
        modoExpresCheckBox.setVisible(true);
        modoPorPuntosCheckBox.setVisible(true);
        checkBoxChat.setVisible(true);
        txtOpcionMesa.setText("Seleccione las opciones de Mesa que quiera cambiar");
    }

    private void mostrarEspera() {
        panelDerechaInicio.remove(cargarPartidaButton);
        panelPartida.remove(guardarPartidaButton);
        txtInfoInicio.setText("\nesperando a que se unan jugadores (se necesitan entre 2-4 jugadores para empezar a jugar) " +
                "\nCantidad de jugadores:" + controlador.cantJugadores());
        actualizarBarra();
        iniciarPartidaButton.setVisible(false);
        mostrarMensajeAsistente("Bienvenido al Rummy " + controlador.getNombreJugador() + "!!! Una vez que haya 2 jugadores o mas en la partida el anfitrion iniciara la partida." +
                "\nAdemas, si quieres apostar podes hacerlo con minimo 250 fichas." +
                "\nQue disfrutes el juego!!!");

    }

    private void mostrarEsperaAnfitrion() {
        if (!controlador.partidaCargada() && controlador.cantJugadores() <= 1){
            panelDerechaInicio.add(cargarPartidaButton);
        }
        txtInfoInicio.setText("Esperando a que se unan jugadores (se necesitan entre 2-4 jugadores para empezar a jugar) " +
                "\nCantidad de jugadores:" + controlador.cantJugadores() +
                "\nCuando este la cantidad necesaria presione el boton para iniciar.");
        actualizarBarra();
        mostrarMensajeAsistente("Bienvenido al Rummy " + controlador.getNombreJugador() + "!!! Una vez que haya 2 jugadores o mas en la partida \npodes iniciar la partida apretando el boton del mismo nombre." +
                                "\nAdemas, si quieres apostar podes hacerlo con minimo 250 fichas.");

        mostrarMensajeAsistente("Tambien tenes disponible las opciones de mesa que te permiten cambiar el tiempo por cada turno, \ncambiar el modo de juego (ya sea expres o por puntos), activar o desactivar las partidas competitivas \ny por ultimo podes activar el check el cual se puede unir una vez este iniciada la partida." +
                                "\nQue disfrutes el juego!!!");
        //comprobarApuesta();
    }

    private void mostrarEsperaCargarPartida() {
        txtInfoInicio.setText("Se te asigno un nombre de jugador automaticamente porque la partida fue cargada por el anfitrion." +
                "\nespere a que se activen los jugadores (se necesitan que los " + controlador.cantJugadores() + " jugadores esten para cargar correctamente la partida)" +
                "Jugadores Activos:" + controlador.getJugadoresActivos());
        comprobarApuesta();
        mostrarMensajeAsistente("Se te asigno un nombre de jugador automaticamente porque la partida fue cargada por el anfitrion.");
        mostrarMensajeAsistente("Bienvenido al Rummy " + controlador.getNombreJugador() + "!!! Una vez que esten todos los jugadores activos el anfitrion iniciara la partida.");
    }

    private void comprobarApuesta() {
        if (controlador.apuestasActivadas() && controlador.getNombreJugador() != null) {
            if (controlador.getcantidadApostada() == 0) {
                controlador.restarFichas();
                avisarSobreApuesta();
            }else {
                avisarSobreApuesta();
            }
        }
        mostrarFichas();
    }

    private void mostrarEsperaCargarPartidaAnfitrion() {
        verificarEstadosOpcionesMesa();
        txtInfoInicio.setText("Se ha cargado una partida!" +
                "\nesperando a que se activen los jugadores (se necesitan que los " + controlador.cantJugadores() + " jugadores esten para cargar correctamente la partida)" +
                "\nJugadores Activos:" + controlador.getJugadoresActivos() +
                "\nCuando este la cantidad de jugadores activos necesaria presione el boton iniciar la partida:");
        comprobarApuesta();
        mostrarMensajeAsistente("Se te asigno un nombre de jugador automaticamente porque la partida fue cargada por el anfitrion.");
        mostrarMensajeAsistente("Bienvenido al Rummy " + controlador.getNombreJugador() + "!!! Una vez que esten todos los jugadores activos podrias iniciar la partida presionando el boton Iniciar Partida.");
    }

    private void mostrarFichas() {
        if (controlador.getNombreJugador() != null){
            txtInfoInicio.setText(txtInfoInicio.getText() + "\nTus fichas: " + controlador.cantFichas());
        }
    }

    private void actualizarBarra() {
        cantidadJugadoresBar.setMaximum(4);
        cantidadJugadoresBar.setMinimum(0);
        if (controlador.partidaCargada()){
            cantidadJugadoresBar.setMaximum(controlador.cantJugadores());
            cantidadJugadoresBar.setValue(controlador.getJugadoresActivos());
            cantidadJugadoresBar.setString("Jugadores Activos");
        }else {
            cantidadJugadoresBar.setValue(controlador.cantJugadores());
            cantidadJugadoresBar.setString("Jugadores");
        }
    }

    @Override
    public void actualizarCantJugadores() {
        if (!modoChat) {
            pantallaEspera();
        }else {
            mostrarNuevoMensaje("Se esta esperando a que inicie la partida. Cantidad de jugadores " + controlador.cantJugadores() + ". Activos: " + controlador.getJugadoresActivos());
        }
    }

    @Override
    public void iniciarTurno() {
        if (!modoChat) {
            if (tabbedPane.getComponentAt(0).equals(panelInicio)) {
                activarPartida();
            }
            panelJugadas.removeAll();
            listaCheckJugada.clear();
            agregarCartaAJugadaButton.setEnabled(false);
            terminarTurnoButton.setEnabled(false);
            seleccionJugada.setEnabled(false);
            cancelarSeleccionButton.setEnabled(false);
            agregarNuevaJugadaButton.setEnabled(false);
            asignarNombresJugadores();
            //establecerColorJugadores();
            actualizarCartaBocaArriba();
            if (controlador.esTurnoJugador() && !controlador.isEliminado()) {
                if (controlador.jugadorEnAutomatico()) {
                    controlador.iniciarJuegoAutomatico();
                } else if (controlador.esAnfitrion()) {
                    guardarPartidaButton.setEnabled(true);
                }
                txtTurno.setText("Bienvenido al | modo " + controlador.getModoJuego() + " | , " + controlador.getNombreJugador() + ". Es su turno.");
                mazoButton.setEnabled(true);
                cartaBocaArribaButton.setEnabled(true);
                mostrarMensajeAsistente("Ahora mismo es tu turno. Para comenzar debes decidir si agarrar una carta del mazo o la carta que esta boca arriba al lado del mazo.");
                comprobarTiempoTurno();
            } else if (controlador.isEliminado()) {
                mazoButton.setEnabled(false);
                cartaBocaArribaButton.setEnabled(false);
                if (controlador.apuestasActivadas()) {
                    reengancharseButton.setEnabled(true);
                    txtTurno.setText("Has sido Eliminado. Si quiere puede reengancharse a la partida apostando la mitad de las fichas que aposto antes y volvera a jugar empezando el turno con los mismos puntos que el jugador que mas tiene.");
                    mostrarMensajeAsistente("Fuiste elminado por sobrepasar el limite de puntos pero tenes la chance reengancharte a la partida con los puntos del jugador que mas tiene." +
                            "\nPara eso dirigite al panel de partida y presiona reengancharse.");
                } else {
                    txtTurno.setText("Has sido Eliminado. No es posible reengancharse debido a que las apuestas no estan activadas");
                    mostrarMensajeAsistente("Fuiste elminado por sobrepasar el limite de puntos pero no podes reengancharte a la partida porque las apuestas no estan actvadas. Debes esperar a que la partida termine :(");
                }
            } else {
                esperarTurno();
            }
            txtTurno.setText(txtTurno.getText() + ". Tus fichas: " + controlador.cantFichas() + " Tu apuesta: " + controlador.getcantidadApostada() +
                    "\nFichas en el bote de apuestas: " + controlador.getcantidadFichasBote() + ". Jugadores Restantes en la partida: " + controlador.getCantDisponibles());
            if (controlador.getModoJuego().equals("JUEGOAPUNTOS")) {
                txtTurno.setText(txtTurno.getText() + ". Tus puntos de partida: " + controlador.getpuntosJugador());
            }
        }
    }

    private void comprobarTiempoTurno() {
        if (controlador.getTiempoPorTurno() != 0){
            mostrarMensajeAsistente("CUIDADO: Tenes " + controlador.getTiempoPorTurno() + " segundos para poder terminar tu turno sino se realizara automaticamente." +
                               "\nPodes ver el tiempo actual en el panel de partida.");
            temporizador = new Timer();
            tiempoTurno = new Timer();
            crearTareaJuegoAutomatico();
            crearTareaTemporizador();
            tiempoRestante = controlador.getTiempoPorTurno();
            if (controlador.getTiempoPorTurno() == 60) {
                tiempoTurno.schedule(juegoAutomatico,60000);
            }else {
                tiempoTurno.schedule(juegoAutomatico,120000);
            }
            temporizador.schedule(mostrarTiempoActual,0,1000);
        }
    }

    private void crearTareaTemporizador() {
        mostrarTiempoActual = new TimerTask() {
            @Override
            public void run() {
                txtTemporizador.setText("Tiempo: " + tiempoRestante);
                tiempoRestante--;
                if (tiempoRestante == -1){
                    temporizador.cancel();
                }
            }
        };
    }

    private void crearTareaJuegoAutomatico() {
        juegoAutomatico = new TimerTask() {
            @Override
            public void run() {
                tiempoTurno.cancel();
                txtTurno.setText("Se acabo el tiempo!!! Tu turno se jugara automaticamente.");
                tabbedPane.add(panelInicio,0);
                txtInfoInicio.setText("Se te activo el juego automatico por no terminar tu turno a tiempo. En cualquier momento lo podes desactivar." +
                        "\nAVISO: si todos los jugadores entran en modo automatico la partida finalizara amistosamente.");
                desactivarJuegoAutomaticoButton.setVisible(true);
                mostrarMensajeAsistente("Se te habilito el modo de juego automatico por no llegar a terminar a tiempo tu turno. Debes ir al inicio si queres cancelar el modo automatico." +
                        "\nsolo RECORDA: si todos los jugadores estan en modo automatico la partida sera finalizada amistosamente");
                controlador.iniciarJuegoAutomatico();
            }
        };
    }

    private void actualizarCartaBocaArriba() {
        try {
            ICarta cartaDescarteActual = controlador.getCartaDescarte();
            String imagenActual = "ar/edu/unlu/poo/images/cartas/" + cartaDescarteActual.getNumero() + cartaDescarteActual.getPalo() + ".png";
            ImageIcon cartaActual = new ImageIcon(imagenActual);
            cartaBocaArribaButton.setIcon(cartaActual);
            cartaBocaArribaButton.setBorderPainted(false);
            cartaBocaArribaButton.setVisible(true);
        } catch (NoHayCartaBocaArriba e) {
            cartaBocaArribaButton.setVisible(false);
        }
    }

    private void activarPartida() {
        panelPartida.setName("Partida");
        tabbedPane.add(panelPartida, 0);
        tabbedPane.remove(panelInicio);
    }

    public void esperarTurno() {
        txtTurno.setText("Espere su turno. Ahora mismo es el turno de " + controlador.getNombreTurnoActual());
        mostrarMensajeAsistente("Solo debes esperar a que sea tu turno. Mientras piensa en una buena jugada!");
        mazoButton.setEnabled(false);
        cartaBocaArribaButton.setEnabled(false);
    }


    @Override
    public void nuevoTurno() {
        if (!modoChat) {
            actualizarCartas();
            iniciarTurno();
        }else {
            mostrarNuevoMensaje("Se Ha iniciado un nuevo turno. Recuerde usar /mostrarNombreTurnoActual para saber de quien se trata.");
        }
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
        String imagenActual;
        ImageIcon cartaActual;
        if (listaModeloAbajo.size() != cartasJugador.size()) {
            listaModeloAbajo.clear();
            for (ICarta carta : cartasJugador) {
                imagenActual = "ar/edu/unlu/poo/images/cartas/" + carta.getNumero() + carta.getPalo() + ".png";
                cartaActual = new ImageIcon(imagenActual);
                listaModeloAbajo.addElement(cartaActual);
            }
        }
    }

    private void actualizarCartasJugadorIzquierda(int cantidadCartas) {
        if (listaModeloIzquierda.size() != cantidadCartas) {
            listaModeloIzquierda.clear();
            ImageIcon reversoCarta = new ImageIcon("ar/edu/unlu/poo/images/cartas/reverso/reversoCartaJugadorIzquierda.png");
            for (int i = 0; i < cantidadCartas; i++) {
                listaModeloIzquierda.addElement(reversoCarta);
            }
        }
    }

    private void actulizarCartasJugadorArriba(int cantidadCartas) {
        if (listaModeloArriba.size() != cantidadCartas) {
            listaModeloArriba.clear();
            ImageIcon reversoCarta = new ImageIcon("ar/edu/unlu/poo/images/cartas/reverso/reversoCartaJugadorArriba.png");
            for (int i = 0; i < cantidadCartas; i++) {
                listaModeloArriba.addElement(reversoCarta);
            }
        }
    }

    private void actualizarCartasJugadorDerecha(int cantidadCartas) {
        if (listaModeloDerecha.size() != cantidadCartas) {
            listaModeloDerecha.clear();
            ImageIcon reversoCarta = new ImageIcon("ar/edu/unlu/poo/images/cartas/reverso/reversoCartaJugadorDerecha.png");
            for (int i = 0; i < cantidadCartas; i++) {
                listaModeloDerecha.addElement(reversoCarta);
            }
        }
    }

    @Override
    public void continuarTurnoActual() {
        if (!modoChat) {
            if (controlador.esTurnoJugador()) {
                mostrarMensajeAsistente("Ahora que tomaste una carta puedes proseguir agregando una carta a una jugada, agregar una nueva jugada \no incluso termnar tu turno." +
                        "\nA continuacion te dejo algunos tips sobre cada una:" +
                        "\n-Para agregar una nueva jugada debes seleccionar las cartas que queres agregar solo presionando una por una \ny una vez que eso este listo debes selecconar entre las tres jugadas disponibles que te aparecen en seleccionar jugada.\n Por ultimo solo presiona el boton de agregar la nueva jugada!!!" +
                        "\n-Para agregar una o mas cartas a una jugada que ya esta en mesa solo debes seleccionar las cartas presionandolas \ny luego ponerle un tilde presionando la casilla de la jugada que necesites" +
                        "\n-Para terminar el turno debes seleccionar una unica carta y presionar el boton con el mismo nombre." +
                        "\n-En el caso que hayas seleccionado mal tus cartas puedes seleccionar el boton cancelar seleccion para deseleccionarlas");
                cartasSeleccionadasPosicion.clear();
                actualizarCartas();
                agregarCartaAJugadaButton.setEnabled(true);
                terminarTurnoButton.setEnabled(true);
                agregarNuevaJugadaButton.setEnabled(true);
                seleccionJugada.setEnabled(true);
                cancelarSeleccionButton.setEnabled(true);
            }
            actualizarCartaBocaArriba();
        }else {
            mostrarNuevoMensaje("Se esta continuando el turno Actual. Recuerde usar /mostrarNombreTurnoActual para saber de quien se trata.");
        }
    }

    @Override
    public void finalizarPartida() {
        if (!modoChat) {
            volverAInicio();
            desactivarJuegoAutomaticoButton.setVisible(false);
            txtInfoInicio.setText("\nLa partida ha finalizado!!! El ganador es..." + controlador.getGanador() +
                    " con " + controlador.getCantidadPuntosGanador() + " puntos");
            mostrarMensajeAsistente("La partido ha finalizado y ha ganado " + controlador.getGanador() + " con " + controlador.getCantidadPuntosGanador() + " puntos");
            eleccionNuevaPartida();
            mostrarTablaPosiciones(controlador.obtenerPosiciones());
        }else {
            mostrarNuevoMensaje("La partida ha finalizado!!!");
            mostrarNuevoMensaje("El ganador es..." + controlador.getGanador() +
                    " con " + controlador.getCantidadPuntosGanador()+" puntos");
            if (!controlador.getEstadoCompetitivo()){
                mostrarNuevoMensaje("\nNo se ganaron ni puntos ni fichas apostadas porque el competitivo esta desactivado.");
            }
            mostrarTablaPosiciones(controlador.obtenerPosiciones());
        }
    }

    @Override
    public void actualizarJugadas() {
        if (!modoChat) {
            cartasSeleccionadasPosicion.clear();
            listaCheckJugada.clear();
            JList<ImageIcon> listaActual;
            DefaultListModel<ImageIcon> listaModeloActual;
            JCheckBox checkJugadaActual;
            actualizarCartas();
            String imagenActual;
            ImageIcon cartaActual;
            JScrollPane panelScrollActual = null;
            ITapete jugadasEnMesa = controlador.obtenerJugadas();
            System.out.println("actualizo jugada");
            JPanel panelActual = new JPanel(new FlowLayout());
            if (listaCheckJugada.size() != jugadasEnMesa.getListaJugada().size()) {
                panelJugadas.removeAll();
                for (int i = 0; i < jugadasEnMesa.getListaJugada().size(); i++) {
                    //if (i % 3 == 0) {
                        panelActual = new JPanel();
                        BoxLayout layoutPanelActual = new BoxLayout(panelActual,BoxLayout.Y_AXIS);
                        panelActual.setLayout(layoutPanelActual);
                        panelActual.setPreferredSize(new Dimension(90, 117));
                    panelActual.setBackground(new Color(4, 21, 80));
                        panelJugadas.add(panelActual);
                    //}
                    listaActual = new JList<>();
                    listaActual.setBackground(new Color(4, 21, 80));
                    listaModeloActual = new DefaultListModel<>();
                    listaActual.setModel(listaModeloActual);
                    //listaActual.setName("Jugada " + (i + 1));
                    listaActual.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                    panelScrollActual = new JScrollPane(listaActual);
                    //60 y 60 para jugadas mas grandes
                    panelScrollActual.setPreferredSize(new Dimension(85, 105));
                    checkJugadaActual = new JCheckBox("Jugada " + (i + 1) + ":");
                    checkJugadaActual.setBackground(new Color(4, 21, 80));
                    checkJugadaActual.setForeground(new Color(253,255,254));
                    listaCheckJugada.add(checkJugadaActual);
                    panelActual.add(checkJugadaActual);
                    panelScrollActual.setViewportView(listaActual);
                    panelActual.add(panelScrollActual);
                    for (ICarta carta : jugadasEnMesa.getListaJugada().get(i).getCartasJugada()) {
                        imagenActual = "ar/edu/unlu/poo/images/cartas/" + carta.getNumero() + carta.getPalo() + ".png";
                        cartaActual = new ImageIcon(imagenActual);
                        listaModeloActual.addElement(cartaActual);
                    }
                }
            }
            mostrarMensajeAsistente("Hay nuevas jugadas en la mesa!!!");
        }else {
            mostrarNuevoMensaje("Hay nuevas jugadas sin ver!!! escriba el comando /mostrarJugadas para verlas");
        }
    }

    @Override
    public void cerrarPartida() {
        if (!modoChat) {
            volverAInicio();
            if (controlador.getModoJuego().equals("EXPRES") || !controlador.juegoIniciado()) {
                txtInfoInicio.setText("\nLa partida fue cerrada ya que no se pueden  hacer combinaciones o añadir cartas ");
                mostrarMensajeAsistente("Se cerro la partida porque no se pueden hacer nuevas jugadas y las jugadas en mesa estan llenas.");
                if (!controlador.getEstadoCompetitivo()) {
                    txtInfoInicio.setText(txtInfoInicio.getText() + "\nNo se ganaron ni puntos ni fichas apostadas porque el competitivo esta desactivado.");
                    mostrarMensajeAsistente("Las partidas competitivas estan desactivas por lo que tampoco se ganaron puntos ni fichas :c");
                }
                eleccionNuevaPartida();
            } else {
                txtInfoInicio.setText("\nLa ronda fue cerrada por lo que se sumaran los puntos sobrantes a cada jugador e iniciara una nueva ronda...");
                mostrarMensajeAsistente("La ronda quedo cerrada por tener jugadas llenas y por no poder hacer nuevas jugadas. Pronto iniciara una nueva ronda!");
                //controlador.iniciarNuevaRonda();
                controlador.resultadoRonda();
            }
        }else {
            if (controlador.getModoJuego().equals("EXPRES") || !controlador.juegoIniciado()){
                mostrarNuevoMensaje("La partida fue cerrada ya que no se pueden  hacer combinaciones o añadir cartas ");
                if (!controlador.getEstadoCompetitivo()){
                    mostrarNuevoMensaje("No se ganaron ni puntos ni fichas apostadas porque el competitivo esta desactivado.");
                }
                //controlador.obtenerPosiciones();
                mostrarTablaPosiciones(controlador.obtenerPosiciones());
            }else {
                mostrarNuevoMensaje("La ronda fue cerrada por lo que se sumaran los puntos sobrantes a cada jugador e iniciara una nueva ronda...");
            }
        }
    }

    @Override
    public void mostrarApuestaCancelada() {
        if (!modoChat) {
            pantallaEspera();
            if (!controlador.apuestasActivadas()) {
                txtInfoInicio.setText("\nApuestas Desactivadas!!!");
                mostrarMensajeAsistente("Se cancelaron las apuestas!!!");
            }
        }else {
            mostrarNuevoMensaje("Se cancelaron las apuestas en la partida.");
        }
    }

    @Override
    public void avisarSobreApuesta() {
        if (!modoChat) {
            txtInfoInicio.setText(txtInfoInicio.getText() + "\nHay apuestas Activas de " + controlador.getcantidadApostada() + " fichas!!! \nPara desactivarlas apueste 0 fichas" +
                        "\nTu total de fichas ahora seria de: " + controlador.cantFichas());
            mostrarMensajeAsistente("Las apuestas fueron activadas. En el caso que quieras desactivarlas puedes apostar 0 fichas y se cancelaran y en caso contrario todos apostaran " + controlador.getcantidadApostada() + " fichas.");
        }else {
            escribirEnChat("Los jugadores tienen las apuestas activas!!");
        }
    }

    @Override
    public void mostrarResultadosPuntosRonda(ArrayList<IJugador> jugadores) {
        if (!modoChat) {
            mostrarMensajeAsistente("Ahora mismo podes visualizar los resultados en puntos de la ronda actual en el panel resumen de partida mientras se prepara la nueva ronda.");
        }
        panelFinRonda.setName("Resumen de Partida");
        tabbedPane.add(panelFinRonda);
        for (int i = 0; i < jugadores.size(); i++) {
            if (i == 0){
                nombreJugadorPuntos1.setText(jugadores.get(i).getNombre());
                puntosJugador1.setText(puntosJugador1.getText() + "\n\t" + jugadores.get(i).getPuntosDePartida());
            } else if (i == 1) {
                nombreJugadorPuntos2.setText(jugadores.get(i).getNombre());
                puntosJugador2.setText(puntosJugador2.getText() + "\n\t" + jugadores.get(i).getPuntosDePartida());
            } else if (i == 2) {
                nombreJugadorPuntos3.setText(jugadores.get(i).getNombre());
                puntosJugador3.setText(puntosJugador3.getText() + "\n\t" + jugadores.get(i).getPuntosDePartida());
            } else if (i == 3){
                nombreJugadorPuntos4.setText(jugadores.get(i).getNombre());
                puntosJugador4.setText(puntosJugador4.getText() + "\n\t" + jugadores.get(i).getPuntosDePartida());
            }
        }
        if (controlador.esAnfitrion()){
            iniciarNuevaRondaButton.setVisible(true);
        }else{
            iniciarNuevaRondaButton.setVisible(false);
        }
    }

    @Override
    public void finalizarPartidaAmistosamente() {
        if (!modoChat) {
            volverAInicio();
            txtInfoInicio.setText("\nLa partida ha finalizado Amistosamente!!! Se devolvieron apuestas actuales y los puntos no cuentan");
            mostrarMensajeAsistente("La partida finalizo amistosamente por lo que se devolvieron las apuestas si estaban activas y no contaron los puntos.");
            panelAbajo.setEnabled(false);
            panelOpciones.setEnabled(false);
            eleccionNuevaPartida();
        }else {
            mostrarNuevoMensaje("Se finalizo la partida amistosamente!!! Se devolvieron apuestas actuales y los puntos no cuentan");
        }
    }

    private void volverAInicio() {
        panelInicio.setName("Inicio");
        tabbedPane.add(panelInicio, 0);
        tabbedPane.remove(panelPartida);
    }

    private void eleccionNuevaPartida() {
        //controlador.comprobarAnfitrion();
        if (controlador.esAnfitrion()) {
            mostrarMensajeAsistente("Si deseas volver a jugar una nueva partida ve al menu de inicio y presiona nueva partida. " +
                    "\nEn caso contrario, espero tengas un buen dia!! :D");
            txtInfoInicio.setText(txtInfoInicio.getText() + "\n¿Desea Iniciar una nueva partida?");
            iniciarNuevaPartidaButton.setVisible(true);
            salirButton.setVisible(true);
            iniciarPartidaButton.setVisible(false);
        }else {
            mostrarMensajeAsistente("Espera a que el anfitrion decida si quiere una nueva partida. " +
                    "\nEn caso que abandone el juego esta la posibilidad de que uno del resto de jugadores se convierta en el nuevo anfitrion.");
        }
        actualizarBarra();
    }

    @Override
    public void eleccionAnularPartida() {
        if (!modoChat) {
            seleccionAnularPartida = true;
            anularPartidaButton.setForeground(new Color(229, 213, 7));
        }else {
            mostrarNuevoMensaje("Se esta solicitando anular la partida.");
        }
    }

    @Override
    public void obtenerNombre() {
        if (!modoChat) {
            System.out.println("llego aca");
            //txtInfoInicio.setText("Ingrese su nombre");
            mostrarMensajeAsistente("Solo escribe tu nombre e inicia sesion");
            panelUsuario.setVisible(true);
            txtNombre.setEnabled(true);
            iniciarSesionButton.setEnabled(true);
            cargarPartidaButton.setVisible(false);
        }else {
            estadoActualChat = EstadoChat.OBTENER_NOMBRE;
            txtMensajeChat.setText("Ingrese su nombre como espectador:\n");
        }
    }

    @Override
    public void cerrarJuego() {
        System.out.println("salio por aca");
        controlador.eliminarJugador();
        System.exit(0);
        //cambiar directamente sacar al anfitrion y hacerle la pregunta al siguiente anfitrion y si elige que no cerrarle su ventana
    }

    @Override
    public void mostrarTablaPosiciones(ArrayList<IJugador> jugadores) {
        if (!modoChat) {
            mostrarMensajeAsistente("Puedes visualizar la tabla de posiciones en el panel del mismo nombre. " +
                    "\nEn caso que no aparezcas es porque no alcanzaste el top de jugadores :(");
        }
        panelTablaPosiciones.setName("Tabla De Posiciones");
        tabbedPane.add(panelTablaPosiciones);
        //txtTablaPosiciones.setText("\nTabla de posiciones (top 5 jugadores):");
        IJugador jugadorAux;
        txtnombreJugadorTablaPosiciones.setText("");
        txtPuntosJugadorTablaPosiciones.setText("");
        for (int i = 0; i < 5; i++) {
            if (i < jugadores.size()){
                jugadorAux = jugadores.get(i);
                txtnombreJugadorTablaPosiciones.setText(txtnombreJugadorTablaPosiciones.getText() +
                        (i + 1) + "- " + jugadorAux.getNombre() +  "\n");
                txtPuntosJugadorTablaPosiciones.setText(txtPuntosJugadorTablaPosiciones.getText()+ "\t" + jugadorAux.getPuntosTotalesXP() + "\n");
            }
        }
    }

    @Override
    public void mostrarErrorConexion() {
        mostrarMensajeAsistente("Parece que has tenido un problema con tu conexion :(");
        txtTurno.setText("Hubo un problema con tu conexion.");
    }

    @Override
    public void errorCantidadJugadores() {
        mostrarMensajeAsistente("ERROR: cantidad de jugadores insuficiente.");
        volverAInicio();
    }

    @Override
    public void mostrarJugadorSalioDelJuego() {
        volverAInicio();
        txtInfoInicio.setText("Un jugador Ha salido");
        mostrarMensajeAsistente("Un jugador ha salido del juego.");
        actualizarBarra();
        if (controlador.juegoIniciado()) {
            eleccionNuevaPartida();
        }else {
            pantallaEspera();
        }
    }

    @Override
    public void avisarCambiosOpcionesMesa() {
        if (!modoChat) {
            tabbedPane.setForegroundAt(1, new Color(229, 11, 9));
            mostrarMensajeAsistente("El anfitrion realizo cambios en las opciones de mesa del juego.");
        }else {
            mostrarNuevoMensaje("El anfitrion realizo cambios en las opciones de mesa del juego.");
            if (controlador.publicoPermitido()){
                txtEscribirChat.setEnabled(true);
            }else {
                txtEscribirChat.setEnabled(false);
                mostrarNuevoMensaje("El anfitrion desahibilito el publico por lo que no podras escribir.");
            }
        }
    }

    @Override
    public void activarSoloChat() {
        modoChat = true;
        tabbedPane.removeAll();
        tabbedPane.add(panelChat);
        obtenerNombre();
        //falta agregar el modo chat para cada actualizar
    }

    @Override
    public void mostrarNuevoMensaje(String nuevoMensaje) {
        escribirEnChat(nuevoMensaje);
    }

    @Override
    public void mostrarErrorJugadaLLena() {
        cartasSeleccionadasPosicion.clear();
        mostrarMensajeAsistente("Error: La jugada seleccionada esta llena :(");
    }

    @Override
    public void mostrarErrorCartaNoAgregada() {
        cartasSeleccionadasPosicion.clear();
        mostrarMensajeAsistente("Error: Las cartas seleccionadas para agregar no forman parte de la jugada seleccionada.");
    }

    @Override
    public void mostrarErrorNoEsJugada() {
        mostrarMensajeAsistente("Error: Las cartas seleccionadas no forman una jugada. :C");
        cartasSeleccionadasPosicion.clear();
    }

    @Override
    public void mostrarErrorCartasInsuficientes() {
        cartasSeleccionadasPosicion.clear();
        mostrarMensajeAsistente("Error: Faltan cartas para continuar.");
    }

    @Override
    public void mostrarErrorRummyNoDisponible() {
        cartasSeleccionadasPosicion.clear();
        mostrarMensajeAsistente("Error: La opcion de rummy no esta disponible porque ya hizo una jugada anteriormente. :(");
    }
}
