package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.exceptions.NoHayCartaBocaArriba;
import ar.edu.unlu.poo.modelo.ICarta;
import ar.edu.unlu.poo.modelo.IJugador;
import ar.edu.unlu.poo.vistas.consola.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class VistaConsola implements IVista{
    private boolean cambiosOpcionesMesa;

    public boolean esNumero(String txtIngresado) {
        boolean resultado;
        try{
            Integer.parseInt(txtIngresado);
            resultado = true;
        }catch (NumberFormatException e){
            resultado = false;
        }
        return resultado;
    }

    public void errorRangoNumerico() {
        print("Numero fuera de rango.");
    }

    public boolean hayJugadasSinVer() {
        return jugadasSinVer;
    }

    public void reiniciarTablaRonda() {
        String jugadorActual;
        for (int i = 0; i < controlador.cantJugadores(); i++) {
            jugadorActual = controlador.obtenerJugador(i);
            acumuladorTablaFinRonda = "| " + jugadorActual + " | Puntos";
        }
    }

    public Flujo flujoActual() {
        return flujoActual;
    }
    private JFrame frame;
    private JPanel panelConsola;
    private JTextArea txtAreaMuestra;
    private JTextField txtConsola;
    private JButton enterButton;

    private Controlador controlador;
    private boolean jugadorAgregado = false;

    private boolean jugadasSinVer = false;

    private boolean hayApuesta = false;

    private Timer temporizador;
    private Timer tiempoTurno;
    private int tiempoRestante;
    private String txtActual;
    private TimerTask juegoAutomatico;
    private TimerTask mostrarTiempoActual;

    private Flujo flujoActual;
    private String acumuladorTablaFinRonda;

    private boolean modoChat;

    public VistaConsola() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame("Rummy - Version 1.0");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setContentPane(panelConsola);
                frame.pack();
                frame.setVisible(true);
                txtAreaMuestra.setEditable(false);
                cambiosOpcionesMesa = false;

                tiempoRestante = 0;

                enterButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        procesarTexto(txtConsola.getText());
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
                txtConsola.addKeyListener(new KeyListener() {
                                              @Override
                                              public void keyTyped(KeyEvent e) {
                                                  //sin uso
                                              }
                                              @Override
                                              public void keyPressed(KeyEvent e) {
                                                  if (e.getKeyCode() == KeyEvent.VK_ENTER){
                                                      procesarTexto(txtConsola.getText());
                                                  }
                                              }
                                              @Override
                                              public void keyReleased(KeyEvent e) {
                                                //sin uso
                                              }
                                          }
                );
            }
        });
    }

    private void procesarTexto(String txtIngresado){
        if (!modoChat) {
            print(txtIngresado);
        }
        txtConsola.setText("");
        txtIngresado = txtIngresado.trim();
        flujoActual = flujoActual.procesarEntrada(txtIngresado);
    }

    public void print(String txtActual){
        txtAreaMuestra.setText(txtAreaMuestra.getText() + txtActual + "\n");
    }

    public Flujo mostrarOpcionesNuevaPartida(String txtIngresado, Flujo flujoActual) {
        txtIngresado = txtIngresado.toUpperCase();
        if (txtIngresado.equals("Y")){
            controlador.nuevoJuego();
        }else if (txtIngresado.equals("N")){
            controlador.eliminarJugador();
            System.exit(0);
        }else {
            opcionIncorrecta();
        }
        return flujoActual;
    }

    public String mostrarTiempoActual() {
        int tiempoActual = controlador.getTiempoPorTurno();
        String resultado;
        if (tiempoActual == 0){
            resultado = "DESACTIVADO";
        }else {
            resultado = tiempoActual + " segundos";
        }
        return resultado;
    }

    public String obtenerEstado(boolean estado) {
        String resultado;
        if (estado){
            resultado = "ACTIVADO";
        }else {
            resultado = "DESACTIVADO";
        }
        return resultado;
    }


    public void mostrarSeleccionCartas(){
        mostrarCartas();
        print("\n seleccione las cartas segun su POSICION (empezando por la 1) \nuna vez finalizado presione 0 para continuar");
        guardarTxtActual();
    }

    public void opcionIncorrecta(){
        print("Opcion incorrecta!!!");
    }


    public String procesarCambiosMesa() {
        String txtOpcionMesa = "4-Opciones de mesa (solo disponible para el jefe de mesa)";
        if (cambiosOpcionesMesa){
            //cambiar color a rojo del txt
        }
        return txtOpcionMesa;
    }


    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void pantallaEspera() {
        if (!modoChat) {
            if (!jugadorAgregado && !controlador.partidaCargada()) {
                jugadorAgregado = true;
                flujoActual = new FlujoObtenerNombre(this, controlador);
            } else {
                if (controlador.partidaCargada() && controlador.getNombreJugador() == null) {
                    controlador.activarNuevoJugador();
                }
                flujoActual = new FlujoEsperaPartida(this, controlador);
            }
        }else {
            print("Se esta esperando a que se inicie la partida");
        }
    }


    public void limpiarPantalla(){
        txtAreaMuestra.setText(" ");
    }

    @Override
    public void actualizarCantJugadores(){
        if (!modoChat) {
            flujoActual = new FlujoEsperaPartida(this, controlador);
            //print("Se ha unido un nuevo jugador a la partida!!!");
        }else {
            print("Se esta esperando a que inicie la partida. Cantidad de jugadores " + controlador.cantJugadores() + ". Activos: " + controlador.getJugadoresActivos());
        }
    }

    @Override
    public void iniciarTurno(){
        if (!modoChat) {
            flujoActual = new FlujoInicioTurno(this, controlador);
        }
    }

    public void cambiarEstadoConsola(boolean estado) {
        txtConsola.setEnabled(estado);
    }

    public void activarTiempoDeTurno() {
        tiempoTurno = new Timer();
        temporizador = new Timer();
        crearJuegoAutomatico();
        crearTareaTemporizador();
        tiempoRestante = controlador.getTiempoPorTurno();
        temporizador.schedule(mostrarTiempoActual,0,1000);
        if (controlador.getTiempoPorTurno() == 60){
            tiempoTurno.schedule(juegoAutomatico,60000);
        }else {
            tiempoTurno.schedule(juegoAutomatico,120000);
        }
    }

    private void crearTareaTemporizador() {
        mostrarTiempoActual = new TimerTask() {
            @Override
            public void run() {
                txtAreaMuestra.setText(txtActual + "\nTiempo restante: " + tiempoRestante);
                tiempoRestante--;
                if (tiempoRestante == -1){
                    temporizador.cancel();
                }
            }
        };
    }

    private void crearJuegoAutomatico() {
        juegoAutomatico = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Se acabo el tiempo :(");
                tiempoTurno.cancel();
                txtAreaMuestra.setText("Se acabo el tiempo!!! Tu turno se jugara automaticamente.");
                controlador.iniciarJuegoAutomatico();
            }
        };
    }


    private String obtenerCartaDescarte() {
        try {
            return controlador.getCartaDescarte().toString();
        } catch (NoHayCartaBocaArriba e) {
            return " No hay carta de descarte";
        }
    }

    public void guardarTxtActual() {
        txtActual = txtAreaMuestra.getText();
    }


    public void mostrarCartas(){
        ArrayList<ICarta> cartasActuales = controlador.obtenerCartas();
        print("\nTus cartas:");
        mostrarPosiciones(cartasActuales);
        print("\n");
        for (int i = 0; i < cartasActuales.size(); i++) {
            txtAreaMuestra.setText(txtAreaMuestra.getText() + cartasActuales.get(i));
            if (i == 10 || i == 20){
                txtAreaMuestra.setText(txtAreaMuestra.getText() + "\n");
            }
        }
    }

    private void mostrarPosiciones(ArrayList<ICarta> cartasActuales) {
        txtAreaMuestra.setText(txtAreaMuestra.getText() + "\n");
        int tamanioString;
        for (int i = 0; i < cartasActuales.size(); i++) {
            tamanioString = cartasActuales.get(i).toString().length();
            if (tamanioString < 7){
                txtAreaMuestra.setText(txtAreaMuestra.getText() + "Pos " + (i + 1) + " ");
            }else {
            for (int j = 0; j <= tamanioString - 3; j++) {
                if (tamanioString /2 == j){
                    txtAreaMuestra.setText(txtAreaMuestra.getText() + (i + 1));
                }else {
                    txtAreaMuestra.setText(txtAreaMuestra.getText() + "_");
                }
            }
                //txtAreaMuestra.setText(txtAreaMuestra.getText() + (i + 1) + "\t");
            }
        }
    }

    @Override
    public void nuevoTurno(){
        if (!modoChat) {
            if (controlador.esTurnoJugador()) {
                flujoActual = new FlujoInicioTurno(this, controlador);
            } else {
                flujoActual = new FlujoEsperaTurno(this, controlador);
            }
        }else {
            print("Se Ha iniciado un nuevo turno. Recuerde usar /mostrarNombreTurnoActual para saber de quien se trata.");
        }
    }

    @Override
    public void continuarTurnoActual(){
        if (!modoChat) {
            if (controlador.esTurnoJugador()) {
                flujoActual = new FlujoContinuarTurno(this, controlador);
            } else {
                flujoActual = new FlujoEsperaTurno(this, controlador);
            }
        }else {
            print("Se esta continuando el turno Actual. Recuerde usar /mostrarNombreTurnoActual para saber de quien se trata.");
        }
    }

    public void mostrarAvisoEliminado() {
        if (controlador.apuestasActivadas()) {
            print("Usted ha sido eliminado de la partida por sobrepasar la cantidad de puntos.");
            print("¿Quiere reengancharse? (si acepta debe apostar la mitad de lo que aposto en un inicio)");
            print("Tus fichas: " + controlador.cantFichas() + " | Cantidad apostada: " + controlador.getcantidadApostada());
            print("1-Para Reengancharse");
        } else {
            print("Usted ha sido eliminado de la partida por sobrepasar la cantidad de puntos.");
            print("No puede reengancharse porque no hay apuestas activas :(");
        }
        txtConsola.setEnabled(true);
    }

    @Override
    public void finalizarPartida() {
        if (!modoChat) {
            flujoActual = new FlujoFinPartida(this, controlador);
        }else {
            print("La partida ha finalizado!!!");
            print("El ganador es..." + controlador.getGanador() +
                    " con " + controlador.getCantidadPuntosGanador()+" puntos");
            if (!controlador.getEstadoCompetitivo()){
                print("\nNo se ganaron ni puntos ni fichas apostadas porque el competitivo esta desactivado.");
            }
            mostrarTablaPosiciones(controlador.obtenerPosiciones());
        }
    }

    public void mostrarOpcionesNuevaPartida() {
        //comprueba cual es el anfitrion antes de dar la eleccion por si hubo un cambio inesperado
        if (controlador.esAnfitrion()){
            txtAreaMuestra.setText(txtAreaMuestra.getText() + "\n¿Desea iniciar una nueva partida? (Y/N) (Y para si, N para no)");
        }else {
            txtConsola.setEnabled(false);
            txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nEl anfitrion esta decidiendo si iniciar una nueva partida...");
        }
    }

    @Override
    public void actualizarJugadas(){
        if (!modoChat) {
            jugadasSinVer = true;
        }else {
            print("Hay nuevas jugadas sin ver!!! escriba el comando /mostrarJugadas para verlas");
        }
    }

    @Override
    public void cerrarPartida() {
        if (!modoChat) {
            flujoActual = new FlujoPartidaCerrada(this, controlador);
        }else {
            if (controlador.getModoJuego().equals("EXPRES")){
                print("La partida fue cerrada ya que no se pueden  hacer combinaciones o añadir cartas ");
                if (!controlador.getEstadoCompetitivo()){
                    print("No se ganaron ni puntos ni fichas apostadas porque el competitivo esta desactivado.");
                }
                //controlador.obtenerPosiciones();
                mostrarTablaPosiciones(controlador.obtenerPosiciones());
            }else {
                print("La ronda fue cerrada por lo que se sumaran los puntos sobrantes a cada jugador e iniciara una nueva ronda...");
            }
        }
    }

    @Override
    public void mostrarApuestaCancelada(){
        if (!modoChat) {
            hayApuesta = false;
            print("\nSe cancelaron las apuestas!!!");
        }else {
            print("Se cancelaron las apuestas en la partida.");
        }
    }

    @Override
    public void avisarSobreApuesta() {
        if (!modoChat) {
            if (!controlador.esAnfitrion()) {
                hayApuesta = true;
            }
        }else {
            print("Los jugadores tienen las apuestas activas!!");
        }
    }

    @Override
    public void mostrarResultadosPuntosRonda(ArrayList<IJugador> jugadores) {
        print("Puntos de los jugadores (el limite es 300):");
        acumuladorTablaFinRonda += "\n";
        for (int i = 0; i < controlador.cantJugadores(); i++) {
            acumuladorTablaFinRonda += jugadores.get(i).getNombre() + "\t" + jugadores.get(i).getPuntosDePartida() + "\t";
        }
        print(acumuladorTablaFinRonda);
        if (!modoChat) {
            flujoActual = new FlujoEsperaNuevaRonda(this, controlador);
        }
    }

    @Override
    public void finalizarPartidaAmistosamente() {
        if (!modoChat) {
            flujoActual = new FlujoFinPartidaAmistosa(this, controlador);
        }else {
            print("Se finalizo la partida amistosamente!!! Se devolvieron apuestas actuales y los puntos no cuentan");
        }
    }

    @Override
    public void eleccionAnularPartida() {
        if (!modoChat) {
            flujoActual = new FlujoAnularPartida(this, controlador);
        }else {
            print("Se esta solicitando anular la partida.");
        }
    }

    @Override
    public void obtenerNombre() {
        flujoActual = new FlujoObtenerNombre(this, controlador);
    }

    @Override
    public void cerrarJuego() {
        controlador.eliminarJugador();
        System.exit(0);
    }

    @Override
    public void mostrarTablaPosiciones(ArrayList<IJugador> jugadores) {
        txtAreaMuestra.setText(txtAreaMuestra.getText() +
                "\nTabla de posiciones (top 5 jugadores):");
        IJugador jugadorAux;
        for (int i = 0; i < 5; i++) {
            if (i < jugadores.size()){
                jugadorAux = jugadores.get(i);
                txtAreaMuestra.setText(txtAreaMuestra.getText() +
                        "\n" + (i + 1) + "- Nombre: " + jugadorAux.getNombre() + " | Puntos de XP: " + jugadorAux.getPuntosTotalesXP());
            }
        }
    }

    @Override
    public void mostrarErrorConexion() {
        System.out.println("Error remoto");
        limpiarPantalla();
        txtAreaMuestra.setText(txtAreaMuestra.getText() + "Error: hubo un problema con la conexion remota");
    }

    @Override
    public void errorCantidadJugadores() {
        limpiarPantalla();
        flujoActual = new FlujoEsperaPartida(this, controlador);
        print("Error: faltan jugadores para comenzar la partida.");
    }

    @Override
    public void mostrarJugadorSalioDelJuego() {
        print("Un Jugador Ha salido Del Juego.");
    }

    @Override
    public void avisarCambiosOpcionesMesa() {
        if (!modoChat) {
            cambiosOpcionesMesa = true;
            if (!controlador.esTurnoJugador()) {
                print("\nEl anfitrion hizo cambios en las opciones de mesa!!!");
            }
        }else {
            flujoActual = new FlujoChatPublico(this, controlador);
            print("\nEl anfitrion hizo cambios en las opciones de mesa!!!");
        }
    }

    public void mostrarPartidasDisponibles(ArrayList<String> partidaDisponible) {
        print("Partidas disponibles:");
        for (int i = 0; i < 8; i++) {
            if (i < partidaDisponible.size()) {
                print((i + 1) + "-" + partidaDisponible.get(i));
            }else {
                print((i + 1) + "- <Espacio disponible para guardar partida>");
            }
        }
    }
    @Override
    public void activarSoloChat() {
        modoChat = true;
        flujoActual = new FlujoChatPublico(this, controlador);
    }

    @Override
    public void mostrarNuevoMensaje(String mensajeNuevo) {
        print(mensajeNuevo);
    }

    @Override
    public void mostrarErrorJugadaLLena() {
        print("Error: La jugada seleccionada esta llena.");
    }

    @Override
    public void mostrarErrorCartaNoAgregada() {
        print("Error: Las cartas seleccionadas para agregar no forman parte de la jugada seleccionada.");
    }

    @Override
    public void mostrarErrorNoEsJugada() {
        print("Error: Las cartas seleccionadas no forman esa jugada.");
    }

    @Override
    public void mostrarErrorCartasInsuficientes() {
        print("Error: Faltan cartas para armar la jugada");
    }

    @Override
    public void mostrarErrorRummyNoDisponible() {
        print("Error: La opcion de rummy no esta disponible porque ya hizo una jugada anteriormente.");
    }
}