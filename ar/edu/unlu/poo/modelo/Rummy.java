package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.exceptions.JugadorInexistente;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static java.lang.Math.random;

public class Rummy extends ObservableRemoto implements IRummy {
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
    }

    public void agregarJugador(Jugador nuevoJugador, boolean anfitrion) throws RemoteException{
        if (!juegoIniciado){
            nuevoJugador.setJefeMesa(anfitrion);
            jugadores.add(nuevoJugador);
            notificarObservadores("nuevo jugador");
        }
    }

    public boolean isJuegoIniciado() throws RemoteException{
        return juegoIniciado;
    }

    public void iniciarJuego() throws RemoteException {
        try {
            iniciarOtraRonda();
            juegoIniciado = true;
            if (jugadores.size() == 2){
                repartirCartasJugadores(10);
            } else if (jugadores.size() >= 3) {
                repartirCartasJugadores(7);
            }
            elegirJugadorMano();
            //notificarObservadores("cartas repartidas");
        }catch (RemoteException e){
            e.printStackTrace();
        }

    }

    public void iniciarOtraRonda() throws RemoteException {
        ganador = null;
        solicitudDeAnularPartida = 0;
        reiniciarEstadosJugadores();
        devolverCartasAMesa();
        //devuelve todas las cartas de los jugadores a la mesa para despues mezclarlas con el mazo
        mezclarMazo();
    }

    private void reiniciarEstadosJugadores() {
        for (Jugador jugadorActual:jugadores) {
            jugadorActual.setAgregoJugada(false);
            if (modo.equals(modoDeJuego.JUEGOAPUNTOS) && partidaFinalizada){
                jugadorActual.setEliminado(false);
                jugadorActual.setCantApostada(0);
                partidaFinalizada = false;
            } else if (modo.equals(modoDeJuego.EXPRES)) {
                jugadorActual.setCantApostada(0);
            }
            jugadorActual.setHizoRummy(false);
            jugadorActual.setPuntosDePartida(0);
            jugadorActual.setFichasGanadasPartida(0);
        }
    }

    private void devolverCartasAMesa() {
        Carta cartaAux;
        for (int i = 0; i < jugadores.size();i++){
            if (!jugadores.get(i).getCartasEnMano().isEmpty()){
                while (!jugadores.get(i).getCartasEnMano().isEmpty()){
                    cartaAux = jugadores.get(i).getCartasEnMano().remove(0);
                    mazoDeJuego.agregarCartaBocaArriba(cartaAux);
                }
            }
        }
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
        System.out.println("posicion turno: " + posicionTurnoActual);
        notificarObservadores("nuevo turno");
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

    @Override //
    public void repartirCartasJugadores(int cantidadCartas) throws RemoteException {
        Carta cartaAux;
        for (int i = 0; i < jugadores.size(); i++){
            mazoDeJuego.repartir(cantidadCartas, jugadores.get(i));
        }

    }

    @Override //
    public void sacarCartaMazo(String jugador) throws RemoteException{
        Jugador jugadorAux = buscarJugador(jugador);
        mazoDeJuego.sacarCartaMazo(jugadorAux);
        notificarObservadores("continuar turno jugador");
    }

    @Override //
    public void agarrarCartaBocaArriba(String nombreJugador) throws RemoteException{
        Jugador jugadorAux = buscarJugador(nombreJugador);
        if (mazoDeJuego.sacarCartaBocaArriba(jugadorAux)){
            //si pudo sacar la carta notifica el cambio a los observadores
            notificarObservadores("continuar turno jugador");
        }
    }

    public Carta getCartaBocaArriba() {
        return mazoDeJuego.cartaBocaArribaActual();
    }

    @Override
    public ITapete getMesaJugadas() throws RemoteException{
        return mesaDeJuego;
    }

    @Override
    public int getCantCartasOponente(String oponente) throws RemoteException{
        Jugador jugadorActual = buscarJugador(oponente);
        return jugadorActual.getCartasEnMano().size();
    }

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
    public int getCantidadFichas(String nombreJugador) {
        Jugador jugadorActual = buscarJugador(nombreJugador);
        return jugadorActual.getFichasTotales();
    }



    public void apostarFichas(int cantFichas) throws RemoteException{
        //apuesta la misma cantidad por todos los jugadores
        // y si no aceptan apostar luego se puede cancelar y devolver las fichas
        for (Jugador jugador: jugadores) {
            jugador.setCantApostada(cantFichas);
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

    @Override
    public String getModoActual() throws RemoteException {
        return String.valueOf(modo);
        //supuestamente devuelve el modo pasado a string
    }

    @Override
    public String getNombreGanador() throws RemoteException {
        return ganador.getNombre();
    }

    @Override
    public int getPuntosGanador() throws RemoteException {
        return ganador.getPuntosTotalesXP();
    }

    @Override
    public String getJugador(int posicion) throws RemoteException {
        return String.valueOf(jugadores.get(posicion));
    }

    @Override
    public boolean estaEliminado(String nombreJugador) throws RemoteException{
        Jugador jugadorActual = buscarJugador(nombreJugador);
        return jugadorActual.isEliminado();
    }

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
        partidaFinalizada = true;
        //pongo de ganador al anfitrion aunque no gana nada para reiniciar los estados del juego
        apuestasActivas = false;
        notificarObservadores("finalizo partida amistosamente");
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

    @Override
    public void nuevoJuego() throws RemoteException {
        juegoIniciado = false;
        notificarObservadores("nuevo juego");
    }

    /*@Override
    public void cerrarJuego() throws RemoteException {
        notificarObservadores("cerrar juego");
        System.exit(0);
    }*/

    @Override
    public ArrayList<IJugador> obtenerJugadoresPorPuntos(ArrayList<IJugador> jugadores) throws RemoteException{
        devolverCartasAMesa();
        //devuelvo las cartas a mesa para no pasarlas al archivo
        for (Jugador jugador:this.jugadores) {
            agregarJugadorOrdenado(jugadores, jugador);
        }
        return jugadores;
    }

    @Override
    public void eliminarJugador(String nombreJugador) throws RemoteException {
        Jugador jugadorBorrado = buscarJugador(nombreJugador);
        jugadores.remove(jugadorBorrado);
        if (jugadorBorrado.getJefeMesa() && !jugadores.isEmpty()){
            jugadores.get(0).setJefeMesa(true);
        }
        if (juegoIniciado){
            solicitudDeAnularPartida = jugadores.size() - 1;
            //obligo anular la partida por la falta de un jugador
            anularPartida(true);
        }else {
            notificarObservadores("jugador sacado del juego");
        }
    }

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

    @Override
    public void modificarCompetitivo() throws RemoteException {
        if (estadoCompetitivo){
            estadoCompetitivo = false;
        }else {
            estadoCompetitivo = true;
        }
        /*if (juegoIniciado){
            notificarObservadores("continuar turno jugador");
        }else {
            notificarObservadores("continuar espera");
        }*/
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

    @Override
    public void comprobarRummy(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador)throws RemoteException {
        Jugador jugadorActual = buscarJugador(nombreJugador);
        if (!tieneJugada(jugadorActual) && !agregoJugada(jugadorActual)) {
            if (posicionesSeleccionadas.size() == jugadorActual.getCartasEnMano().size()) {
                ArrayList<Carta> cartasSeleccionadas = new ArrayList<>();
                obtenerCartasOrdenadas(cartasSeleccionadas, jugadorActual);
                if (esRummy(cartasSeleccionadas)) {
                    jugadorActual.setHizoRummy(true);
                    agregarJugada(nombreJugador, cartasSeleccionadas, false);
                    notificarObservadores("jugada agregada");
                    finalizarPartida(nombreJugador);
                }else {
                    devolverCartas(jugadorActual,cartasSeleccionadas);
                    notificarObservadores("continuar turno jugador");
                    //excepcion
                }
            } else {
                notificarObservadores("continuar turno jugador");
                //excepcion de que no selecciono todas las cartas
            }
        }else {
            notificarObservadores("continuar turno jugador");
            //excepcion
        }
    }

    private boolean agregoJugada(Jugador jugadorActual) {
        return jugadorActual.getAgregoJugada();
    }

    @Override
    public void comprobarCombinacion(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador)throws RemoteException {
        Jugador jugadorActual = buscarJugador(nombreJugador);
        boolean estaLLena = false;
        ArrayList<Carta> cartasSeleccionadas = new ArrayList<>();
        obtenerCartasPorPosicion(posicionesSeleccionadas, jugadorActual, cartasSeleccionadas);
        if (esCombinacion( cartasSeleccionadas)){
            if (cartasSeleccionadas.size() == 4){
                estaLLena = true;
            }
            agregarJugada(nombreJugador, cartasSeleccionadas, estaLLena);

            notificarObservadores("jugada agregada");
        }else {
            devolverCartas(jugadorActual, cartasSeleccionadas);
            //excepcion
            notificarObservadores("continuar turno jugador");
        }
    }

    private boolean esCombinacion(ArrayList<Carta> cartasSeleccionadas) {
        Carta siguiente;
        Carta actual;
        boolean resultado = true;
        for (int i = 0; i < cartasSeleccionadas.size(); i++) {
            if ((i +1) != cartasSeleccionadas.size()){
                actual = cartasSeleccionadas.get(i);
                siguiente = cartasSeleccionadas.get(i + 1);
                if (actual.getNumero() != siguiente.getNumero()){
                    resultado = false;
                }
            }
        }
        return resultado;
    }

    private void agregarJugada(String nombreJugador, ArrayList<Carta> cartasJugada, boolean esLlena) {
        Jugada nuevaJugada = new Jugada();
        nuevaJugada.setJugadaLlena(esLlena);
        nuevaJugada.setNombreCreadorJugada(nombreJugador);
        nuevaJugada.setCartasJugada(cartasJugada);
        mesaDeJuego.agregarJugada(nuevaJugada);
    }

    private boolean tieneJugada(Jugador jugadorActual) {
        boolean resultado = false;
        for (Jugada jugada: mesaDeJuego.getJugada()) {
            if (jugada.getNombreCreadorJugada().equals(jugadorActual.getNombre())){
                resultado = true;
            }
        }
        return resultado;
    }

    private boolean esRummy(ArrayList<Carta> cartasAux) {
        //solo se verifica que sea una escalera porque el rummy debe ser una jugada unica
        // y es imposible hacer una unica jugada con solo numeros iguales
        return esEscalera(cartasAux);

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
        quitarCartasSeleccionadas(jugadorActual.getCartasEnMano(), cartas);
        posicionesSeleccionadas.clear();
    }

    @Override
    public void mezclarMazo() throws RemoteException{
        //cuando el mazo se quede sin cartas se van a agregar de manera random al mazo las cartas que quedaron boca arriba
        mazoDeJuego.mezclarMazo();
    }

    @Override
    public void comprobarEscalera(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador)throws RemoteException{
        Jugador jugadorActual = buscarJugador(nombreJugador);
        boolean estaLLena = false;
        if (posicionesSeleccionadas.size() <= jugadorActual.getCartasEnMano().size() && posicionesSeleccionadas.size() >= 3) {
            ArrayList<Carta> cartasSeleccionadas = new ArrayList<>();
            obtenerCartasPorPosicion(posicionesSeleccionadas, jugadorActual, cartasSeleccionadas);
            //solo acomoda los valores en el caso de que esten el as al principio y la k al final
            acomodarValoresExtremos(cartasSeleccionadas);
            if (esEscalera(cartasSeleccionadas)){
                if (cartasSeleccionadas.size() == 13){
                    estaLLena = true;
                }
                agregarJugada(nombreJugador, cartasSeleccionadas, estaLLena);
                notificarObservadores("jugada agregada");
            }else {
                devolverCartas(jugadorActual, cartasSeleccionadas);
                //excepcion?
                notificarObservadores("continuar turno jugador");
            }
        }else {
            notificarObservadores("continuar turno jugador");
            //excepcion de que no selecciono todas las cartas
        }
    }

    private void devolverCartas(Jugador jugadorActual, ArrayList<Carta> cartasAux) {
        while (!cartasAux.isEmpty()){
            jugadorActual.devolverCarta(cartasAux.remove(0));
        }
    }

    @Override
    public boolean esEscalera(ArrayList<Carta> nuevaJugada) {
        boolean resultado = true;
        for (int i = 0; i < nuevaJugada.size(); i++) {
            if ((i +1) != nuevaJugada.size()) {
                if (nuevaJugada.get(i).numero == 13 && nuevaJugada.get(i + 1).numero == 1){

                } else if (nuevaJugada.get(i).numero == 13 && nuevaJugada.get(i + 1).numero != 1) {
                    resultado = false;
                    //compruebo que el siguiente a la k en la escalera sea el As
                } else if ((nuevaJugada.get(i).numero + 1) != nuevaJugada.get(i + 1).numero) {
                    //compruebo que el numero actual sumado 1 sea igual al numero siguiente
                    //(ejemplo: carta1 = 7 y carta2 = 8 entonces si carta1 + 1 valor es igual a carta 2 se confirma que esta en escalera)
                    resultado = false;
                }
            }
        }
        return resultado;
    }
    @Override
    public void acomodarValoresExtremos(ArrayList<Carta> nuevaJugada) {
        Carta cartaAux;
        int posAgregada = 0;
        int siguiente;
        int actual;
        int posUltimaCarta = nuevaJugada.size() - 1;
        if (nuevaJugada.get(0).numero == 1){
            if (nuevaJugada.get(posUltimaCarta).numero == 13) {
                for (int i = 0; i < nuevaJugada.size(); i++) {
                    if ((i +1) != nuevaJugada.size()){
                    //si el numero anterior sumado 1 no es igual al siguiente
                    // es posible que sea el inicio de la escalera
                        siguiente = nuevaJugada.get(i + 1).numero;
                        actual = (nuevaJugada.get(i).numero + 1);
                        if (siguiente > actual) {
                            System.out.println("ejecutado");
                            cartaAux = nuevaJugada.remove(i + 1);
                            nuevaJugada.add(posAgregada, cartaAux);
                            posAgregada++;
                            //posAgregada sirve para saber en que posicion mover la carta mal ordenada
                        }
                    }
                }
            }
        }
    }


    @Override
    public void agregarCartaAJugada(ArrayList<Integer> posicionesSeleccionadas, int posicionJugada, String nombreJugador) throws RemoteException {
        ArrayList<Carta> jugada = buscarJugada(posicionJugada);
        if (jugada != null) {
            Jugador jugadorActual = buscarJugador(nombreJugador);
            ArrayList<Carta> cartasSeleccionadas = new ArrayList<>();
            obtenerCartasPorPosicion(posicionesSeleccionadas, jugadorActual, cartasSeleccionadas);
            //primero se comprueba si se quiere agregar la carta a una jugada del mismo numero o del mismo palo
            if (esEscalera(jugada)){
                agregarCartasSeleccionadas(cartasSeleccionadas, jugada);
                acomodarValoresExtremos(jugada);
                //comprobamos si con las cartas agregadas sigue siendo una escalera
                if (esEscalera(jugada)){
                    if (jugada.size() == 13){
                        mesaDeJuego.getJugada().get(posicionJugada).setJugadaLlena(true);
                    }
                    jugadorActual.setAgregoJugada(true);
                    notificarObservadores("cartas agregadas");
                }else{
                    quitarCartasSeleccionadas(jugada, cartasSeleccionadas);
                    devolverCartas(jugadorActual,cartasSeleccionadas);
                    //excepcion
                }
            }else{
                agregarCartasSeleccionadas(cartasSeleccionadas, jugada);
                if (esCombinacion(jugada)){
                    if (jugada.size() == 4){
                        mesaDeJuego.getJugada().get(posicionJugada).setJugadaLlena(true);
                    }
                    jugadorActual.setAgregoJugada(true);
                    notificarObservadores("cartas agregadas");
                }else {
                    quitarCartasSeleccionadas(jugada, cartasSeleccionadas);
                    devolverCartas(jugadorActual,cartasSeleccionadas);
                    //excepcion
                }
            }
        }else{
            //excepcion
        }
        notificarObservadores("continuar turno jugador");
    }

    private void agregarCartasSeleccionadas(ArrayList<Carta> cartasSeleccionadas, ArrayList<Carta> jugada) {
        for (Carta carta: cartasSeleccionadas) {
            agregarCartaOrdenada(jugada, carta);
        }
    }

    private void quitarCartasSeleccionadas(ArrayList<Carta> jugada, ArrayList<Carta> cartasSeleccionadas) {
        for (Carta carta:cartasSeleccionadas) {
            jugada.remove(carta);
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

    @Override
    public ArrayList<Carta> buscarJugada(int posicionDeLaJugada) throws RemoteException{
        return mesaDeJuego.getJugada().get(posicionDeLaJugada).getCartasJugada();
    }

    @Override
    public void terminarTurno(ArrayList<Integer> cartasSeleccionadas, String nombreJugador)throws RemoteException{
        Jugador jugadorActual = buscarJugador(nombreJugador);
        Carta cartaTirada = jugadorActual.tirarCarta(cartasSeleccionadas.get(0));
        //le saca la carta que selecciono
        if (jugadorActual.getCartasEnMano().isEmpty()){
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

    private void cerrarPartida(String nombreJugador) throws RemoteException{
        finalizarPartida(nombreJugador);
    }

    private boolean jugadasLlenas() throws RemoteException{
        Jugada jugadaActual;
        boolean resultado = true;
        if (!mesaDeJuego.getJugada().isEmpty()){
            for (int i = 0; i < mesaDeJuego.getJugada().size(); i++) {
                jugadaActual = mesaDeJuego.getJugada().get(i);
                if (!jugadaActual.isJugadaLlena()){
                    resultado = false;
                }
            }
        }
        return resultado;
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




    @Override
    public void finalizarPartida(String nombreJugador) throws RemoteException {
        Jugador jugadorActual = buscarJugador(nombreJugador);
         if (modo.equals(modoDeJuego.JUEGOAPUNTOS)){
            contarPuntosPartida();
        }
        establecerGanador();
         if (ganador.getHizoRummy()){
             duplicarPuntosCartas();
             //vuelve a comprobar los eliminados despues de contar los puntos
             comprobarEliminados();
         }
         if (!estadoCompetitivo){
            contarPuntosTotales();
         }
        if (jugadorActual.getCartasEnMano().isEmpty() || quedaUnJugador() || todosEliminados()){
            juegoIniciado = false; //se desactiva el juego iniciado
            if (quedaUnJugador() || todosEliminados()){
                partidaFinalizada = true;
            }
            if (apuestasActivas && estadoCompetitivo){
                ganador.sumarFichasTotales(mesaDeJuego.otorgarFichasAlGanador());
            }
            notificarObservadores("fin de partida");
            //dialogo de mensaje que avise que la partida fue finalizada y tal vez algun boton de nueva partida
            //mostrar clasificacion y sumar los puntos del jugador a la clasificacion
        }else if (jugadorActual.getCartasEnMano().size() <= 2 && jugadasLlenas()){
            if (modo.equals(modoDeJuego.EXPRES)){
                juegoIniciado = false;
                //la partida fue cerrada en modo expres por lo que se puede iniciar una nueva si se quiere
                notificarObservadores("partida cerrada modo expres");
            }else {
                //si llega aca significa que la ronda fue cerrada por lo que se iniciara una nueva
                notificarObservadores("partida cerrada");
            }
        }else {
            iniciarOtraRonda();
            notificarObservadores("nueva ronda");
        }
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
    private void comprobarEliminados() throws RemoteException {
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
                cartaAux = jugadores.get(i).getCartasEnMano().get(i);
                puntosConseguidos += cartaAux.getPuntos();
            }
            jugadores.get(i).sumarPuntosDePartida(puntosConseguidos);
        }
    }

    @Override
    public void agregarPuntosClasificacion()throws RemoteException {

    }

    @Override
    public void siguienteTurno(Jugador jugadorIzquierda)throws RemoteException{
        //busca la posicion del jugador que tiene el turno y luego de guardarla avisa el cambio de turno
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).equals(jugadorIzquierda)){
                posicionTurnoActual = i;
                notificarObservadores("nuevo turno");
            }
        }
    }

    public int getJugadoresSize() {
        return jugadores.size();
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
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



    /*public void addObserver(Observer o){
        observadores.add(o);
    }*/

    /*private void notificarObservadores(int i){
        for (Observer o : observadores)
            o.notificarCambio(i);
    }*/
}
