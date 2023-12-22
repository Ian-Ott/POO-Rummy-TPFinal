package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.Serializacion.services.Serializador;
import ar.edu.unlu.poo.exceptions.*;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static java.lang.Math.random;

public class Rummy extends ObservableRemoto implements IRummy, Serializable {
    enum modoDeJuego{
        EXPRES,JUEGOAPUNTOS
    }
    private Mazo mazoDeJuego;
    private ArrayList<Jugador> jugadores;
    private Tapete mesaDeJuego;
    private static Rummy instancia;
    private boolean juegoIniciado;
    private int posicionTurnoActual;
    private modoDeJuego modo;
    private int limitePuntos;
    private Jugador ganador;
    private boolean apuestasActivas;
    private int solicitudDeAnularPartida;
    private boolean estadoCompetitivo;

    private boolean partidaFinalizada;
    private int tiempoDeTurno;

    private boolean tomoCartaJugadorTurno;
    private static Serializador serializador;
    private boolean cargadosPuntosJugadoresEnTabla;
    private boolean partidaCargada;
    private boolean publicoPermitido;
    private boolean finRonda;
    //private ArrayList<Observer> observadores = new ArrayList<>();

    public Rummy() throws RemoteException {
        jugadores = new ArrayList<>();
        mazoDeJuego = new Mazo();
        mesaDeJuego = new Tapete();
        juegoIniciado = false;
        modo = modoDeJuego.EXPRES;
        limitePuntos = 300;
        solicitudDeAnularPartida = 0;
        estadoCompetitivo = true;
        tiempoDeTurno = 0;
        partidaFinalizada = false;
        publicoPermitido = false;
        finRonda = false;
    }

    public static IRummy getInstancia() throws RemoteException {
        try {
            if (instancia == null){
                instancia = new Rummy();
            }
        }catch (RemoteException e){
            e.printStackTrace();
        }
        return instancia;
    }
//inicio juego:

    public boolean isJuegoIniciado() throws RemoteException{
        return juegoIniciado;
    }

    public void iniciarJuego() throws RemoteException{
        if (!partidaCargada) {
            reiniciarEstados();
            juegoIniciado = true;
            if (jugadores.size() == 2) {
                repartirCartasJugadores(10);
            } else if (jugadores.size() >= 3) {
                repartirCartasJugadores(7);
            }
            mezclarMazo();
            elegirJugadorMano();
        }else {
            partidaCargada = false;
            notificarObservadores("juego iniciado");
        }
        //notificarObservadores("cartas repartidas");
    }

    public void mezclarMazo() throws RemoteException{
        //cuando el mazo se quede sin cartas se van a agregar de manera random al mazo las cartas que quedaron boca arriba
        mazoDeJuego.mezclarMazo();
    }

    public void reiniciarEstados() throws RemoteException {
        ganador = null;
        solicitudDeAnularPartida = 0;
        if (partidaFinalizada || finRonda) {
            finRonda = false;
            reiniciarEstadosJugadores();
            devolverCartasAMesa();
            cargadosPuntosJugadoresEnTabla = false;
        }
        //devuelve todas las cartas de los jugadores a la mesa para despues mezclarlas con el mazo
    }
    @Override
    public void nuevoJuego() throws RemoteException {
        juegoIniciado = false;
        notificarObservadores("nuevo juego");
    }
    public void iniciarNuevaRonda() throws RemoteException{
        iniciarJuego();
    }

    private void reiniciarEstadosJugadores() {
        for (Jugador jugadorActual:jugadores) {
            jugadorActual.setAgregoJugada(false);
            if (modo.equals(modoDeJuego.JUEGOAPUNTOS) && partidaFinalizada){
                jugadorActual.setEliminado(false);
                jugadorActual.setCantApostada(0);
                jugadorActual.setPuntosDePartida(0);
                partidaFinalizada = false;
            } else if (modo.equals(modoDeJuego.EXPRES)) {
                jugadorActual.setCantApostada(0);
                partidaFinalizada = false;
            }
            jugadorActual.setModoAutomatico(false);
            jugadorActual.setHizoRummy(false);
            jugadorActual.setFichasGanadasPartida(0); //dejar esto?
        }
    }

    private void devolverCartasAMesa() {
        //devuelvo las cartas de jugadores y de las jugadas a la pila de cartas boca arriba
        Carta cartaAux;
        for (int i = 0; i < jugadores.size();i++){
            if (!jugadores.get(i).getCartasEnMano().isEmpty()){
                while (!jugadores.get(i).getCartasEnMano().isEmpty()){
                    cartaAux = jugadores.get(i).getCartasEnMano().remove(0);
                    mazoDeJuego.agregarCartaBocaArriba(cartaAux);
                }
            }
        }
        ArrayList<Carta> jugadaActual;
        for (int i = 0; i < mesaDeJuego.getListaJugada().size(); i++) {
            jugadaActual = mesaDeJuego.buscarJugada(i);
            while (!jugadaActual.isEmpty()){
                cartaAux = jugadaActual.remove(0);
                mazoDeJuego.agregarCartaBocaArriba(cartaAux);
            }
        }
        mesaDeJuego.getListaJugada().clear();
    }

    private void elegirJugadorMano() throws RemoteException {
        int posiciones = jugadores.size();
        //aca se elige cual jugador va a ser mano y se comienza el nuevo turno
        posicionTurnoActual = (int) (random() * (posiciones));
        while(jugadores.get(posicionTurnoActual).isEliminado()) {
            //este while hace que no inicie un jugador que este eliminado
            if (jugadores.get(posicionTurnoActual).isEliminado()){
                posicionTurnoActual = (int) (random() * (posiciones));
            }
        }
        tomoCartaJugadorTurno = false;
        System.out.println("posicion turno: " + posicionTurnoActual);
        notificarObservadores("nuevo turno");
    }



