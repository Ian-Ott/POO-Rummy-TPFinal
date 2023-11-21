package ar.edu.unlu.poo.ventana;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.Carta;
import ar.edu.unlu.poo.modelo.ICarta;
import ar.edu.unlu.poo.modelo.ITapete;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VistaConsola implements IVista{
    enum EstadosPosibles{
        SIN_ESTADO,SELECCION_NOMBRE,PRIMERAS_OPCIONES,SELECCION_CARTAS,SELECCION_JUGADA, FIN_PARTIDA, POSIBLE_ANULAR_PARTIDA, CONTINUAR_TURNO
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
                enterButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        procesarTexto();
                    }
                });
            }
        });

    }

    private void procesarTexto(){
        String textoIngresado = txtConsola.getText().toLowerCase();
        txtConsola.setText("");
        if (controlador.juegoIniciado()){
            if (estadoActual.equals(EstadosPosibles.PRIMERAS_OPCIONES)){
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
            }
        } else if (estadoActual.equals(EstadosPosibles.SELECCION_NOMBRE)){
            if (!controlador.estaEnElJuego(textoIngresado)){
                estadoActual = EstadosPosibles.SIN_ESTADO;
                controlador.nuevoJugador(controlador.esAnfitrion(), textoIngresado);
            }else {
                JOptionPane.showMessageDialog(null,"Error: hay alguien con ese nombre en la partida!!!");
                obtenerNombre();
            }
        } else{
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
        }
    }

    private void seleccionarOpcionesReenganche(String textoIngresado) {
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
    }

    private void seleccionarOpcionesInicio(String textoIngresado) {
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
                controlador.activarModoExpres();
                txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nSe activo el modo Expres!!!");
            } else if (textoIngresado.equals("3")) {
                controlador.activarModoPuntos();
                txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nSe activo el modo Juego por Puntos!!!");
            } else if (apuesta >= 250 && !hayApuesta) {
                hayApuesta = true;
                txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nApuestas Activadas!!!");
                controlador.apostar(apuesta);
            } else {
                opcionIncorrecta();
            }
        }else {opcionIncorrecta();}
    }

    private void seleccionarOpcionesTurno(String textoIngresado) {
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
            mostrarJugadasEnMesa();
        } else if (textoIngresado.equals("5")) {
            mostrarCantidadCartas();
        } else if (textoIngresado.equals("6")) {
            //falto implementar el opciones de mesa
        } else if (textoIngresado.equals("9")) {
            controlador.solicitarAnularPartida();
        } else if (textoIngresado.equals("0")) {
            finTurno = true;
            terminarTurno();
        }else {
            opcionIncorrecta();
        }
    }

    private void seleccionarOpcionesAnularPartida(String textoIngresado) {
        String eleccion = textoIngresado.toUpperCase();
        if (eleccion.equals("Y") || eleccion.equals("N")){
            limpiarPantalla();
            txtAreaMuestra.setText("Usted tomo su eleccion. Esperando a que el resto de jugadores decida...");
            controlador.tomarDecisionDePartida(eleccion);
        } else {
            opcionIncorrecta();
        }
    }

    private void seleccionarOpcionesDePartida(String textoIngresado) {
        String eleccion = textoIngresado.toUpperCase();
        if (eleccion.equals("Y")){
            estadoActual = EstadosPosibles.SIN_ESTADO;
            controlador.nuevoJuego();
            //la consola vuelve al primer estado original donde se esperan jugadores (de hecho podrian entrar nuevos jugadores)
        } else if (eleccion.equals("N")) {
            controlador.cerrarJuego();
        }else {
            opcionIncorrecta();
        }
    }

    private void seleccionarJugada(String textoIngresado) {
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
    }

    private void seleccionarCartas(String textoIngresado) {
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
                    finTurno = false;
                    estadoActual = EstadosPosibles.PRIMERAS_OPCIONES;
                    controlador.terminarTurno(posicionesSeleccionadas);
                    posicionesSeleccionadas.clear();
                }
            }else {
                opcionIncorrecta();
            }
        } else {
            opcionIncorrecta();
            //excepcion
        }
    }

    private void seleccionarPrimerasOpciones(String textoIngresado) {
        if (textoIngresado.equals("1")){
            controlador.tomarCartaMazo();
        } else if (textoIngresado.equals("2")) {
            controlador.tomarCartaDescarte();
        }else {
            opcionIncorrecta();
        }
    }

    private void mostrarCantidadCartas(){
        continuarTurnoActual();
        ArrayList<String> oponentes = controlador.nombreOponentes(controlador.getNombreJugador());
        for (int i = 0; i < controlador.cantJugadores() - 1;i++){
            txtAreaMuestra.setText(txtAreaMuestra.getText()+"\nJugador " + oponentes.get(i) +
                    "\nCantidad de cartas: " + controlador.cantCartasOponente(oponentes.get(i)));
        }
    }

    private void agregarPosicion(int numero) {
        if (!posicionesSeleccionadas.contains(numero)){
            posicionesSeleccionadas.add(numero);
        }else {
            JOptionPane.showMessageDialog(null, "posicion ya seleccionada!!!");
        }
    }

    private void mostrarSeleccionCartas(){
        mostrarCartas();
        txtAreaMuestra.setText(txtAreaMuestra.getText() +
                "\n seleccione las cartas segun su POSICION (empezando por la 1) \nuna vez finalizado presione 0 para continuar");
            estadoActual = EstadosPosibles.SELECCION_CARTAS;
    }

    public void opcionIncorrecta(){
        JOptionPane.showMessageDialog(null, "Opcion incorrecta!!!");
    }

    private void mostrarMenu() {
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
                //"\n6-Opciones de mesa (solo disponible para el jefe de mesa)" +
                "\n9-Anular Partida" +
                "\n0-terminar turno" +
                "\n----------------------------------------------------------"+
                "\nCantidad de fichas en el bote de apuestas: " + controlador.getcantidadFichasBote() +
                "\nJugadores Restantes en la partida: " + controlador.getCantDisponibles());
    }


    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void pantallaEspera(boolean anfitrion) {
        estadoActual = EstadosPosibles.SIN_ESTADO;
        if (!jugadorAgregado){
            jugadorAgregado = true;
            obtenerNombre();
        }else {
            limpiarPantalla();
            if (!anfitrion){
                mostrarEspera();
            }else {
                mostrarEsperaAnfitrion();
            }
        }
    }

    private void mostrarEspera(){
        txtAreaMuestra.setText("\n__________________________________________" +
                "\nesperando a que se unan jugadores (se necesitan entre 2-4 jugadores para empezar a jugar) " +
                "\nCantidad de jugadores:" + controlador.cantJugadores() +
                "\n__________________________________________");
        if (controlador.apuestasActivadas()){
            controlador.restarFichas();
            avisarSobreApuesta();
        }
    }

    private void mostrarEsperaAnfitrion(){
        txtAreaMuestra.setText("__________________________________________" +
                "\nesperando a que se unan jugadores (se necesitan entre 2-4 jugadores para empezar a jugar) " +
                "\nCantidad de jugadores:" + controlador.cantJugadores() +
                "\nCuando este la cantidad de jugadores necesaria seleccione la opcion:" +
                "\n1-Iniciar Partida" +
                "\n tambien puede seleccionar otro modo de juego: (por defecto esta activado el modo expres)"+
                "\n2-Activar Modo Expres (es una partida rapida de una ronda en la que gana el jugador que cierra antes)" +
                "\n3-Activar Juego a Puntos (Cuando un jugador alcanza los 300 puntos queda eliminado. El último jugador es el ganador de la partida)" +
                "\n__________________________________________"+
                "\nSi desea Apostar solo ingrese la cantidad que desea apostar y se definira la situacion de la apuesta segun la decision del resto de jugadores" +
                "\nminimo de Apuesta: 250" +
                "\nTus fichas:" + controlador.cantFichas());
                if (controlador.apuestasActivadas()){
                    txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nApuestas Activadas!!!");
                }
    }

    private void limpiarPantalla(){
        txtAreaMuestra.setText(" ");
    }

    @Override
    public void actualizarCantJugadores(){
        pantallaEspera(controlador.esAnfitrion());
    }


    private void mostrarJugadasEnMesa(){
        ITapete jugadasEnMesa = controlador.obtenerJugadas();
        txtAreaMuestra.setText("\n");
        txtAreaMuestra.setText(txtAreaMuestra.getText() + jugadasEnMesa);
        txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nSeleccione la jugada a la que quiera agregar una carta \n(si desea la agregar cartas a la jugada 1 solo seleccione el 1 y asi con las demas) \n\npresione 0 para cancelar");
        estadoActual = EstadosPosibles.SELECCION_JUGADA;
    }
    private void terminarTurno(){
        txtAreaMuestra.setText("Para finalizar su turno, seleccione una carta para descartar (en el caso de que no tenga cartas escriba un 0)");
        mostrarSeleccionCartas();
    }




    @Override
    public void iniciarTurno(){
        estadoActual = EstadosPosibles.PRIMERAS_OPCIONES;
        mostrarPrimerasOpciones();
        txtConsola.setEnabled(true);
    }

    private void mostrarPrimerasOpciones(){
        limpiarPantalla();
        txtAreaMuestra.setText("----------------------------------------------------------" +
                "\n1-tomar Carta del mazo" +
                "\n2-tomar carta descartada" +
                "\nCarta disponible en la pila de descartes:" + controlador.getCartaDescarte()+
                "\nTus cartas:\n");
        mostrarCartas();
    }


    private void mostrarCartas(){
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
    public void esperarTurno(){
        limpiarPantalla();
        if (controlador.isEliminado()){
            mostrarAvisoEliminado();
        }
        else if (!controlador.esTurnoJugador()){
            txtAreaMuestra.setText("______________________________________________" +
                    "\nHa iniciado un nuevo turno, pero no es suyo. Espere su siguiente turno..." +
                    "\n______________________________________________");
            txtConsola.setEnabled(false);
        }
    }


    @Override
    public void actualizarCartas(ArrayList<ICarta> cartasJugador) {
        /*for (int i = 0; i < cartasJugador.size(); i++) {
            txtAreaMuestra.setText("\n" + cartasJugador.get(i));
        }*/
    }

    @Override
    public void nuevoTurno(){
        if (controlador.esTurnoJugador()){
            iniciarTurno();
        }else {
            esperarTurno();
        }
    }

    @Override
    public void continuarTurnoActual(){
        if (controlador.isEliminado()){
            mostrarAvisoEliminado();
        } else if (controlador.esTurnoJugador()){
            estadoActual = EstadosPosibles.CONTINUAR_TURNO;
            limpiarPantalla();
            mostrarMenu();
            mostrarCartas();
            if (jugadasSinVer){
                txtAreaMuestra.setText(txtAreaMuestra.getText() +
                        "\nHay nuevas jugadas disponibles en la mesa!!!");
            }
        }
    }

    private void mostrarAvisoEliminado() {
        txtAreaMuestra.setText("Usted ha sido eliminado de la partida por sobrepasar la cantidad de puntos."+
                "\n¿Quiere reengancharse? (si acepta debe apostar la mitad de lo que aposto en un inicio)"+
                "\nTus fichas: " + controlador.cantFichas() + " | Cantidad apostada: " + controlador.getcantidadApostada() +
                "\nAVISO: En el caso que las apuestas esten desactivadas no es posible hacer el reenganche. "+
                "\nEscriba Y para reenganchar.");
        txtConsola.setEnabled(true);
    }

    @Override
    public void finalizarPartida() {
        limpiarPantalla();
        txtAreaMuestra.setText("\nLa partida ha finalizado!!! El ganador es..." + controlador.getGanador() +
                " con " + controlador.getCantidadPuntosGanador()+" puntos");
        mostrarTablaPosiciones();
        eleccionNuevaPartida();
    }

    private void eleccionNuevaPartida() {
        if (controlador.esAnfitrion()){
            estadoActual = EstadosPosibles.FIN_PARTIDA;
            txtAreaMuestra.setText(txtAreaMuestra.getText() + "\n¿Desea iniciar una nueva partida? (Y/N) (Y para si, N para no)");
        }else {
            txtConsola.setEnabled(false);
            txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nEl anfitrion esta decidiendo si iniciar una nueva partida..."
                            //+ "\n(AVISO:En caso que se cierre el juego significa que no hay una nueva partida)"
            );
        }
    }

    @Override
    public void actualizarJugadas(){
        jugadasSinVer = true;
        continuarTurnoActual();
    }

    @Override
    public void cerrarPartida() {
        if (controlador.getModoJuego().equals("EXPRES")){
            txtAreaMuestra.setText("\nLa partida fue cerrada ya que no se pueden  hacer combinaciones o añadir cartas ");
        }else {
            txtAreaMuestra.setText("\nLa ronda fue cerrada por lo que se sumaran los puntos sobrantes a cada jugador e iniciara una nueva ronda...");
            controlador.iniciarNuevaRonda();
        }

    }

    @Override
    public void mostrarErrorApuesta(){
        hayApuesta = false;
        if (controlador.esAnfitrion()){
            mostrarEsperaAnfitrion();
        }
        txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nSe cancelaron las apuestas!!!");
    }

    @Override
    public void avisarSobreApuesta() {
        if (!controlador.esAnfitrion()){
            hayApuesta = true;
            txtConsola.setEnabled(true);
            txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nAVISO:"+
                    "\nLas apuestas han sido activadas. (en el caso que quiera cancelar la apuesta presione 0)" +
                    "\nTu apuesta seria de: " + controlador.getcantidadApostada() + " fichas.");
        }
    }

    @Override
    public void mostrarResultadosPuntos() {
        txtAreaMuestra.setText("\nPuntos de los jugadores (el limite es 300):");
        for (int i = 0; i < controlador.cantJugadores(); i++) {
            txtAreaMuestra.setText(txtAreaMuestra.getText() + "\n" + controlador.obtenerJugador(i));
        }
        controlador.iniciarNuevaRonda();
    }

    @Override
    public void finalizarPartidaAmistosamente() {
        limpiarPantalla();
        txtAreaMuestra.setText("\nLa partida ha finalizado Amistosamente!!! Se devolvieron apuestas actuales y los puntos no cuentan");
        mostrarTablaPosiciones();
        eleccionNuevaPartida();
    }

    @Override
    public void eleccionAnularPartida() {
        estadoActual = EstadosPosibles.POSIBLE_ANULAR_PARTIDA;
        txtAreaMuestra.setText(txtAreaMuestra.getText() + "\nSe solicito anular la partida.\n¿Desea anular la partida? (Y/N) (Y para si, N para no)");
        txtConsola.setEnabled(true);
    }

    @Override
    public void obtenerNombre() {
        estadoActual = EstadosPosibles.SELECCION_NOMBRE;
        txtAreaMuestra.setText("Escriba su nombre...");
    }

    @Override
    public void solicitarCerrarVentana() {
        txtAreaMuestra.setText("Se decidio no empezar una nueva partida. Por favor, cierre la ventana.");
    }

    private void mostrarTablaPosiciones() {
        txtAreaMuestra.setText(txtAreaMuestra.getText() +
                "\nTabla de posiciones:");
    }
}
