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
import java.time.LocalDateTime;
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
                iniciarPartidaButton.setVisible(false);
                panelUsuario.setVisible(true);
                listaCheckJugada = new ArrayList<>();

                spinnerApuesta.setForeground(new Color(24,224,229));
                spinnerApuesta.setBackground(new Color(4,18,48));

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
                                controlador.nuevoJugador(controlador.esAnfitrion(), txtNombre.getText());
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
                            controlador.cancelarApuesta();
                        } else if ((int)spinnerApuesta.getValue() >= 250) {
                            controlador.apostar((int)spinnerApuesta.getValue());
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

                        JList<String> cartas = (JList<String>) e.getSource();
                        if (e.getValueIsAdjusting()) {
                            int indiceSeleccionado = cartas.getSelectedIndex();
                            if (indiceSeleccionado != -1 && !cartasSeleccionadasPosicion.contains(indiceSeleccionado)) {
                                System.out.println("seleccionado : " + indiceSeleccionado);
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
                        controlador.terminarTurno(cartasSeleccionadasPosicion);
                    }
                });
                mazoButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mazoButton.setEnabled(false);
                        cartaBocaArribaButton.setEnabled(false);
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
        juegoAutomatico = new TimerTask() {
            @Override
            public void run() {
                tiempoTurno.cancel();
                txtTurno.setText("Se acabo el tiempo!!! Tu turno se jugara automaticamente.");
                tabbedPane.add(panelInicio,0);
                txtInfoInicio.setText("Se te activo el juego automatico por no terminar tu turno a tiempo. En cualquier momento lo podes desactivar." +
                        "\nAVISO: si todos los jugadores entran en modo automatico la partida finalizara amistosamente.");
                desactivarJuegoAutomaticoButton.setVisible(true);
                txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() +"|-Se te habilito el modo de juego automatico por no llegar a terminar a tiempo tu turno. Debes ir al inicio si queres cancelar el modo automatico." +
                        "\nsolo RECORDA: si todos los jugadores estan en modo automatico la partida sera finalizada amistosamente");
                controlador.iniciarJuegoAutomatico();
            }
        };
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
        desactivarJuegoAutomaticoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-Juego Automatico Desactivado!!!");
                desactivarJuegoAutomaticoButton.setVisible(false);
                tabbedPane.remove(panelInicio);
                controlador.desactivarJuegoAutomatico();
            }
        });
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
        txtInfoInicio.setText("Error el nombre debe estar dentro del juego o su nombre esta vacio");
        txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-El nombre no fue aceptado es posible que ese nombre ya se encuentre en el juego o que escribiste un nombre vacio");
        //cambiar
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
                if (oponenteActual.contains("*")){
                    oponenteActual = oponenteActual.replace("*", "");
                    datosJugadorDerecha.setText(oponenteActual);
                    datosJugadorDerecha.insertIcon(new ImageIcon("ar/edu/unlu/poo/images/starIcon.jpg"));
                }else {datosJugadorDerecha.setText(oponenteActual);}
            } else if (i == 1) {
                if (oponenteActual.contains("*")){
                oponenteActual = oponenteActual.replace("*", "");
                datosJugadorArriba.setText(oponenteActual);
                datosJugadorArriba.insertIcon(new ImageIcon("ar/edu/unlu/poo/images/starIcon.jpg"));
                }else {datosJugadorArriba.setText(oponenteActual);}
            } else if (i == 2) {
                if (oponenteActual.contains("*")){
                    oponenteActual = oponenteActual.replace("*", "");
                    datosJugadorIzquierda.setText(oponenteActual);
                    datosJugadorIzquierda.insertIcon(new ImageIcon("ar/edu/unlu/poo/images/starIcon.jpg"));
                }else {datosJugadorIzquierda.setText(oponenteActual);}
            }
        }
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
        if (controlador.getNombreJugador() == null) {
            obtenerNombre();
        }else if (controlador.esAnfitrion()){
            //esto sucede solo si se esta iniciando una nueva partida en el mismo servidor
            iniciarPartidaButton.setVisible(true);
        }
        panelInicio.setVisible(true);
        if (controlador.esAnfitrion()){
            txtInfoInicio.setText("Esperando a que se unan jugadores (se necesitan entre 2-4 jugadores para empezar a jugar) " +
                    "\nCantidad de jugadores:" + controlador.cantJugadores() +
                    "\nCuando este la cantidad necesaria presione el boton para iniciar.");
            actualizarBarra();
            txtAsistenteAyuda.setText("\n|" + LocalDateTime.now() + "|-Bienvenido al Rummy " + controlador.getNombreJugador() + "!!! Una vez que haya 2 jugadores o mas en la partida podes iniciar la partida apretando el boton del mismo nombre." +
                    "\nAdemas, si quieres apostar podes hacerlo con minimo 250 fichas.");
            txtAsistenteAyuda.setText("\n|" + LocalDateTime.now() + "|-Tambien tenes disponible las opciones de mesa que te permiten cambiar el tiempo por cada turno, cambiar el modo de juego (ya sea expres o por puntos), activar o desactivar las partidas competitivas y por ultimo podes activar el publico el cual se puede unir una vez este iniciada la partida." +
                    "\nQue disfrutes el juego!!!");
        }else {
            txtInfoInicio.setText("\nesperando a que se unan jugadores (se necesitan entre 2-4 jugadores para empezar a jugar) " +
                    "\nCantidad de jugadores:" + controlador.cantJugadores());
            actualizarBarra();
            iniciarPartidaButton.setVisible(false);
            txtAsistenteAyuda.setText("\n|" + LocalDateTime.now() + "|-Bienvenido al Rummy " + controlador.getNombreJugador() + "!!! Una vez que haya 2 jugadores o mas en la partida el anfitrion iniciara la partida." +
                    "\nAdemas, si quieres apostar podes hacerlo con minimo 250 fichas." +
                    "\nQue disfrutes el juego!!!");
        }
        if (controlador.apuestasActivadas()){
            avisarSobreApuesta();
        }
    }

    /*private void establecerColorJugadores() {
        if (controlador.esAnfitrion()){
            datosJugadorActual.setBackground(new Color(13,13,229));
            datosJugadorDerecha.setBackground(new Color(229,14,16));
            datosJugadorArriba.setBackground(new Color(30,146,12));
            datosJugadorIzquierda.setBackground(new Color(215,80,180));
        } else if (datosJugadorDerecha.getText().contains(controlador.getNombreAnfitrion())) {
            //segun donde esta posicionado el anfitrion se establecen los mismos colores para todos los jugadores
            datosJugadorActual.setBackground(new Color(13,13,229));
            datosJugadorDerecha.setBackground(new Color(229,14,16));
            datosJugadorArriba.setBackground(new Color(30,146,12));
            datosJugadorIzquierda.setBackground(new Color(215,80,180));

        }
    }*/

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
        if (tabbedPane.getComponentAt(0).equals(panelInicio)) {
            activarPartida();
        }
        if (!controlador.esAnfitrion()){
            seleccionTiempo.setVisible(false);
            partidaCompetitivaCheck.setVisible(false);
            modoExpresCheckBox.setVisible(false);
            modoPorPuntosCheckBox.setVisible(false);
            checkBoxChat.setVisible(false);
            txtOpcionMesa.setText("No podes modificar las opciones de mesa por no ser anfitrion.");
        }else {
            seleccionTiempo.setVisible(true);
            partidaCompetitivaCheck.setVisible(true);
            modoExpresCheckBox.setVisible(true);
            modoPorPuntosCheckBox.setVisible(true);
            checkBoxChat.setVisible(true);
            txtOpcionMesa.setText("Seleccione las opciones de Mesa que quiera cambiar");
        }
        agregarCartaAJugadaButton.setEnabled(false);
        terminarTurnoButton.setEnabled(false);
        listaAbajo.setEnabled(false);
        seleccionJugada.setEnabled(false);
        cancelarSeleccionButton.setEnabled(false);
        agregarNuevaJugadaButton.setEnabled(false);
        asignarNombresJugadores();
        //establecerColorJugadores();
        actualizarCartaBocaArriba();
        if (controlador.esTurnoJugador() && !controlador.isEliminado()){
            if (controlador.jugadorEnAutomatico()){
                controlador.iniciarJuegoAutomatico();
            }
            txtTurno.setText("Bienvenido al | modo" +controlador.getModoJuego() + " | , " + controlador.getNombreJugador() +". Es su turno.");
            mazoButton.setEnabled(true);
            cartaBocaArribaButton.setEnabled(true);
            txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-Ahora mismo es tu turno. Para comenzar debes decidir si agarrar una carta del mazo o la carta que esta boca arriba al lado del mazo.");
            comprobarTiempoTurno();
        } else if (controlador.isEliminado()) {
            mazoButton.setEnabled(false);
            cartaBocaArribaButton.setEnabled(false);
            if (controlador.apuestasActivadas()){
                reengancharseButton.setEnabled(true);
                txtTurno.setText("Has sido Eliminado. Si quiere puede reengancharse a la partida apostando la mitad de las fichas que aposto antes y volvera a jugar empezando el turno con los mismos puntos que el jugador que mas tiene.");
                txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() +"|-Fuiste elminado por sobrepasar el limite de puntos pero tenes la chance reengancharte a la partida con los puntos del jugador que mas tiene." +
                        "\nPara eso dirigite al panel de partida y presiona reengancharse.");
            }else {
                txtTurno.setText("Has sido Eliminado. No es posible reengancharse debido a que las apuestas no estan activadas");
                txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() +"|-Fuiste elminado por sobrepasar el limite de puntos pero no podes reengancharte a la partida porque las apuestas no estan actvadas. Debes esperar a que la partida termine :(");
            }
        } else {
            esperarTurno();
        }
        txtTurno.setText(txtTurno.getText() +  "Tus fichas: " + controlador.cantFichas() + " Tu apuesta: " + controlador.getcantidadApostada() +
        "\nFichas en el bote de apuestas: " + controlador.getcantidadFichasBote() + ". Jugadores Restantes en la partida: " + controlador.getCantDisponibles());
        if (controlador.getModoJuego().equals("JUEGOAPUNTOS")){
            txtTurno.setText("Tus puntos: "+ controlador.getpuntosJugador());
        }
    }

    private void comprobarTiempoTurno() {
        if (controlador.getTiempoPorTurno() != 0){
            txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-CUIDADO: Tenes " + controlador.getTiempoPorTurno() + " segundos para poder terminar tu turno sino se realizara automaticamente." +
                    "\nPodes ver el tiempo actual en el panel de partida.");
            temporizador = new Timer();
            tiempoTurno = new Timer();
            if (controlador.getTiempoPorTurno() == 60) {
                temporizador.schedule(mostrarTiempoActual,0,60000);
                tiempoTurno.schedule(juegoAutomatico,60000);
            }else {
                temporizador.schedule(mostrarTiempoActual,0,120000);
                tiempoTurno.schedule(juegoAutomatico,120000);
            }
        }
    }

    private void actualizarCartaBocaArriba() {
        ICarta cartaDescarteActual = controlador.getCartaDescarte();
        String imagenActual = "ar/edu/unlu/poo/images/cartas/" + cartaDescarteActual.getNumero() + cartaDescarteActual.getPalo()+ ".png";
        ImageIcon cartaActual = new ImageIcon(imagenActual);
        cartaBocaArribaButton.setIcon(cartaActual);
        cartaBocaArribaButton.setBorderPainted(false);
    }

    private void activarPartida() {
        panelPartida.setName("Partida");
        tabbedPane.add(panelPartida, 0);
        tabbedPane.remove(panelInicio);
    }

    @Override
    public void esperarTurno() {
        txtTurno.setText("Espere su turno. Ahora mismo es el turno de " + controlador.getNombreTurnoActual());
        txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|- Solo debes esperar a que sea tu turno. Mientras piensa en una buena jugada!");
        mazoButton.setEnabled(false);
        cartaBocaArribaButton.setEnabled(false);
    }

    @Override
    public void actualizarCartas(ArrayList<ICarta> cartasJugador) {
        //?
    }

    @Override
    public void nuevoTurno() {
        actualizarCartas();
        iniciarTurno();
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
        String imagenActual;
        ImageIcon cartaActual;
        for (ICarta carta: cartasJugador) {
            imagenActual = "ar/edu/unlu/poo/images/cartas/" + carta.getNumero() + carta.getPalo()+ ".png";
            cartaActual = new ImageIcon(imagenActual);
            listaModeloAbajo.addElement(cartaActual);
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
        if (controlador.esTurnoJugador()) {
            txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-Ahora que tomaste una carta puedes proseguir agregando una carta a una jugada, agregar una nueva jugada o incluso termnar tu turno." +
                    "\nA continuacion te dejo algunos tips sobre cada una:" +
                    "\n-Para agregar una nueva jugada debes seleccionar las cartas que queres agregar solo presionando una por una y una vez que eso este listo debes selecconar entre las tres jugadas disponibles que te aparecen en seleccionar jugada. Por ultimo solo presiona el boton de agregar la nueva jugada!!!" +
                    "\n-Para agregar una o mas cartas a una jugada que ya esta en mesa solo debes seleccionar las cartas presionandolas y luego ponerle un tilde presonando la casilla de la jugada que necesites" +
                    "\n-Para terminar el turno debes seleccionar una unica carta y presonar el boton con el mismo nombre." +
                    "\n-En el caso que hayas seleccionado mal tus cartas puedes seleccionar el boton cancelar seleccion para deseleccionarlas");
            cartasSeleccionadasPosicion.clear();
            actualizarCartas();
            listaAbajo.setEnabled(true);
            agregarCartaAJugadaButton.setEnabled(true);
            terminarTurnoButton.setEnabled(true);
            agregarNuevaJugadaButton.setEnabled(true);
            seleccionJugada.setEnabled(true);
            cancelarSeleccionButton.setEnabled(true);
        }
        actualizarCartaBocaArriba();
    }

    @Override
    public void finalizarPartida() {
        volverAInicio();
        txtInfoInicio.setText("\nLa partida ha finalizado!!! El ganador es..." + controlador.getGanador() +
            " con " + controlador.getCantidadPuntosGanador()+" puntos");
        txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-La partido ha finalizado y ha ganado " + controlador.getGanador() + " con " + controlador.getCantidadPuntosGanador()+" puntos");
        eleccionNuevaPartida();
        controlador.obtenerPosiciones();
    }

    @Override
    public void actualizarJugadas() {
        cartasSeleccionadasPosicion.clear();
        listaCheckJugada.clear();
        JList<ImageIcon> listaActual;
        DefaultListModel<ImageIcon> listaModeloActual;
        JCheckBox checkJugadaActual;
        actualizarCartas();
        String imagenActual;
        ImageIcon cartaActual;
        JPanel panelActual = new JPanel(new FlowLayout());
        ITapete jugadasEnMesa = controlador.obtenerJugadas();
        panelJugadas.removeAll();
        for (int i = 0; i < jugadasEnMesa.getJugada().size(); i++) {
            if (i%3 == 0){
                panelActual = new JPanel(new FlowLayout());
                panelJugadas.add(panelActual);
            }
            listaActual = new JList<>();
            listaModeloActual = new DefaultListModel<>();
            listaActual.setModel(listaModeloActual);
            //listaActual.setName("Jugada " + (i + 1));
            listaActual.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            checkJugadaActual = new JCheckBox("Jugada " + (i + 1) + ":");
            listaCheckJugada.add(checkJugadaActual);
            panelActual.add(checkJugadaActual);
            //panelActual.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            panelActual.add(listaActual);
            for (ICarta carta: jugadasEnMesa.getJugada().get(i).getCartasJugada()) {
                imagenActual = "ar/edu/unlu/poo/images/cartas/jugada/" + carta.getNumero() + carta.getPalo()+ ".png";
                cartaActual = new ImageIcon(imagenActual);
                listaModeloActual.addElement(cartaActual);
            }
        }
        txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-Hay nuevas jugadas en la mesa!!!");
    }

    @Override
    public void cerrarPartida() {
        volverAInicio();
        if (controlador.getModoJuego().equals("EXPRES")){
            txtInfoInicio.setText("\nLa partida fue cerrada ya que no se pueden  hacer combinaciones o añadir cartas ");
            txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-Se cerro la partida porque no se pueden hacer nuevas jugadas y las jugadas en mesa estan llenas.");
            if (!controlador.getEstadoCompetitivo()){
                txtInfoInicio.setText(txtInfoInicio.getText() + "\nNo se ganaron ni puntos ni fichas apostadas porque el competitivo esta desactivado.");
                txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-Las partidas competitivas estan desactivas por lo que tampoco se ganaron puntos ni fichas :c");
            }
            eleccionNuevaPartida();
        }else {
            txtInfoInicio.setText("\nLa ronda fue cerrada por lo que se sumaran los puntos sobrantes a cada jugador e iniciara una nueva ronda...");
            txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-La ronda quedo cerrada por tener jugadas llenas y por no poder hacer nuevas jugadas. Pronto iniciara una nueva ronda!");
            controlador.iniciarNuevaRonda();
        }
    }

    @Override
    public void mostrarErrorApuesta() {
        pantallaEspera();
        txtInfoInicio.setText("\nApuestas Desactivadas!!!");
        txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-Se cancelaron las apuestas!!!");
    }

    @Override
    public void avisarSobreApuesta() {
        if (controlador.getcantidadApostada() != 0) {
            txtInfoInicio.setText(txtInfoInicio.getText() + "\nHay apuestas Activas de " + controlador.getcantidadApostada() + " fichas!!! \nPara desactivarlas apueste 0 fichas");
            txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-Las apuestas fueron activadas. En el caso que quieras desactivarlas puedes apostar 0 fichas y se cancelaran y en caso contrario todos apostaran " + controlador.getcantidadApostada() + " fichas.");
        }
    }

    @Override
    public void mostrarResultadosPuntosRonda(ArrayList<IJugador> jugadores) {
        txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-Ahora mismo podes visualizar los resultados en puntos de la ronda actual en el panel resumen de partida mientras se prepara la nueva ronda.");
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
    }

    @Override
    public void finalizarPartidaAmistosamente() {
        volverAInicio();
        txtInfoInicio.setText("\nLa partida ha finalizado Amistosamente!!! Se devolvieron apuestas actuales y los puntos no cuentan");
        txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-La partida finalizo amistosamente por lo que se devolvieron las apuestas si estaban activas y no contaron los puntos.");
        panelAbajo.setEnabled(false);
        panelOpciones.setEnabled(false);
        eleccionNuevaPartida();
    }

    private void volverAInicio() {
        panelInicio.setName("Inicio");
        tabbedPane.add(panelInicio, 0);
        tabbedPane.remove(panelPartida);
    }

    private void eleccionNuevaPartida() {
        controlador.comprobarAnfitrion();
        if (controlador.esAnfitrion()) {
            txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-Si deseas volver a jugar una nueva partida ve al menu de inicio y presiona nueva partida. \nEn caso contrario, espero tengas un buen dia!! :D ");
            txtInfoInicio.setText(txtInfoInicio.getText() + "\n¿Desea Iniciar una nueva partida?");
            iniciarNuevaPartidaButton.setVisible(true);
            salirButton.setVisible(true);
        }else {
            txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-Espera a que el anfitrion decida si quiere una nueva partida. En caso que abandone el juego esta la posibilidad de que uno del resto de jugadores se convierta en el nuevo anfitrion.");
        }
    }

    @Override
    public void eleccionAnularPartida() {
        seleccionAnularPartida = true;
        anularPartidaButton.setForeground(new Color(229,213,7));
    }

    @Override
    public void obtenerNombre() {
        txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-Solo escribe tu nombre e inicia sesion");
        panelUsuario.setVisible(true);
        txtNombre.setEnabled(true);
        iniciarSesionButton.setEnabled(true);
    }

    @Override
    public void cerrarJuego() {
        controlador.eliminarJugador();
        System.exit(0);
        //cambiar directamente sacar al anfitrion y hacerle la pregunta al siguiente anfitrion y si elige que no cerrarle su ventana
    }

    @Override
    public void mostrarTablaPosiciones(ArrayList<IJugador> jugadores) {
        txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-Puedes visualizar la tabla de posiciones en el panel del mismo nombre. En caso que no aparezcas es porque no alcanzaste el top de jugadores :(");
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
                       i + "- " + jugadorAux.getNombre() +  "\n");
                txtPuntosJugadorTablaPosiciones.setText(txtPuntosJugadorTablaPosiciones.getText() + jugadorAux.getPuntosTotalesXP() + "\n");
            }
        }
    }

    @Override
    public void mostrarErrorConexion() {
        txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-Parece que has tenido un problema con tu conexion :(");
        txtTurno.setText("Hubo un problema con tu conexion.");
    }

    @Override
    public void errorCantidadJugadores() {
        txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-ERROR: cantidad de jugadores insuficiente.");
        volverAInicio();
    }

    @Override
    public void mostrarJugadorSalioDelJuego() {
        volverAInicio();
        txtInfoInicio.setText("Un jugador Ha salido");
        txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now()+ "|-Un jugador ha salido del juego.");
        actualizarBarra();
        eleccionNuevaPartida();
    }

    @Override
    public void avisarCambiosOpcionesMesa() {
        tabbedPane.setForegroundAt(1,new Color(229,11,9));
        txtAsistenteAyuda.setText(txtAsistenteAyuda.getText() +"\n|" + LocalDateTime.now() + "|-El anfitrion realizo cambios en las opciones de mesa del juego.");
        //panelOpcionesMesa.setForeground(new Color(229,11,9));
    }
}