    @Override //
    public void repartirCartasJugadores(int cantidadCartas) throws RemoteException {
        for (int i = 0; i < jugadores.size(); i++){
            mazoDeJuego.repartir(cantidadCartas, jugadores.get(i));
        }

    }
//sobre jugador
    public void agregarJugador(Jugador nuevoJugador) throws RemoteException{
        if (!juegoIniciado){
            jugadores.add(nuevoJugador);
            if (jugadores.size() == 1){
                nuevoJugador.setJefeMesa(true);
            }else {
                nuevoJugador.setJefeMesa(false);
            }
            notificarObservadores("nuevo jugador");
        }
    }

    @Override
    public void eliminarJugador(String nombreJugador) throws RemoteException {
        Jugador jugadorBorrado = buscarJugador(nombreJugador);
        jugadores.remove(jugadorBorrado);
        if (jugadorBorrado.getJefeMesa() && !jugadores.isEmpty()){
            jugadores.get(0).setJefeMesa(true);
        }
        if (juegoIniciado && !partidaFinalizada){
            solicitudDeAnularPartida = jugadores.size() - 1;
            //obligo anular la partida por la falta de un jugador
            anularPartida(true);
        }else {
            notificarObservadores("jugador sacado del juego");
        }
    }

    public int getJugadoresSize() throws RemoteException {
        return jugadores.size();
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }
    @Override
    public int getCantidadFichas(String nombreJugador) {
        Jugador jugadorActual = buscarJugador(nombreJugador);
        return jugadorActual.getFichasTotales();
    }

    @Override
    public int getCantCartasOponente(String oponente) throws RemoteException{
        Jugador jugadorActual = buscarJugador(oponente);
        return jugadorActual.getCartasEnMano().size();
    }
    @Override
    public String getJugador(int posicion) throws RemoteException {
        return String.valueOf(jugadores.get(posicion));
    }

    @Override
    public ArrayList<IJugador> getIJugadores() throws RemoteException {
        ArrayList<IJugador> iJugadores = new ArrayList<>();
        for (Jugador jugador: jugadores) {
            iJugadores.add((IJugador) jugador);
        }
        return iJugadores;
    }

