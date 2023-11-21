package no.Usar;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.Carta;
import ar.edu.unlu.poo.modelo.ICarta;
import ar.edu.unlu.poo.modelo.Observer;
import ar.edu.unlu.poo.modelo.Palo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class VentanaViejaNOUSAR extends JFrame implements ActionListener, Observer{
    private JFrame frame;
    private JFrame frameEspera;
    private JLabel etiqueta2;
    private Controlador controlador;
    private JLabel cantJugadores;
    private JPanel panelPrincipal;
    private JButton mazo;
    private JPanel menuJugadorAbajo;
    private JPanel cartasJugadorAbajo;
    private JPanel menuJugadorDerecha;
    private JPanel cartasJugadorDerecha;
    private JPanel menuJugadorIzquierda;
    private JPanel cartasJugadorIzquierda;

    private JPanel menuJugadorArriba;
    private JPanel cartasJugadorArriba;
    private Integer cantidadJugadores = 0;
    private Integer jugadoresEnVentana = 0;


    public VentanaViejaNOUSAR() throws RemoteException {

    }


    public void iniciarVentana(String nombreJugador, boolean esAnfitrion) throws RemoteException {
        frame = new JFrame("Rummy Beta - Version 0.0");
        frame.setSize(1500,1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panelPrincipal = new JPanel();
        //JLabel etiqueta1 = new JLabel("Seleccione una opcion");
        panelPrincipal = (JPanel) frame.getContentPane();
        panelPrincipal.setLayout(new BorderLayout());
        //panelPrincipal.add(etiqueta1,BorderLayout.NORTH);

        //jugador de abajo
        menuJugadorAbajo = new JPanel();
        menuJugadorAbajo.setLayout(new BorderLayout());

        cartasJugadorAbajo = new JPanel();
        cartasJugadorAbajo.setLayout(new FlowLayout());

        String anfitrion = " ";
        if (esAnfitrion){
            anfitrion = "|Estrella|";
            //agregar boton de opciones de mesa
        }
        etiqueta2 = new JLabel(" "+ nombreJugador + " " + anfitrion, SwingConstants.CENTER);
        cantidadJugadores++;

        menuJugadorAbajo.add(etiqueta2,BorderLayout.NORTH);

        menuJugadorAbajo.add(cartasJugadorAbajo, BorderLayout.CENTER);

        panelPrincipal.add(menuJugadorAbajo,BorderLayout.SOUTH);

        //jugador de la derecha
        menuJugadorDerecha = new JPanel();
        menuJugadorDerecha.setLayout(new BorderLayout());


        cartasJugadorDerecha = new JPanel();
        cartasJugadorDerecha.setLayout(new FlowLayout());



        menuJugadorDerecha.add(etiqueta2,BorderLayout.NORTH);
        menuJugadorDerecha.add(cartasJugadorDerecha, BorderLayout.CENTER);
        panelPrincipal.add(menuJugadorDerecha,BorderLayout.EAST);

        //jugador de la izquierda
        menuJugadorIzquierda = new JPanel();
        menuJugadorIzquierda.setLayout(new BorderLayout());


        cartasJugadorIzquierda = new JPanel();
        cartasJugadorIzquierda.setLayout(new FlowLayout());


        menuJugadorIzquierda.add(etiqueta2,BorderLayout.NORTH);
        menuJugadorIzquierda.add(cartasJugadorIzquierda, BorderLayout.CENTER);
        panelPrincipal.add(menuJugadorIzquierda,BorderLayout.WEST);

        //jugador de arriba
        menuJugadorArriba = new JPanel();
        menuJugadorArriba.setLayout(new BorderLayout());


        cartasJugadorArriba = new JPanel();
        cartasJugadorArriba.setLayout(new FlowLayout());


        menuJugadorArriba.add(cartasJugadorArriba, BorderLayout.CENTER);
        panelPrincipal.add(menuJugadorArriba,BorderLayout.NORTH);

        //tablero del centro
        JPanel menuTablero = new JPanel();
        menuTablero.setLayout(new BorderLayout());

        mazo = new JButton("mazo");
        mazo.addActionListener(this);
        mazo.setMinimumSize(new Dimension(20,20));
        mazo.setMaximumSize(new Dimension(80,120));
        mazo.setPreferredSize(new Dimension(80,120));

        JButton bocaArriba = new JButton("cartas Boca Arriba");
        bocaArriba.addActionListener(this);
        bocaArriba.setMinimumSize(new Dimension(20,20));
        bocaArriba.setMaximumSize(new Dimension(80,120));
        bocaArriba.setPreferredSize(new Dimension(80,120));
        JButton jugada1 = new JButton("carta Jugada 1");
        //arrayList de Jbutton?
        jugada1.addActionListener(this);
        jugada1.setMinimumSize(new Dimension(20,20));
        jugada1.setMaximumSize(new Dimension(50,70));
        jugada1.setPreferredSize(new Dimension(50,70));

        JButton jugada2 = new JButton("carta Jugada 2");
        //arrayList de Jbutton?
        jugada2.addActionListener(this);
        jugada2.setMinimumSize(new Dimension(20,20));
        jugada2.setMaximumSize(new Dimension(50,70));
        jugada2.setPreferredSize(new Dimension(50,70));
        JButton jugada3 = new JButton("carta Jugada 3");
        //arrayList de Jbutton?
        jugada3.addActionListener(this);
        jugada3.setMinimumSize(new Dimension(20,20));
        jugada3.setMaximumSize(new Dimension(50,70));
        jugada3.setPreferredSize(new Dimension(50,70));

        JPanel cartasJugadas = new JPanel();
        cartasJugadas.setLayout(new FlowLayout());
        cartasJugadas.add(jugada1);
        cartasJugadas.add(jugada2);
        cartasJugadas.add(jugada3);

        JPanel mazoYbocaArriba = new JPanel();
        mazoYbocaArriba.setLayout(new FlowLayout());
        mazoYbocaArriba.add(mazo);
        mazoYbocaArriba.add(bocaArriba);

        menuTablero.add(mazoYbocaArriba, BorderLayout.EAST);
        menuTablero.add(cartasJugadas,BorderLayout.CENTER);
        panelPrincipal.add(menuTablero,BorderLayout.CENTER);

        //botones adicionales
        JPanel panelBotonesIzquierda = new JPanel();
        panelBotonesIzquierda.setLayout(new FlowLayout(FlowLayout.LEADING));
        //ponerlo sino hacia arriba el layout
        JButton botonJugada = new JButton("Crear Jugada");
        JButton botonFinTurno = new JButton("Terminar Turno");
        panelBotonesIzquierda.add(botonJugada);
        panelBotonesIzquierda.add(botonFinTurno);

        menuJugadorIzquierda.add(panelBotonesIzquierda, BorderLayout.SOUTH);

        JPanel panelBotonesDerecha = new JPanel();
        panelBotonesDerecha.setLayout(new FlowLayout(FlowLayout.LEADING));
        //ponerlo sino hacia arriba el layout
        JButton botonListo = new JButton("Listo");
        JButton botonCancelar = new JButton("Cancelar");
        panelBotonesDerecha.add(botonListo);
        panelBotonesDerecha.add(botonCancelar);
        jugadoresEnVentana++;
        menuJugadorDerecha.add(panelBotonesDerecha, BorderLayout.SOUTH);
        if (cantidadJugadores >= 2){
            ArrayList<String> oponentes = controlador.nombreOponentes(nombreJugador);
            for (int i = 0; i < oponentes.size();i++){
                agregarOponente(oponentes.get(i));
                jugadoresEnVentana++;
            }
        }
        frame.setVisible(true);
        frameEspera.setVisible(false);
    }


    public void actualizarCarta(Carta cambio) {

    }


    public void pantallaEspera(boolean anfitrion) throws InterruptedException, RemoteException {
        if (anfitrion){
            iniciarVentanaEsperaAnfitrion();
        }else {
            iniciarVentanaEspera();
            //esta ventana deberia de terminar cuando el anfitrion comience el juego
        }
    }


    public void actualizarCantJugadores() throws RemoteException {
        //cantidadJugadores++;
        String jugadores = String.valueOf(controlador.cantJugadores());
        cantJugadores.setText(jugadores);
    }


    public void cerrarPantallaEspera() {

    }


    public void actualizarCartas(int numero, Palo palo) {
        JButton nuevoBotonCarta = new JButton("N:" + numero + "P:" + palo);
        nuevoBotonCarta.addActionListener(this);
        nuevoBotonCarta.setMinimumSize(new Dimension(20,20));
        nuevoBotonCarta.setMaximumSize(new Dimension(240,240));
        nuevoBotonCarta.setPreferredSize(new Dimension(120,200));
        cartasJugadorAbajo.add(nuevoBotonCarta);
    }

    public void agregarOponente(String nombreOponente){
        etiqueta2 = new JLabel(" " + nombreOponente,SwingConstants.CENTER);
        etiqueta2.setName(nombreOponente);
        if (jugadoresEnVentana == 1){
            menuJugadorDerecha.add(etiqueta2,BorderLayout.NORTH);
        } else if (jugadoresEnVentana == 2) {
            menuJugadorArriba.add(etiqueta2,BorderLayout.NORTH);
        } else if (jugadoresEnVentana == 3) {
            menuJugadorIzquierda.add(etiqueta2,BorderLayout.NORTH);
        }
        //cantidadJugadores++;
    }


    public void agregarCartaOtroJugador(String nombreJugador) {
        JButton nuevoBotonCarta = new JButton("reverso");
        nuevoBotonCarta.addActionListener(this);
        nuevoBotonCarta.setMinimumSize(new Dimension(20,20));
        nuevoBotonCarta.setMaximumSize(new Dimension(40,60));
        nuevoBotonCarta.setPreferredSize(new Dimension(40,60));
        for (int i = 0;i < menuJugadorDerecha.getComponentCount();i++){
            if (menuJugadorDerecha.getComponent(i).getName() != null){
                if (menuJugadorDerecha.getComponent(i).getName().equals(nombreJugador)){
                    cartasJugadorDerecha.add(nuevoBotonCarta);
                }
            }
        }
        for (int i = 0;i < menuJugadorIzquierda.getComponentCount();i++) {
            if (menuJugadorIzquierda.getComponent(i).getName() != null){
                if (menuJugadorIzquierda.getComponent(i).getName().equals(nombreJugador)) {
                    cartasJugadorIzquierda.add(nuevoBotonCarta);
                }
            }
        }
        for (int i = 0;i < menuJugadorArriba.getComponentCount();i++){
            if (menuJugadorArriba.getComponent(i).getName() != null){
                if (menuJugadorArriba.getComponent(i).getName().equals(nombreJugador)){
                    cartasJugadorArriba.add(nuevoBotonCarta);
                }
            }
        }
    }


    public void iniciarTurno() throws RemoteException {

    }


    public void esperarTurno() throws RemoteException {

    }


    public void darControl() {

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

    private void iniciarVentanaEsperaAnfitrion() throws InterruptedException, RemoteException {
        frameEspera = new JFrame("Rummy Beta - Version 0.0");
        frameEspera.setSize(1500,1000);
        frameEspera.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cantJugadores = new JLabel();
        String jugadores = String.valueOf(controlador.cantJugadores());
        JLabel etiqueta1 = new JLabel("Esperando a que se unan jugadores...\nCantidad de jugadores Actual: ");
        cantJugadores.setText(jugadores);
        controlador.nuevoJugador(true);
        JPanel panelPrincipal = (JPanel) frameEspera.getContentPane();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.add(cantJugadores,BorderLayout.CENTER);
        panelPrincipal.add(etiqueta1, BorderLayout.WEST);

        JButton iniciarPartida = new JButton("Iniciar Partida");
        iniciarPartida.addActionListener(this);
        iniciarPartida.setMinimumSize(new Dimension(20,20));
        iniciarPartida.setMaximumSize(new Dimension(50,70));
        iniciarPartida.setPreferredSize(new Dimension(50,70));

        panelPrincipal.add(iniciarPartida,BorderLayout.SOUTH);
        frameEspera.setVisible(true);
    }

    private void iniciarVentanaEspera() throws InterruptedException, RemoteException {
        frameEspera = new JFrame("Rummy Beta - Version 0.0");
        frameEspera.setSize(1500,1000);
        frameEspera.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String jugadores = String.valueOf(controlador.cantJugadores());
        JLabel etiqueta1 = new JLabel("Esperando a que el anfitrion comience el juego...\nCantidad de jugadores Actual: ");
        cantJugadores = new JLabel();
        cantJugadores.setText(jugadores);
        controlador.nuevoJugador(false);
        JPanel panelPrincipal = (JPanel) frameEspera.getContentPane();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.add(cantJugadores,BorderLayout.CENTER);
        panelPrincipal.add(etiqueta1, BorderLayout.WEST);
        frameEspera.setVisible(true);

    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        if (comando.equals("Iniciar Partida")){
            controlador.iniciarJuego();
            //cierro la ventana actual para iniciar el juego
        }
    }

    @Override
    public void notificarCambio(Object o) throws RemoteException {

    }

}
