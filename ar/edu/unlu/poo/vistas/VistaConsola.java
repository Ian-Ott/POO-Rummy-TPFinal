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



    enum EstadosPosibles{
        SIN_ESTADO,SELECCION_NOMBRE,PRIMERAS_OPCIONES,SELECCION_CARTAS,SELECCION_JUGADA, FIN_PARTIDA, POSIBLE_ANULAR_PARTIDA, CONTINUAR_TURNO, OPCIONES_DE_MESA, OPCIONES_TIEMPO, JUEGO_AUTOMATICO
    }

    enum EstadosJugadas{
        SIN_ESTADO,POSIBLE_RUMMY, POSIBLE_ESCALERA, POSIBLE_CARTA_PARA_JUGADA, POSIBLE_COMBINACION
    }
    private JFrame frame;
    private JPanel panelConsola;
    private JTextArea txtAreaMuestra;
    private JTextField txtConsola;
    private JButton enterButton;

    private Controlador controlador;
    private boolean jugadorAgregado = false;
    private ArrayList<Integer> posicionesSeleccionadas = new ArrayList<>();

    private boolean jugadasSinVer = false;

    private int posicionJugada;

    private boolean hayApuesta = false;

    private EstadosJugadas estadoActualJugada;

    private EstadosPosibles estadoActual;
    private boolean finTurno = false;
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
                //estadoActual = EstadosPosibles.SIN_ESTADO;

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
        //flujoActual.mostrarSiguienteTexto();

        /*String textoIngresado = txtConsola.getText().toLowerCase();
        txtConsola.setText("");
        if (controlador.juegoIniciado()){
            if (estadoActual.equals(EstadosPosibles.JUEGO_AUTOMATICO)){
                if(textoIngresado.equals("1")){
                    controlador.desactivarJuegoAutomatico();
                }
            }else if (estadoActual.equals(EstadosPosibles.PRIMERAS_OPCIONES)){
                seleccionarPrimerasOpciones(textoIngresado);
            } else if (estadoActual.equals(EstadosPosibles.SELECCION_CARTAS)) {
                seleccionarCartas(textoIngresado);
            } else if (estadoActual.equals(EstadosPosibles.SELECCION_JUGADA)) {
                seleccionarJugada(textoIngresado);
            } else if (estadoActual.equals(EstadosPosibles.FIN_PARTIDA)) {
                seleccionarOpcionesDePartida(textoIngresado);
            } else if (estadoActual.equals(EstadosPosibles.POSIBLE_ANULAR_PARTIDA)) {
                seleccionarOpcionesAnularPartida(textoIngresado);
            }else if (controlador.isEliminado()) {
                seleccionarOpcionesReenganche(textoIngresado);
            } else if(estadoActual.equals(EstadosPosibles.CONTINUAR_TURNO)) {
                seleccionarOpcionesTurno(textoIngresado);
            } else if (estadoActual.equals(EstadosPosibles.OPCIONES_DE_MESA)) {
                seleccionarOpcionesDeMesa(textoIngresado);
            } else if (estadoActual.equals(EstadosPosibles.OPCIONES_TIEMPO)) {
                seleccionarOpcionesTiempo(textoIngresado);
            }
        } else if (estadoActual.equals(EstadosPosibles.SELECCION_NOMBRE)){
            if (!controlador.estaEnElJuego(textoIngresado)){
                estadoActual = EstadosPosibles.SIN_ESTADO;
                controlador.nuevoJugador(controlador.esAnfitrion(), textoIngresado);
            }else {
                txtAreaMuestra.setText("Error: hay alguien con ese nombre en la partida!!!");
                obtenerNombre();
            }
        }else if (estadoActual.equals(EstadosPosibles.OPCIONES_DE_MESA)) {
            seleccionarOpcionesDeMesa(textoIngresado);
        }  else if (estadoActual.equals(EstadosPosibles.OPCIONES_TIEMPO)) {
            seleccionarOpcionesTiempo(textoIngresado);
        }else{
            if (controlador.esAnfitrion()){
                seleccionarOpcionesInicio(textoIngresado);
            }else {
                //opciones para cancelar la apuesta
                if (textoIngresado.equals("0")){
                    hayApuesta = false;
                    controlador.cancelarApuesta();
                    txtConsola.setEnabled(false);
                }else {
                    opcionIncorrecta();
                }
            }
        }*/
    }

    public void print(String txtActual){
        txtAreaMuestra.setText(txtAreaMuestra.getText() + txtActual + "\n");
    }

    /*private void seleccionarOpcionesTiempo(String textoIngresado) {
        if (textoIngresado.equals("1")) {
            controlador.setTiempoTurno(60);
        } else if (textoIngresado.equals("2")) {
            controlador.setTiempoTurno(120);
        } else if (textoIngresado.equals("3")) {
            controlador.setTiempoTurno(0);
        }
    }*/

    /*private void seleccionarOpcionesDeMesa(String textoIngresado) {
        if (textoIngresado.equals("1")) {
            mostrarOpcionesDeTiempo();
        } else if (textoIngresado.equals("2")) {
            controlador.modificarPartidasCompetitivas();
            continuarEnEstadoAnterior();
        } else if (textoIngresado.equals("3")) {
            controlador.modificarOpcionChat();
            continuarEnEstadoAnterior();
        } else if (textoIngresado.equals("4")) {
            controlador.activarModoExpres();
            txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nSe activo el modo Expres!!!");
            continuarEnEstadoAnterior();
        } else if (textoIngresado.equals("5")) {
            controlador.activarModoPuntos();
            txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nSe activo el modo Juego por Puntos!!!");
            continuarEnEstadoAnterior();
        } else if (textoIngresado.equals("0")) {
            continuarEnEstadoAnterior();
        }
    }*/

    private void continuarEnEstadoAnterior() {
        if (controlador.juegoIniciado()){
            continuarTurnoActual();
        }else {
            pantallaEspera();
        }
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

    /*private void mostrarOpcionesDeTiempo() {
        estadoActual = EstadosPosibles.OPCIONES_TIEMPO;
        txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nSeleccione la cantidad de tiempo que quiere por cada turno:" +
                "\n1-60 segundos por turno" +
                "\n2-120 segundos por turno" +
                "\n3-desactivar tiempo por turnos" +
                "\nTiempo actual: " + mostrarTiempoActual());
    }*/

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

    /*private void seleccionarOpcionesReenganche(String textoIngresado) {
        if (hayApuesta){
            String eleccion = textoIngresado.toUpperCase();
            if (eleccion.equals("Y")){
                controlador.hacerReenganche();
            }else {
                opcionIncorrecta();
            }
        }else {
            txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nLas apuestas estan desactivadas no es posible hacer el reenganche.");
        }
    }*/

    /*private void seleccionarOpcionesInicio(String textoIngresado) {
        //valida el texto sea un string
        if (textoIngresado.matches("[0-123456789]*")){
            int apuesta = Integer.parseInt(textoIngresado);
            if (textoIngresado.equals("1")){
                if (controlador.esAnfitrion()){
                    controlador.iniciarJuego();
                }else {
                    opcionIncorrecta();
                }
            } else if (textoIngresado.equals("2")) {
                mostrarOpcionesDeMesa();
            } else if (apuesta >= 250 && !hayApuesta) {
                hayApuesta = true;
                txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nApuestas Activadas!!!");
                controlador.apostar(apuesta);
            } else {
                opcionIncorrecta();
            }
        }else {opcionIncorrecta();}
    }*/

    /*private void seleccionarOpcionesTurno(String textoIngresado) {
        if (textoIngresado.equals("1")){
            limpiarPantalla();
            txtAreaMuestra.setText("Para hacer Rummy debe armar una escalera con todas sus cartas con la condicion de que no hizo una jugada anteriormente:");
            mostrarSeleccionCartas();
            estadoActualJugada = EstadosJugadas.POSIBLE_RUMMY;
        } else if (textoIngresado.equals("2")) {
            limpiarPantalla();
            txtAreaMuestra.setText("Hacer Escalera (minimo 3 cartas):");
            mostrarSeleccionCartas();
            estadoActualJugada = EstadosJugadas.POSIBLE_ESCALERA;
        }
        else if (textoIngresado.equals("3")) {
            limpiarPantalla();
            txtAreaMuestra.setText("Hacer Combinacion de numeros iguales (solo se puede entre 3-4 cartas):");
            estadoActualJugada = EstadosJugadas.POSIBLE_COMBINACION;
            mostrarSeleccionCartas();
        } else if (textoIngresado.equals("4")) {
            jugadasSinVer = false;
            //mostrarJugadasEnMesa();
        } else if (textoIngresado.equals("5")) {
            //mostrarCantidadCartas();
        } else if (textoIngresado.equals("6")) {
            mostrarOpcionesDeMesa();
        } else if (textoIngresado.equals("9")) {
            controlador.solicitarAnularPartida();
        } else if (textoIngresado.equals("0")) {
            finTurno = true;
            terminarTurno();
        }else {
            opcionIncorrecta();
        }
    }*/

    /*private void mostrarOpcionesDeMesa() {
        if (controlador.esAnfitrion()){
            estadoActual = EstadosPosibles.OPCIONES_DE_MESA;
            txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nSeleccione las opciones que quiera cambiar: " +
                    "\n1-Cambiar tiempo por cada turno (tiempo actual: " + " " + "segundos)" +
                    "\n2-Activar/Desactivar partidas competitivas, Estado actual: " + obtenerEstado(controlador.getEstadoCompetitivo()) + "(al desactivar esta opcion no se cuentan las fichas y los puntos obtenidos)" +
                    "\n3-Permitir publico y chat, Estado Actual: " + " " +
                    "\n4-Activar Ronda Expres (es una partida rapida de una ronda en la que gana el jugador que cierra antes)" +
                    "\n5-Activar Modo Por Puntos (Cuando un jugador alcanza los 300 puntos queda eliminado. El último jugador es el ganador de la partida)" +
                    "\nModo Actual: "+ controlador.getModoJuego() +
                    "\n0-Volver");
        }else {
            txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nNo podes modificar las opciones de mesa solo esta disponible para el anfitrion.");
        }
        guardarTxtActual();
    }*/

    public String obtenerEstado(boolean estado) {
        String resultado;
        if (estado){
            resultado = "ACTIVADO";
        }else {
            resultado = "DESACTIVADO";
        }
        return resultado;
    }

    /*private void seleccionarOpcionesAnularPartida(String textoIngresado) {
        String eleccion = textoIngresado.toUpperCase();
        if (eleccion.equals("Y") || eleccion.equals("N")){
            limpiarPantalla();
            txtAreaMuestra.setText("Usted tomo su eleccion. Esperando a que el resto de jugadores decida...");
            controlador.tomarDecisionDePartida(eleccion);
        } else {
            opcionIncorrecta();
        }
    }*/

    /*private void seleccionarOpcionesDePartida(String textoIngresado) {
        String eleccion = textoIngresado.toUpperCase();
        if (eleccion.equals("Y")){
            estadoActual = EstadosPosibles.SIN_ESTADO;
            controlador.nuevoJuego();
            //la consola vuelve al primer estado original donde se esperan jugadores (de hecho podrian entrar nuevos jugadores)
        } else if (eleccion.equals("N")) {
            cerrarJuego();
        }else {
            opcionIncorrecta();
        }
    }*/

    /*private void seleccionarJugada(String textoIngresado) {
        //valida el texto sea un string
        if (textoIngresado.matches("[0-123456789]*")){
        int numero = Integer.parseInt(textoIngresado);
        if (numero <= controlador.getJugadasSize() && numero > 0){
                posicionJugada = numero - 1;
                mostrarSeleccionCartas();
                estadoActualJugada = EstadosJugadas.POSIBLE_CARTA_PARA_JUGADA;
            } else if (numero == 0) {
                estadoActual = EstadosPosibles.CONTINUAR_TURNO;
                continuarTurnoActual();
            }else {
                opcionIncorrecta();
            }
        }else {opcionIncorrecta();}
    }*/

    /*private void seleccionarCartas(String textoIngresado) {
        int numero;
        //valida el texto sea un string
        if (textoIngresado.matches("[0-123456789]*")){
            numero = Integer.parseInt(textoIngresado);
            if (numero == 0){
                if (estadoActualJugada.equals(EstadosJugadas.POSIBLE_RUMMY)){
                    estadoActualJugada = EstadosJugadas.SIN_ESTADO;
                    if (posicionesSeleccionadas.isEmpty()){
                        continuarTurnoActual();
                    }else {
                        estadoActual = EstadosPosibles.CONTINUAR_TURNO;
                        controlador.armarRummy(posicionesSeleccionadas);
                        posicionesSeleccionadas.clear();
                    }
                } else if (estadoActualJugada.equals(EstadosJugadas.POSIBLE_ESCALERA)) {
                    estadoActualJugada = EstadosJugadas.SIN_ESTADO;
                    if (posicionesSeleccionadas.isEmpty()){
                        continuarTurnoActual();
                    }else {
                        estadoActual = EstadosPosibles.CONTINUAR_TURNO;
                        controlador.armarEscalera(posicionesSeleccionadas);
                        posicionesSeleccionadas.clear();
                    }
                } else if (estadoActualJugada.equals(EstadosJugadas.POSIBLE_COMBINACION)) {
                    estadoActualJugada = EstadosJugadas.SIN_ESTADO;
                    if (posicionesSeleccionadas.isEmpty()){
                        continuarTurnoActual();
                    }else {
                        estadoActual = EstadosPosibles.CONTINUAR_TURNO;
                        controlador.armarCombinacionIguales(posicionesSeleccionadas);
                        posicionesSeleccionadas.clear();
                    }
                } else if (estadoActualJugada.equals(EstadosJugadas.POSIBLE_CARTA_PARA_JUGADA)) {
                    estadoActualJugada = EstadosJugadas.SIN_ESTADO;
                    if (posicionesSeleccionadas.isEmpty()){
                        continuarTurnoActual();
                    }else {
                        controlador.agregarCartasAJugada(posicionesSeleccionadas, posicionJugada);
                    }
                    posicionesSeleccionadas.clear();
                } else if (finTurno && controlador.getCartasSize() == 0) {
                    cancelarTemporizador();
                    finTurno = false;
                    estadoActual = EstadosPosibles.PRIMERAS_OPCIONES;
                    controlador.terminarTurno(posicionesSeleccionadas);
                } else {

                    //excepcion
                }
            } else if (numero <= controlador.getCartasSize() && numero > 0) {
                agregarPosicion(numero - 1);
                txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nPosicion " + numero + " seleccionada!!!");
                if (finTurno){
                    cancelarTemporizador();
                    finTurno = false;
                    estadoActual = EstadosPosibles.PRIMERAS_OPCIONES;
                    posicionesSeleccionadas.clear();
                    controlador.terminarTurno(posicionesSeleccionadas);

                }
            }else {
                opcionIncorrecta();
            }
        } else {
            opcionIncorrecta();
            //excepcion
        }
    }*/

    /*private void cancelarTemporizador() {
        if (controlador.getTiempoPorTurno() != 0){
            temporizador.cancel();
            tiempoTurno.cancel();
        }
    }*/

    /*private void seleccionarPrimerasOpciones(String textoIngresado) {
        if (textoIngresado.equals("1")){
            controlador.tomarCartaMazo();
        } else if (textoIngresado.equals("2")) {
            controlador.tomarCartaDescarte();
        }else {
            opcionIncorrecta();
        }
    }*/



    /*private void agregarPosicion(int numero) {
        if (!posicionesSeleccionadas.contains(numero)){
            posicionesSeleccionadas.add(numero);
        }else {
            print("posicion ya seleccionada!!!");
        }
    }*/

    public void mostrarSeleccionCartas(){
        mostrarCartas();
        print("\n seleccione las cartas segun su POSICION (empezando por la 1) \nuna vez finalizado presione 0 para continuar");
        estadoActual = EstadosPosibles.SELECCION_CARTAS;
        guardarTxtActual();
    }

    public void opcionIncorrecta(){
        print("Opcion incorrecta!!!");
    }

    /*private void mostrarMenu() {
        limpiarPantalla();
        String bienvenida;
        if (controlador.esAnfitrion()){
            bienvenida = "\nBienvenido " + controlador.getNombreJugador() + "*";
        }else {
            bienvenida = "\nBienvenido " + controlador.getNombreJugador();
        }
        txtAreaMuestra.setText("----------------------------------------------------------" +
                "\n| Modo " + controlador.getModoJuego() + " |"+
                bienvenida +
                "\nTus fichas: " + controlador.cantFichas() + " Tu apuesta: " + controlador.getcantidadApostada() +
                "\nSeleccione una opcion para continuar su turno:" +
                "\n1-hacer Rummy" +
                "\n2-hacer escalera" +
                "\n3-hacer combinaciones de numeros iguales" +
                "\n4-ver jugadas en mesa / agregar carta a una jugada" +
                "\n5-ver cartas restantes jugadores" +
                procesarCambiosMesa() +
                "\n9-Anular Partida" +
                "\n0-terminar turno" +
                "\n----------------------------------------------------------"+
                "\nCantidad de fichas en el bote de apuestas: " + controlador.getcantidadFichasBote() +
                "\nJugadores Restantes en la partida: " + controlador.getCantDisponibles());
        if (controlador.getModoJuego().equals("JUEGOAPUNTOS")){
            txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nTus puntos de partida: "+ controlador.getpuntosJugador());
        }
    }*/

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
            //estadoActual = EstadosPosibles.SIN_ESTADO;
            if (!jugadorAgregado && !controlador.partidaCargada()) {
                jugadorAgregado = true;
                //obtenerNombre();
                flujoActual = new FlujoObtenerNombre(this, controlador);
                flujoActual.mostrarSiguienteTexto();
            } else {
                if (controlador.partidaCargada() && controlador.getNombreJugador() == null){
                    controlador.activarNuevoJugador();
                }
            /*controlador.comprobarAnfitrion();
            limpiarPantalla();
            if (!controlador.esAnfitrion()){
                mostrarEspera();
            }else {
                mostrarEsperaAnfitrion();
            }*/
                flujoActual = new FlujoEsperaPartida(this, controlador);
                flujoActual.mostrarSiguienteTexto();
            }
    }

    /*private void mostrarEspera(){
        txtAreaMuestra.setText("\n__________________________________________" +
                "\nesperando a que se unan jugadores (se necesitan entre 2-4 jugadores para empezar a jugar) " +
                "\nCantidad de jugadores:" + controlador.cantJugadores() +
                "\n__________________________________________");
        if (controlador.apuestasActivadas()){
            controlador.restarFichas();
            avisarSobreApuesta();
        }
    }*/

    /*private void mostrarEsperaAnfitrion(){
        txtAreaMuestra.setText("__________________________________________" +
                "\nesperando a que se unan jugadores (se necesitan entre 2-4 jugadores para empezar a jugar) " +
                "\nCantidad de jugadores:" + controlador.cantJugadores() +
                "\nCuando este la cantidad de jugadores necesaria seleccione la opcion:" +
                "\n1-Iniciar Partida" +
                "\n tambien puede seleccionar otro modo de juego: (por defecto esta activado el modo expres)"+
                "\n2-Cambiar Opciones de Mesa" +
                "\n__________________________________________"+
                "\nSi desea Apostar solo ingrese la cantidad que desea apostar y se definira la situacion de la apuesta segun la decision del resto de jugadores" +
                "\nminimo de Apuesta: 250" +
                "\nTus fichas:" + controlador.cantFichas());
                if (controlador.apuestasActivadas()){
                    txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nApuestas Activadas!!!");
                }
    }*/

    public void limpiarPantalla(){
        txtAreaMuestra.setText(" ");
    }

    @Override
    public void actualizarCantJugadores(){
        flujoActual = new FlujoEsperaPartida(this, controlador);
        flujoActual.mostrarSiguienteTexto();
        print("Se ha unido un nuevo jugador a la partida!!!");
    }



    /*private void terminarTurno(){
        txtAreaMuestra.setText("Para finalizar su turno, seleccione una carta para descartar (en el caso de que no tenga cartas escriba un 0)");
        mostrarSeleccionCartas();
        guardarTxtActual();
    }*/




    @Override
    public void iniciarTurno(){
        /*if (controlador.jugadorEnAutomatico()) {
            System.out.println("entro a juego automatico aca");
            try {
                controlador.iniciarJuegoAutomatico();
            } catch (NoHayCartaBocaArriba e) {
                System.out.println("No hay carta boca arriba");
                //aca nunca deberia entrar porque siempre se pone una carta boca arriba pero me obliga poner try catch
            }
        }else {
            estadoActual = EstadosPosibles.PRIMERAS_OPCIONES;
        }
        mostrarPrimerasOpciones();
        cambiarEstadoConsola(true);
        if (controlador.getTiempoPorTurno() != 0){
            activarTiempoDeTurno();
        }*/
        flujoActual = new FlujoInicioTurno(this, controlador);
        flujoActual.mostrarSiguienteTexto();
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

    private void mostrarPrimerasOpciones(){
        limpiarPantalla();
        txtAreaMuestra.setText("----------------------------------------------------------" +
                "\n1-tomar Carta del mazo" +
                "\n2-tomar carta descartada" +
                "\nCarta disponible en la pila de descartes:" + obtenerCartaDescarte()+
                "\nTus cartas:\n");
        mostrarCartas();
        guardarTxtActual();
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
                //esto sirve para que no pase el tamaño de la consola
            }
        }
    }

    private void mostrarPosiciones(ArrayList<ICarta> cartasActuales) {
        txtAreaMuestra.setText(txtAreaMuestra.getText() + "\n");
        int mitadString;
        for (int i = 0; i < cartasActuales.size(); i++) {
            mitadString = cartasActuales.get(i).toString().length() / 2;
            mitadString+= 3; //le sumo por los espacios que deja la carta y añadidos que no los cuenta este calculo
            for (int j = 0; j < mitadString; j++) {
                txtAreaMuestra.setText(txtAreaMuestra.getText() + "-");
            }
            txtAreaMuestra.setText(txtAreaMuestra.getText() + (i + 1));
            for (int j = 0; j < mitadString; j++) {
                txtAreaMuestra.setText(txtAreaMuestra.getText() + "-");
            }
        }
    }

    @Override
    public void esperarTurno(){
        limpiarPantalla();
        if (controlador.isEliminado()){
            mostrarAvisoEliminado();
        } else if (!controlador.esTurnoJugador()){
            txtAreaMuestra.setText("______________________________________________" +
                    "\nHa iniciado un nuevo turno, pero no es suyo. Espere su siguiente turno..." +
                    "\n______________________________________________");
            txtConsola.setEnabled(false);
            if (controlador.jugadorEnAutomatico()){
                txtConsola.setEnabled(true);
                txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nSe te activo el juego automatico por no terminar tu turno a tiempo." +
                        "\nAVISO: si todos los jugadores entran en modo automatico la partida finalizara amistosamente." +
                        "\nSi quiere desactivar esta opcion solo presione 1");
                estadoActual = EstadosPosibles.JUEGO_AUTOMATICO;
            }
        }
        guardarTxtActual();
        //else que continue el turno?
    }


    /*@Override
    public void actualizarCartas(ArrayList<ICarta> cartasJugador) {
        /*for (int i = 0; i < cartasJugador.size(); i++) {
            txtAreaMuestra.setText("\n" + cartasJugador.get(i));
        }
    }*/

    @Override
    public void nuevoTurno(){
        if (controlador.esTurnoJugador()){
            flujoActual = new FlujoInicioTurno(this, controlador);
        }else {
            flujoActual = new FlujoEsperaTurno(this, controlador);
        }
        flujoActual.mostrarSiguienteTexto();
    }

    @Override
    public void continuarTurnoActual(){
        if (controlador.esTurnoJugador()) {
            flujoActual = new FlujoContinuarTurno(this, controlador);
        }else {
            flujoActual = new FlujoEsperaTurno(this, controlador);
        }
        flujoActual.mostrarSiguienteTexto();
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
        flujoActual = new FlujoFinPartida(this, controlador);
        flujoActual.mostrarSiguienteTexto();
    }

    public void mostrarOpcionesNuevaPartida() {
        //comprueba cual es el anfitrion antes de dar la eleccion por si hubo un cambio inesperado
        controlador.comprobarAnfitrion();
        if (controlador.esAnfitrion()){
            //estadoActual = EstadosPosibles.FIN_PARTIDA;
            txtAreaMuestra.setText(txtAreaMuestra.getText() + "\n¿Desea iniciar una nueva partida? (Y/N) (Y para si, N para no)");
        }else {
            txtConsola.setEnabled(false);
            txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nEl anfitrion esta decidiendo si iniciar una nueva partida...");
        }
    }

    @Override
    public void actualizarJugadas(){
        jugadasSinVer = true;
        //continuarTurnoActual();
    }

    @Override
    public void cerrarPartida() {
        /*if (controlador.getModoJuego().equals("EXPRES")){
            txtAreaMuestra.setText("\nLa partida fue cerrada ya que no se pueden  hacer combinaciones o añadir cartas ");
            if (!controlador.getEstadoCompetitivo()){
                txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nNo se ganaron ni puntos ni fichas apostadas porque el competitivo esta desactivado.");
            }
        }else {
            txtAreaMuestra.setText("\nLa ronda fue cerrada por lo que se sumaran los puntos sobrantes a cada jugador e iniciara una nueva ronda...");
            controlador.iniciarNuevaRonda();
        }*/
        flujoActual = new FlujoPartidaCerrada(this, controlador);
        flujoActual.mostrarSiguienteTexto();

    }

    @Override
    public void mostrarErrorApuesta(){
        hayApuesta = false;
        /*if (controlador.esAnfitrion()){
            mostrarEsperaAnfitrion();
        }*/
        print("\nSe cancelaron las apuestas!!!");
    }

    @Override
    public void avisarSobreApuesta() {
        if (!controlador.esAnfitrion()){
            hayApuesta = true;
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
        flujoActual = new FlujoEsperaNuevaRonda(this, controlador);
        flujoActual.mostrarSiguienteTexto();
    }

    @Override
    public void finalizarPartidaAmistosamente() {
        /*limpiarPantalla();
        txtAreaMuestra.setText("\nLa partida ha finalizado Amistosamente!!! Se devolvieron apuestas actuales y los puntos no cuentan");
        controlador.obtenerPosiciones();
        mostrarOpcionesNuevaPartida();*/
        flujoActual = new FlujoFinPartidaAmistosa(this, controlador);
        flujoActual.mostrarSiguienteTexto();
    }

    @Override
    public void eleccionAnularPartida() {
        /*estadoActual = EstadosPosibles.POSIBLE_ANULAR_PARTIDA;
        txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nSe solicito anular la partida.\n¿Desea anular la partida? (Y/N) (Y para si, N para no)");
        txtConsola.setEnabled(true);*/
        flujoActual = new FlujoAnularPartida(this, controlador);
        flujoActual.mostrarSiguienteTexto();
    }

    @Override
    public void obtenerNombre() {
        /*estadoActual = EstadosPosibles.SELECCION_NOMBRE;
        txtAreaMuestra.setText("Escriba su nombre...");*/
        flujoActual = new FlujoObtenerNombre(this, controlador);
        flujoActual.mostrarSiguienteTexto();
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
        print("Error: faltan jugadores para comenzar la partida.");
    }

    @Override
    public void mostrarJugadorSalioDelJuego() {
        limpiarPantalla();
        print("Un Jugador Ha salido Del Juego.");
        flujoActual = new FlujoFinPartidaAmistosa(this,controlador);
        flujoActual.mostrarSiguienteTexto();
        //mostrarOpcionesNuevaPartida();
    }

    @Override
    public void avisarCambiosOpcionesMesa() {
        cambiosOpcionesMesa = true;
        if (!controlador.esTurnoJugador()){
            print("\nEl anfitrion hizo cambios en las opciones de mesa!!!");
        }
        //continuarEnEstadoAnterior();
    }

    public void mostrarPartidasDisponibles(ArrayList<String> partidaDisponible) {
        print("Partidas disponibles:");
        if (partidaDisponible.isEmpty()){
            print("No hay partidas guardadas.");
        }else {
            for (int i = 0; i < partidaDisponible.size(); i++) {
                print((i + 1) + "-" + partidaDisponible.get(i));
            }
        }
    }
    @Override
    public void activarSoloChat() {
        modoChat = true;
        flujoActual = new FlujoChatPublico(this, controlador);
        flujoActual.mostrarSiguienteTexto();
        //falta agregar el modo chat para cada actualizar
    }

    @Override
    public void mostrarNuevoMensaje(String mensajeNuevo) {
        print(mensajeNuevo);
    }
}