    @Override
    public void siguienteTurno(Jugador jugadorIzquierda)throws RemoteException{
        //busca la posicion del jugador que tiene el turno y luego de guardarla avisa el cambio de turno
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).equals(jugadorIzquierda)){
                posicionTurnoActual = i;
                tomoCartaJugadorTurno = false;
                notificarObservadores("nuevo turno");
            }
        }
    }




    @Override
    public ArrayList<String> getNombreOponentes(String nombreJugador)throws RemoteException {
        ArrayList<String> oponentes = new ArrayList<>();

        String nombreAux;
        for (int i = 0; i < jugadores.size();i++){
            nombreAux = jugadores.get(i).getNombre();
            if (!nombreAux.equals(nombreJugador)){
                oponentes.add(nombreAux);
            }
        }

        return oponentes;
    }

    @Override
    public ArrayList<String> getNombreJugadores()throws RemoteException {
        ArrayList<String> nombres = new ArrayList<>();
        String nombreAux = "";
        Jugador jugadorAux;
        for (int i = 0; i < this.jugadores.size(); i++){
            jugadorAux = this.jugadores.get(i);
            nombreAux += jugadorAux.getNombre();
            nombres.add(nombreAux);
            nombreAux = "";
        }
        return nombres;
    }
    @Override
    public ArrayList<Carta> getCartasJugador(String nombreJugador) throws RemoteException {
        ArrayList<Carta> listaCartas = null;
        //poner una excepcion para no devolver null
        Jugador jugadorAux;
        for (int i = 0; i < jugadores.size(); i++){
            jugadorAux = jugadores.get(i);
            if (jugadorAux.getNombre().equals(nombreJugador)){
                listaCartas = jugadorAux.getCartasEnMano();
            }
        }
        return listaCartas;
    }

    public String getNombreTurnoActual(){
        System.out.println("posicion turno actual: " + posicionTurnoActual);
        System.out.println("nombre turno: " + jugadores.get(posicionTurnoActual).getNombre());
        return jugadores.get(posicionTurnoActual).getNombre();
    }


    //sobre jefe de mesa
    @Override
    public boolean isJefeMesa(String nombreJugador) throws RemoteException, JugadorInexistente {
        Jugador jugadorAux = buscarJugador(nombreJugador);
        if (jugadorAux == null){
            throw new JugadorInexistente();
        }
        return jugadorAux.getJefeMesa();
    }

    @Override
    public String getNombreJefeMesa() throws RemoteException {
        String nombreJefeMesa = null;
        //nunca deberia de devolver null porque si o si un jugador debe ser jefe de mesa
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).getJefeMesa()){
                nombreJefeMesa = jugadores.get(i).getNombre();
            }
        }
        return nombreJefeMesa;
    }

    private Jugador buscarJugador(String nombreJugador) {
        Jugador jugador = null;
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).getNombre().equals(nombreJugador)){
                jugador = jugadores.get(i);
            }
        }
        return jugador;
    }

    @Override
    public Jugador buscarJugadorIzquierda(Jugador jugadorActual) {
        Jugador jugadorAux  = null;
        for (int i = 0; i < jugadores.size(); i++){
            if (jugadorActual.equals(jugadores.get(i))){
                if (i == 0){
                    jugadorAux = jugadores.get(jugadores.size() - 1);
                }else {
                    jugadorAux = jugadores.get(i - 1);
                }
            }
        }
        if (jugadorAux.isEliminado()){
            jugadorAux = buscarJugadorIzquierda(jugadorAux);
            //en el caso que el jugador de la izquierda este eliminado el metodo busca recursivamente cual es el siguiente
        }
        return jugadorAux;
    }



    //sobre jugador eliminado
    @Override
    public boolean estaEliminado(String nombreJugador) throws RemoteException{
        Jugador jugadorActual = buscarJugador(nombreJugador);
        return jugadorActual.isEliminado();
    }
    public void reengancharJugador(String nombreJugador)throws RemoteException{
        int puntosNuevos = buscarJugadorConMasPuntos();
        Jugador jugadorActual = buscarJugador(nombreJugador);
        jugadorActual.setPuntosDePartida(puntosNuevos);
        jugadorActual.setEliminado(false);
        //debe apostar la mitad de la apuesta inicial para reenganchar
        int nuevaApuesta = (int) (jugadorActual.getCantApostada() * 0.5);
        jugadorActual.setCantApostada(nuevaApuesta + jugadorActual.getCantApostada());
        jugadorActual.restarFichasTotales(nuevaApuesta);
        //le agrego la cantidad total que aposto por si quiere volver a hacer reenganche y le resto su cantidad de fichas
        mesaDeJuego.agregarApuesta(nuevaApuesta);
    }

    //inicio turno
    @Override //
    public void sacarCartaMazo(String jugador) throws RemoteException{
        Jugador jugadorAux = buscarJugador(jugador);
        mazoDeJuego.sacarCartaMazo(jugadorAux);
        tomoCartaJugadorTurno = true;
        notificarObservadores("continuar turno jugador");
    }

    @Override
    public void agarrarCartaBocaArriba(String nombreJugador) throws RemoteException{
        Jugador jugadorAux = buscarJugador(nombreJugador);
        if (mazoDeJuego.sacarCartaBocaArriba(jugadorAux)){
            tomoCartaJugadorTurno = true;
            //si pudo sacar la carta notifica el cambio a los observadores
            notificarObservadores("continuar turno jugador");
        }
    }

    public Carta getCartaBocaArriba() throws RemoteException, NoHayCartaBocaArriba {
        return mazoDeJuego.cartaBocaArribaActual();
    }
    @Override
    public int getPuntosJugador(String nombreJugador) throws RemoteException {
        Jugador jugadorActual = buscarJugador(nombreJugador);
        return jugadorActual.getPuntosDePartida();
    }

    //sobre jugadas
    @Override
    public ITapete getMesaJugadas() throws RemoteException{
        return mesaDeJuego;
    }

    @Override
    public void comprobarRummy(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador) throws RemoteException, FaltanCartas, NoEsJugada, NoPuedeHacerRummy {
        Jugador jugadorActual = buscarJugador(nombreJugador);
        if (posicionesSeleccionadas.size() == jugadorActual.getCartasEnMano().size()) {
            ArrayList<Carta> cartasSeleccionadas = new ArrayList<>();
            obtenerCartasOrdenadas(cartasSeleccionadas, jugadorActual);
            try {
                mesaDeJuego.verificarRummy(cartasSeleccionadas, jugadorActual);
            }catch (JugadorHizoRummy e){
                finalizarPartida(nombreJugador);
            }
        }else {
            throw new FaltanCartas();
        }
    }

    @Override
    public void comprobarCombinacion(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador) throws RemoteException, FaltanCartas, NoEsJugada{
        Jugador jugadorActual = buscarJugador(nombreJugador);
        if (posicionesSeleccionadas.size() >= 3) {
            ArrayList<Carta> cartasSeleccionadas = new ArrayList<>();
            obtenerCartasPorPosicion(posicionesSeleccionadas, jugadorActual, cartasSeleccionadas);
            try{
                mesaDeJuego.verificarCombinacion(cartasSeleccionadas, jugadorActual);
            }catch (EsJugadaValida e){
                if (jugadorActual.getCartasEnMano().isEmpty()) {
                    finalizarPartida(nombreJugador);
                }else {
                    notificarObservadores("jugada agregada");
                }
            }
        }else {
            throw new FaltanCartas();
        }
    }

    private void obtenerCartasOrdenadas(ArrayList<Carta> cartasAux, Jugador jugadorActual) {
        for (int i = 0; i < jugadorActual.getCartasEnMano().size();i++) {
            //le saca todas las cartas al jugador para que no queden duplicadas y si ocurre un error luego se devuelven
            agregarCartaOrdenada(cartasAux,jugadorActual.getCartasEnMano().remove(i));
        }
    }

    private void obtenerCartasPorPosicion(ArrayList<Integer> posicionesSeleccionadas, Jugador jugadorActual,ArrayList<Carta> cartas) {
        int posicion;
        for (int i = posicionesSeleccionadas.size() - 1; i >= 0 ; i--) {
            posicion = posicionesSeleccionadas.get(i);
            //le resto la cantidad de eliminados porque la posicion elegida va cambiando al eliminar cartas
            System.out.println("llego antes de agregar carta");
            agregarCartaOrdenada(cartas,jugadorActual.getCartasEnMano().get(posicion));
            //le saco la carta al jugador para que no quede duplicada
        }
        mesaDeJuego.quitarCartasSeleccionadas(jugadorActual.getCartasEnMano(), cartas);
        posicionesSeleccionadas.clear();
    }

    @Override
    public void comprobarEscalera(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador) throws RemoteException, FaltanCartas, NoEsJugada{
        Jugador jugadorActual = buscarJugador(nombreJugador);
        if (posicionesSeleccionadas.size() <= jugadorActual.getCartasEnMano().size() && posicionesSeleccionadas.size() >= 3) {
            ArrayList<Carta> cartasSeleccionadas = new ArrayList<>();
            obtenerCartasPorPosicion(posicionesSeleccionadas, jugadorActual, cartasSeleccionadas);
            try {
                mesaDeJuego.verificarEscalera(cartasSeleccionadas, jugadorActual);
            }catch (EsJugadaValida e){
                if (jugadorActual.getCartasEnMano().isEmpty()) {
                    finalizarPartida(nombreJugador);
                }else {
                    notificarObservadores("jugada agregada");
                }
            }
        }else {
            throw new FaltanCartas();
        }
    }

    @Override
    public void agregarCartaAJugada(ArrayList<Integer> posicionesSeleccionadas, int posicionJugada, String nombreJugador) throws RemoteException, JugadaLLena, NoSeAgregaronAJugada {
        Jugador jugadorActual = buscarJugador(nombreJugador);
        ArrayList<Carta> cartasSeleccionadas = new ArrayList<>();
        obtenerCartasPorPosicion(posicionesSeleccionadas, jugadorActual, cartasSeleccionadas);
        try {
            mesaDeJuego.verificarCartaParaJugada(cartasSeleccionadas, posicionJugada, jugadorActual);
        }catch (CartasAgregadasAJugada e){
            notificarObservadores("cartas agregadas");
        }
    }

    @Override
    public void agregarCartaOrdenada(ArrayList<Carta> jugada, Carta cartaElegida) {
        boolean agregada = false;
        if (jugada.isEmpty()){
            jugada.add(cartaElegida);
            System.out.println("primera carta agregada");
        }else {
            for (int i = 0; i < jugada.size(); i++){
                if (cartaElegida.numero < jugada.get(i).numero && !agregada){
                    jugada.add(i, cartaElegida);
                    agregada = true;
                    System.out.println("agregada otra carta");
                }
            }
            if (!agregada){
                System.out.println("agregada carta al final");
                jugada.add(cartaElegida);
            }
        }
    }

    //sobre opciones de mesa
    @Override
    public void modoExpres() throws RemoteException {
        modo = modoDeJuego.EXPRES;
        notificarObservadores("cambios en opciones de mesa");
    }

    @Override
    public void modoPuntos() throws RemoteException {
        modo = modoDeJuego.JUEGOAPUNTOS;
        notificarObservadores("cambios en opciones de mesa");
    }

    @Override
    public String getModoActual() throws RemoteException {
        return String.valueOf(modo);
        //devuelve el modo pasado a string
    }

    @Override
    public void modificarCompetitivo() throws RemoteException {
        if (estadoCompetitivo){
            estadoCompetitivo = false;
        }else {
            estadoCompetitivo = true;
        }
        notificarObservadores("cambios en opciones de mesa");
    }

    @Override
    public boolean getEstadoCompetitivo() throws RemoteException {
        return estadoCompetitivo;
    }

    @Override
    public void cambiarTiempoTurno(int tiempoEstablecido) throws RemoteException {
        tiempoDeTurno = tiempoEstablecido;
        notificarObservadores("cambios en opciones de mesa");
    }

    public int getCantidadTiempoTurno() throws RemoteException{
        return tiempoDeTurno;
    }

    @Override
    public void modificarOpcionPublico() throws RemoteException {
        if (publicoPermitido){
            publicoPermitido = false;
        }else {
            publicoPermitido = true;
        }
    }

    public boolean isPublicoPermitido() throws RemoteException {
        return publicoPermitido;
    }

    @Override
    public void mostrarMensajeEnChat(String txtIngresado) throws RemoteException {
        notificarObservadores(txtIngresado);
    }
    //apuestas
    public void apostarFichas(int cantFichas) throws RemoteException{
        //apuesta la misma cantidad por todos los jugadores
        // y si no aceptan apostar luego se puede cancelar y devolver las fichas
        for (Jugador jugador: jugadores) {
            jugador.setCantApostada(cantFichas);
            System.out.println(jugador.getCantApostada() + " - " + cantFichas);
            jugador.restarFichasTotales(cantFichas);
            mesaDeJuego.agregarApuesta(cantFichas);
        }
        apuestasActivas = true;
        notificarObservadores("nueva apuesta");
    }

    public void cancelarApuestas()throws RemoteException{
        for (Jugador jugador: jugadores) {
            jugador.sumarFichasTotales(jugador.getCantApostada());
            mesaDeJuego.sacarApuesta(jugador.getCantApostada());
            jugador.setCantApostada(0);
        }
        apuestasActivas = false;
        notificarObservadores("apuesta cancelada");
    }
    @Override
    public boolean puedenApostarJugadores(int apuesta) throws RemoteException {
        boolean resultado = true;
        for (Jugador jugador:jugadores) {
            if (jugador.getFichasTotales() < apuesta){
                resultado = false;
            }
        }
        return resultado;
    }

    @Override
    public int getCantApostada(String nombreJugador) throws RemoteException {
        Jugador jugadorActual = buscarJugador(nombreJugador);
        return jugadorActual.getCantApostada();
    }

    public boolean isApuestasActivas()throws RemoteException{
        return apuestasActivas;
    }

    public void apostarFichasJugador(String nombreJugador) throws RemoteException{
        //este metodo va a sacarle las fichas a los nuevos jugadores si hay una apuesta activa
        Jugador jugadorActual = buscarJugador(nombreJugador);
        //la apuesta sera segun la cantidad apostada por el anfitrion
        int cantFichas = jugadores.get(0).getCantApostada();
        jugadorActual.setCantApostada(cantFichas);
        jugadorActual.restarFichasTotales(cantFichas);
        mesaDeJuego.agregarApuesta(cantFichas);
    }

    @Override
    public int getCantidadTotalApuesta() throws RemoteException {
        return mesaDeJuego.getBoteApuesta();
    }


//sobre el ganador
    @Override
    public String getNombreGanador() throws RemoteException {
        return ganador.getNombre();
    }

    @Override
    public int getPuntosGanador() throws RemoteException {
        return ganador.getPuntosTotalesXP();
    }




//sobre finalizar partida
    public void pedidoAnularPartidaAmistosamente() throws RemoteException {
        notificarObservadores("pedido anular partida");
    }

    public void anularPartida(boolean anular) throws RemoteException {
        if (anular){
            if (jugadores.size() - 1 == solicitudDeAnularPartida){
                solicitudDeAnularPartida = 0;
                finalizarPartidaAmistosamente();
            }else {
                solicitudDeAnularPartida++;
            }
        } else{
            solicitudDeAnularPartida = 0;
            notificarObservadores("continuar turno jugador");
        }
    }

    private void finalizarPartidaAmistosamente() throws RemoteException {
        for (Jugador jugador: jugadores) {
            jugador.sumarFichasTotales(jugador.getCantApostada());
            mesaDeJuego.sacarApuesta(jugador.getCantApostada());
            jugador.setCantApostada(0);
        }
        System.out.println("finalizo amistosamente");
        partidaFinalizada = true;
        apuestasActivas = false;
        notificarObservadores("finalizo partida amistosamente");
    }

    private void cerrarPartida(String nombreJugador) throws RemoteException{
        if (modo.equals(modoDeJuego.EXPRES) || quedaUnJugador() || todosEliminados()){
            juegoIniciado = false;
            partidaFinalizada = true;
            establecerGanador();
            if (estadoCompetitivo) {
                contarPuntosTotales();
            }
            if (apuestasActivas && estadoCompetitivo){
                ganador.sumarFichasTotales(mesaDeJuego.otorgarFichasAlGanador());
            }
            //la partida fue cerrada por lo que se puede iniciar una nueva si se quiere
            notificarObservadores("partida cerrada");
        }else {
            establecerResultadosRonda();
            if (!quedaUnJugador() || !todosEliminados()) {
                //si llega aca significa que la ronda fue cerrada por lo que se iniciara una nueva
                notificarObservadores("ronda cerrada");
            }else {
                cerrarPartida(nombreJugador);
            }
        }
    }

    @Override
    public void finalizarPartida(String nombreJugador) throws RemoteException {
        Jugador jugadorActual = buscarJugador(nombreJugador);
        if (jugadorActual.getCartasEnMano().isEmpty() && modo.equals(modoDeJuego.JUEGOAPUNTOS) && !quedaUnJugador() && !todosEliminados()){
            System.out.println("finalizo la ronda!");
            establecerResultadosRonda();
            if (!quedaUnJugador() && !todosEliminados()) {
                finRonda = true;
                notificarObservadores("fin ronda");
            }
        }
        if (jugadorActual.getCartasEnMano().isEmpty() || quedaUnJugador() || todosEliminados()){
            juegoIniciado = false; //se desactiva el juego iniciado
            if (quedaUnJugador() || todosEliminados()){
                partidaFinalizada = true;
                establecerGanador();
                if (estadoCompetitivo) {
                    contarPuntosTotales();
                }
                if (apuestasActivas && estadoCompetitivo){
                    ganador.sumarFichasTotales(mesaDeJuego.otorgarFichasAlGanador());
                }
                notificarObservadores("fin de partida");
            } else if (modo.equals(modoDeJuego.EXPRES)) {
                partidaFinalizada = true;
                establecerGanador();
                contarPuntosTotales();
                if (apuestasActivas && estadoCompetitivo){
                    ganador.sumarFichasTotales(mesaDeJuego.otorgarFichasAlGanador());
                }
                notificarObservadores("fin de partida");
            }
        }
    }

    private void establecerResultadosRonda() throws RemoteException {
            contarPuntosPartida();
            establecerGanador();
        if (ganador.getHizoRummy()){
            duplicarPuntosCartas();
        }
        comprobarEliminados();
    }

    private void duplicarPuntosCartas() {
        //si el ganador hizo rummy a los que perdieron les duplica la cantidad de puntos de las cartas
        int puntos;
        for (Jugador jugador:jugadores) {
            if (ganador != jugador){
                puntos = jugador.getPuntosDePartida() * 2;
                jugador.setPuntosDePartida(puntos);
            }
        }
    }

    private void establecerGanador() throws RemoteException {
        if (quedaUnJugador()){
            for (Jugador jugador: jugadores) {
                if (!jugador.isEliminado()){
                    ganador = jugador;
                }
            }
        } else if (todosEliminados()) {
            ganador = obtenerJugadorConMenosPuntos();
        }else {
            //en este caso algun jugador gano la ronda por quedarse sin cartas asi que se lo busca
            for (Jugador jugador:jugadores) {
                if (jugador.getCartasEnMano().isEmpty()){
                    ganador = jugador;
                }
            }
            if (ganador == null){
                //si llega aca es porque se cerro la partida en modo expres y ningun jugador se quedo sin cartas
                // o que en el modo por puntos hay mas de un jugador y solo se termino la ronda
                ganador = obtenerJugadorConMenosPuntos();
            }
        }
    }

    private Jugador obtenerJugadorConMenosPuntos() {
        int menorCantPuntos = 500000;//estalezco puntos base para comparar luego con los jugadores
        Jugador menorJugadorActual = null;
        for (Jugador jugador:jugadores) {
            if (jugador.getPuntosDePartida() < menorCantPuntos){
                menorCantPuntos = jugador.getPuntosDePartida();
                menorJugadorActual = jugador;
            } else if (jugador.getPuntosDePartida() == menorCantPuntos) {
                menorJugadorActual = jugadores.get(0); //en el caso que haya empate gana el anfitrion si no se encuentra otro con menor cantidad de puntos
            }
        }
        if (menorJugadorActual == null){
            menorJugadorActual = jugadores.get(0);
            //pone al anfitrion de ganador para no declarar un ganador nulo
        }
        return menorJugadorActual;
    }

    private boolean quedaUnJugador() throws RemoteException {
        boolean resultado = false;
        int eliminados = contarEliminados();
        if (jugadores.size() - 1 == eliminados){
            //esta condicion se refiere a que solo queda un jugador
            resultado = true;
        }
        return resultado;
    }

    public int contarEliminados() throws RemoteException{
        int eliminados = 0;
        for (Jugador jugadorActual:jugadores) {
            if (jugadorActual.isEliminado()){
                eliminados++;
            }
        }
        return eliminados;
    }

    private boolean todosEliminados() throws RemoteException {
        boolean resultado = false;
        int eliminados = contarEliminados();
        if (jugadores.size() == eliminados){
            //esta condicion se refiere a que estan todos eliminados
            resultado = true;
        }
        return resultado;
    }
    private void comprobarEliminados() throws RemoteException{
        //boolean nuevoEliminado = false;
        if (modo.equals(modoDeJuego.JUEGOAPUNTOS)){
            for (Jugador jugadorActual:jugadores) {
                if (jugadorActual.getPuntosDePartida() >= limitePuntos && !jugadorActual.isEliminado()){
                    jugadorActual.setEliminado(true);
                    //nuevoEliminado = true;
                }
            }
            /*if (nuevoEliminado){
                notificarObservadores("jugador eliminado");
            }*/
        }
    }

    private void contarPuntosTotales() throws RemoteException {
        if (modo.equals(modoDeJuego.JUEGOAPUNTOS)){
            if (quedaUnJugador() || todosEliminados()){
                //solo va a contar los puntos totales cuando se termine la partida
                // por lo que si termina una ronda no cuenta los totales
                sumarPuntosXP();
            }
        }else {
            sumarPuntosXP();
        }
    }

    private void sumarPuntosXP() {
        int puntosTotales = 0;
        if (modo.equals(modoDeJuego.JUEGOAPUNTOS)){
            if (jugadores.size() == 2){
                puntosTotales += 20;
            } else if (jugadores.size() == 3) {
                puntosTotales += 30;
            } else if (jugadores.size() == 4) {
                puntosTotales += 40;
            }
        }else {
            if (jugadores.size() == 2){
                puntosTotales += 10;
            } else if (jugadores.size() == 3) {
                puntosTotales += 15;
            } else if (jugadores.size() == 4) {
                puntosTotales += 20;
            }
        }
        if (ganador.getFichasGanadasPartida() >= 1000 && ganador.getFichasGanadasPartida() <= 5000){
            puntosTotales += 4;
        } else if (ganador.getFichasGanadasPartida() >= 5001 && ganador.getFichasGanadasPartida() <= 25000) {
            puntosTotales += 8;
        }else if (ganador.getFichasGanadasPartida() >= 25001 && ganador.getFichasGanadasPartida() <= 50000) {
            puntosTotales += 12;
        }else if (ganador.getFichasGanadasPartida() >= 50001 && ganador.getFichasGanadasPartida() <= 100000) {
            puntosTotales += 16;
        }else if (ganador.getFichasGanadasPartida() >= 100001 && ganador.getFichasGanadasPartida() <= 200000) {
            puntosTotales += 20;
        }else if (ganador.getFichasGanadasPartida() >= 200001 && ganador.getFichasGanadasPartida() <= 400000) {
            puntosTotales += 24;
        }else if (ganador.getFichasGanadasPartida() >= 400001 && ganador.getFichasGanadasPartida() <= 600000) {
            puntosTotales += 28;
        }else if (ganador.getFichasGanadasPartida() >= 600001 && ganador.getFichasGanadasPartida() <= 1000000) {
            puntosTotales += 32;
        }else if (ganador.getFichasGanadasPartida() >= 1000001 && ganador.getFichasGanadasPartida() <= 2500000) {
            puntosTotales += 36;
        }else if (ganador.getFichasGanadasPartida() >= 2500001 && ganador.getFichasGanadasPartida() <= 5000000) {
            puntosTotales += 40;
        }else if (ganador.getFichasGanadasPartida() > 5000000 ) {
            puntosTotales += 44;
        }
        //le suma los puntos de xp que consiguio al ganador y a los perderos les agrega 5 puntos
        ganador.sumarPuntosTotalesXP(puntosTotales);
        for (Jugador jugador:jugadores) {
            if (!jugador.equals(ganador)){
                jugador.sumarPuntosTotalesXP(5);
            }
        }
    }

    @Override
    public void contarPuntosPartida(){
        int puntosConseguidos;
        Carta cartaAux;
        for (int i = 0; i < jugadores.size(); i++) {
            puntosConseguidos = 0;
            for (int j = 0; j < jugadores.get(i).getCartasEnMano().size(); j++) {
                cartaAux = jugadores.get(i).getCartasEnMano().get(j);
                puntosConseguidos += cartaAux.getPuntos();
                System.out.println(cartaAux.getPuntos());
            }
            System.out.println(i + "puntos conseguidos: " + puntosConseguidos);
            jugadores.get(i).sumarPuntosDePartida(puntosConseguidos);
        }
    }

    //modo automatico
    @Override
    public void juegoAutomatico(String nombreJugador) throws RemoteException{
        ArrayList<Carta> posibleEscalera;
        ArrayList<Carta> posibleCombinacion;
        if (!tomoCartaJugadorTurno){
            sacarCartaMazo(nombreJugador);
        }
        Jugador jugadorActual = buscarJugador(nombreJugador);
        jugadorActual.setModoAutomatico(true);
        if (todosEnAutomatico()){
            finalizarPartidaAmistosamente();
        }else {
            //posible rummy
            obtenerCartasOrdenadas(jugadorActual.getCartasEnMano(), jugadorActual);
            if (mesaDeJuego.esRummy(jugadorActual.getCartasEnMano())) {
                mesaDeJuego.agregarNuevaJugada(nombreJugador, jugadorActual.getCartasEnMano(), false);
                jugadorActual.getCartasEnMano().clear();
                //como las cartas se agregaron a la jugada las elimino
                finalizarPartida(nombreJugador);
            }
            //a partir de aca las cartas deberian de seguir ordenadas por lo que solo se comprueba el palo y si el numero es continuo
            //posible escalera
            posibleEscalera = obtenerCartasPaloMasRepetido(jugadorActual.getCartasEnMano());
            if (mesaDeJuego.esEscalera(posibleEscalera)) {
                mesaDeJuego.agregarNuevaJugada(nombreJugador, posibleEscalera, false);
            } else {
                mesaDeJuego.devolverCartas(jugadorActual, posibleEscalera);
            }
            //posible combinacion
            posibleCombinacion = obtenerCartasNumeroMasRepetido(jugadorActual.getCartasEnMano());
            if (mesaDeJuego.esCombinacion(posibleCombinacion)) {
                mesaDeJuego.agregarNuevaJugada(nombreJugador, posibleCombinacion, false);
            } else {
                mesaDeJuego.devolverCartas(jugadorActual, posibleCombinacion);
            }
            //posible fin de partida
            if (jugadorActual.getCartasEnMano().isEmpty()) {
                finalizarPartida(nombreJugador);
            } else {
                int posicionCarta = (int) (random() * (jugadorActual.getCartasEnMano().size()));
                terminarTurno(posicionCarta, nombreJugador);
            }
        }
    }
    private ArrayList<Carta> obtenerCartasNumeroMasRepetido(ArrayList<Carta> cartasEnMano) {
        //hacer un remove y luego devolver las cartas si no era
        ArrayList<Carta> cartasMismoNumero;
        int numeroMasRepetido = buscarNumeroMasRepetido(cartasEnMano);
        cartasMismoNumero = new ArrayList<>();
        for (Carta carta:cartasEnMano) {
            if (carta.getNumero() == numeroMasRepetido){
                cartasMismoNumero.add(carta);
            }
        }
        mesaDeJuego.quitarCartasSeleccionadas(cartasEnMano, cartasMismoNumero);
        return  cartasMismoNumero;
    }

    private int buscarNumeroMasRepetido(ArrayList<Carta> cartasEnMano) {
        int numeroMayor = 0;
        int repeticionesCarta = 0;
        int repeticiones = 0;
        for (int i = 1; i < 14; i++) {
            //compruebo en los 13 numeros de carta a ver cual se repite mas
            for (Carta carta:cartasEnMano) {
                if (carta.getNumero() == i){
                    repeticiones++;
                }
            }
            if (repeticiones > repeticionesCarta){
                numeroMayor = i;
                repeticionesCarta = repeticiones;
            }
            repeticiones = 0;
        }
        return numeroMayor;
    }

    private ArrayList<Carta> obtenerCartasPaloMasRepetido(ArrayList<Carta> cartasEnMano) {
        Palo paloRepetido = buscarPaloMasRepetido(cartasEnMano);
        ArrayList<Carta> cartasMismoPalo = new ArrayList<>();
        for (Carta carta:cartasEnMano) {
            if (carta.getPalo().equals(paloRepetido)){
                cartasMismoPalo.add(carta);
            }
        }
        mesaDeJuego.quitarCartasSeleccionadas(cartasEnMano, cartasMismoPalo);
        return cartasMismoPalo;
    }

    private Palo buscarPaloMasRepetido(ArrayList<Carta> cartasEnMano) {
        Palo paloMasRepetido = null;
        int repeticionesPalo = 0;
        int repeticiones = 0;
        for (Palo paloActual: Palo.values()) {
            System.out.println(paloActual);
            for (Carta carta:cartasEnMano) {
                if (carta.getPalo().equals(paloActual)){
                    repeticiones++;
                }
            }
            if (repeticiones > repeticionesPalo){
                paloMasRepetido = paloActual;
                repeticionesPalo = repeticiones;
            }
        }
        return  paloMasRepetido;
    }

    @Override
    public boolean isJugadorEnAutomatico(String nombreJugador) throws RemoteException {
        Jugador jugadorActual = buscarJugador(nombreJugador);
        return jugadorActual.isEnAutomatico();
    }

    @Override
    public void desactivarJuegoAutomatico(String nombreJugador) throws RemoteException {
        Jugador jugadorActual = buscarJugador(nombreJugador);
        jugadorActual.setModoAutomatico(false);
    }

    private boolean todosEnAutomatico() {
        boolean resultado = true;
        for (Jugador jugador: jugadores) {
            if (!jugador.isEnAutomatico()){
                System.out.println("hay alguno que no esta en automatico");
                resultado = false;
            }
        }
        return resultado;
    }



    //sobre tabla de posiciones
    public ArrayList<IJugador> getTablaPosiciones()throws RemoteException{
        ArrayList<IJugador> jugadores = new ArrayList<>();
        serializador = new Serializador("top5.dat");
        Object[] recuperado = serializador.readObjects();
        if (recuperado != null){
            for (int i = 0; i < recuperado.length; i++) {
                jugadores.add((IJugador) recuperado[i]);
            }
        }
        if (!cargadosPuntosJugadoresEnTabla) {
            cargadosPuntosJugadoresEnTabla = true;
            jugadores = obtenerJugadoresPorPuntos(jugadores);
            serializador.writeOneObject(jugadores.get(0));
            for (int i = 1; i < jugadores.size(); i++) {
                serializador.addOneObject(jugadores.get(i));
            }
        }
        return jugadores;
    }

    public ArrayList<IJugador> obtenerJugadoresPorPuntos(ArrayList<IJugador> jugadores) throws RemoteException{
        devolverCartasAMesa();
        //devuelvo las cartas a mesa para no pasarlas al archivo
        for (Jugador jugador:this.jugadores) {
            agregarJugadorOrdenado(jugadores, jugador);
        }
        return jugadores;
    }

    private void agregarJugadorOrdenado(ArrayList<IJugador> jugadores, Jugador jugador) {
        boolean agregado = false;
        if (jugadores.isEmpty()){
            jugadores.add(jugador);
        }else {
            if (jugadorEnTabla(jugadores, jugador)) {
                sumarPuntosJugador(jugadores, jugador);
            }else {
                for (int i = 0; i < jugadores.size(); i++) {

                    if (jugador.getPuntosTotalesXP() >= jugadores.get(i).getPuntosTotalesXP() && !agregado){
                        jugadores.add(i, jugador);
                        agregado = true;
                    }
                }
                if (!agregado){
                    jugadores.add(jugador);
                }
            }
        }
    }

    private void sumarPuntosJugador(ArrayList<IJugador> jugadores, Jugador jugador) {
        Jugador jugadorAux = null;
        for (IJugador ijugador: jugadores) {
            if (jugador.getNombre().equals(ijugador.getNombre())){
                ijugador.sumarPuntosTotalesXP(jugador.getPuntosTotalesXP());
                jugadorAux = (Jugador) ijugador;
                //lo elimino para luego reordenarlo en el top con su nuevo puntaje
            }
        }
        if (jugadorAux != null){
            jugadores.remove(jugadorAux);
            agregarJugadorOrdenado(jugadores,jugadorAux);
        }
    }

    private boolean jugadorEnTabla(ArrayList<IJugador> jugadores, Jugador jugador) {
        boolean resultado = false;
        for (IJugador ijugador: jugadores) {
            if (jugador.getNombre().equals(ijugador.getNombre())){
                resultado = true;
            }
        }
        return resultado;
    }

    private int buscarJugadorConMasPuntos() {
        int puntosMasAltos = 0;
        for (Jugador jugador:jugadores) {
            if (jugador.getPuntosDePartida() > puntosMasAltos){
                puntosMasAltos = jugador.getPuntosDePartida();
            }
        }
        return puntosMasAltos;
    }

//sobre fin de turno
    @Override
    public void terminarTurno(Integer cartaSeleccionada, String nombreJugador) throws RemoteException{
        Jugador jugadorActual = buscarJugador(nombreJugador);
        if (jugadorActual.getCartasEnMano().isEmpty() && cartaSeleccionada == -1){
            finalizarPartida(nombreJugador);
        }
        Carta cartaTirada = jugadorActual.tirarCarta(cartaSeleccionada);
        //le saca la carta que selecciono
        if (cartaTirada != null) {
            if (jugadorActual.getCartasEnMano().isEmpty()) {
                mazoDeJuego.agregarCartaBocaArriba(cartaTirada);
                finalizarPartida(nombreJugador);
            } else if (jugadorActual.getCartasEnMano().size() <= 2 && jugadasLlenas()) {
                cerrarPartida(nombreJugador);
            } else if (quedaUnJugador()) {
                finalizarPartida(nombreJugador);
            } else {
                mazoDeJuego.agregarCartaBocaArriba(cartaTirada);
                Jugador jugadorSiguiente = buscarJugadorIzquierda(jugadorActual);
                siguienteTurno(jugadorSiguiente);
            }
        }
    }
    private boolean jugadasLlenas() throws RemoteException{
        Jugada jugadaActual;
        boolean resultado = true;
        if (!mesaDeJuego.getListaJugada().isEmpty()){
            for (int i = 0; i < mesaDeJuego.getListaJugada().size(); i++) {
                jugadaActual = mesaDeJuego.getListaJugada().get(i);
                if (!jugadaActual.isJugadaLlena()){
                    resultado = false;
                }
            }
        }
        return resultado;
    }

    //sobre guardar, sobreescribir o cargar partida
    public void guardarPartida(String nombrePartida)throws RemoteException{
        ArrayList<PartidaGuardada> partidas = obtenerPartidasGuardadas();
        if (partidas.size() < 8){
            PartidaGuardada partida = new PartidaGuardada(nombrePartida, this, jugadores.getFirst().getNombre());
            partidas.add(partida);
            escribirArchivoPartidas(partidas);
        }
        notificarObservadores("nuevo turno");
    }

    private void escribirArchivoPartidas(ArrayList<PartidaGuardada> partidas) {
        serializador.writeOneObject(partidas.get(0));
        for (int i = 1; i < partidas.size(); i++) {
            serializador.addOneObject(partidas.get(i));
        }
    }

    private ArrayList<PartidaGuardada> obtenerPartidasGuardadas() {
        ArrayList<PartidaGuardada> partidas = new ArrayList<>();
        serializador = new Serializador("partidas.dat");
        /*if (serializador.readFirstObject() == null){
            serializador.writeOneObject()
        }*/
        Object[] recuperado = serializador.readObjects();
        if (recuperado != null){
            for (int i = 0; i < recuperado.length; i++) {
                partidas.add((PartidaGuardada) recuperado[i]);
            }
        }
        return partidas;
    }

    public ArrayList<String> obtenerListadoPartidaGuardada() throws RemoteException{
        ArrayList<PartidaGuardada> partidas = obtenerPartidasGuardadas();
        ArrayList<String> txtPartida = new ArrayList<>();
        if (!partidas.isEmpty()) {
            for (PartidaGuardada partidaActual : partidas) {
                txtPartida.add(partidaActual.toString());
            }
        }
        return txtPartida;
    }

    public void sobreescribirPartida(int posicionPartida, String nombrePartidaAGuardar)throws RemoteException{
        ArrayList<PartidaGuardada> partidas = obtenerPartidasGuardadas();
        if (partidas.size() > posicionPartida){
            PartidaGuardada partida = new PartidaGuardada(nombrePartidaAGuardar, this, jugadores.getFirst().getNombre());
            partidas.remove(posicionPartida);
            partidas.add(posicionPartida,partida);
            escribirArchivoPartidas(partidas);
        }//excepcion
        notificarObservadores("nuevo turno");
    }

    public void cargarPartida(String nombreAnfitrion, int posicionPartida)throws RemoteException{
        ArrayList<PartidaGuardada> partidas = obtenerPartidasGuardadas();
        if (partidas.size() > posicionPartida){
            PartidaGuardada partidaCargada = partidas.get(posicionPartida);
            Rummy juegoCargado = partidaCargada.getJuegoGuardado();
            Jugador anfitrionCargado = juegoCargado.jugadores.get(0);
            if (anfitrionCargado.getNombre().equals(nombreAnfitrion)){
                cargarDatosPartida(juegoCargado);
            }
        }
        notificarObservadores("juego cargado");
    }

    private void cargarDatosPartida(Rummy juegoCargado) {
        this.mazoDeJuego = juegoCargado.mazoDeJuego;
        this.jugadores = juegoCargado.jugadores;
        this.mesaDeJuego = juegoCargado.mesaDeJuego;
        //this.instancia = juegoCargado.getInstancia();   no es necesario
        this.posicionTurnoActual = juegoCargado.posicionTurnoActual;
        this.modo = juegoCargado.modo;
        this.apuestasActivas = juegoCargado.apuestasActivas;
        this.solicitudDeAnularPartida = juegoCargado.solicitudDeAnularPartida;
        this.estadoCompetitivo = juegoCargado.estadoCompetitivo;
        this.tiempoDeTurno = juegoCargado.tiempoDeTurno;
        partidaCargada = true;
    }

    public boolean isPartidaCargada() throws RemoteException{
        return partidaCargada;
    }

    @Override
    public String activarJugadorSiguiente() throws RemoteException {
        //tiene que ver con cargar partida una vez se carga se necesita
        String nombreJugadorActivado = "";
        for (Jugador jugador:jugadores) {
            if (!jugador.isActivo()){
                jugador.setActivo(true);
                nombreJugadorActivado = jugador.getNombre();
            }
        }
        return nombreJugadorActivado;
    }

    @Override
    public int cantJugadoresActivos() throws RemoteException {
        int cantidadActivos = 0;
        for (Jugador jugador:jugadores) {
            if (jugador.isActivo()){
                cantidadActivos++;
            }
        }
        return cantidadActivos;
    }

    /*public void addObserver(Observer o){
        observadores.add(o);
    }*/

    /*private void notificarObservadores(int i){
        for (Observer o : observadores)
            o.notificarCambio(i);
    }*/
}
