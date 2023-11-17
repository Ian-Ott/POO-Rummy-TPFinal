package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.random;

public class Rummy extends ObservableRemoto implements IRummy {
    private Mazo mazoDeJuego;
    private ArrayList<Jugador> jugadores;
    private Tapete mesaDeJuego;
    private static Rummy instancia;
    private boolean juegoIniciado;
    private int posicionTurnoActual;
    //private ArrayList<Observer> observadores = new ArrayList<>();

    public Rummy() throws RemoteException {
        jugadores = new ArrayList<>();
        mazoDeJuego = new Mazo();
        mesaDeJuego = new Tapete();
        juegoIniciado = false;
        Tapete nuevoTapete = new Tapete();
        /*jugadores.add(jugador1);
        jugadores.add(jugador2);
        if (jugador3 != null){
            jugadores.add(jugador3);
        }
        if (jugador4 != null){
            jugadores.add(jugador4);
        }*/

    }

    public void agregarJugador(Jugador nuevoJugador) throws RemoteException{
        if (!juegoIniciado){
            jugadores.add(nuevoJugador);
            notificarObservadores("nuevo jugador");
        }
    }

    public boolean isJuegoIniciado() throws RemoteException{
        return juegoIniciado;
    }

    public void iniciarJuego() throws RemoteException {
        try {
            juegoIniciado = true;
            if (jugadores.size() == 2){
                repartirCartasJugadores(10);
            } else if (jugadores.size() >= 3) {
                repartirCartasJugadores(7);
            }
            notificarObservadores("juego iniciado");
            notificarObservadores("cartas repartidas");
            elegirJugadorMano();
        }catch (RemoteException e){
            e.printStackTrace();
        }

    }

    private void elegirJugadorMano() throws RemoteException {
        //aca se elige cual jugador va a ser mano y se comienza el nuevo turno
        siguienteTurno(jugadores.get((int) (random() * (jugadores.size() - 1))));
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

    @Override
    public void repartirCartasJugadores(int cantidadCartas) throws RemoteException {
        try {
            Carta cartaAux;
            for (int i = 0; i < jugadores.size(); i++){
                mazoDeJuego.repartir(cantidadCartas, jugadores.get(i));
                for (int j = 0; j < jugadores.get(i).getCartasEnMano().size(); j++){
                    cartaAux = (Carta) jugadores.get(i).getCartasEnMano().get(j);
                    notificarObservadores("jugador"+ i + " "+ cartaAux);
                }
            }
        }catch (RemoteException e){
            e.printStackTrace();
        }

    }

    @Override
    public void sacarCartaMazo(String jugador) throws RemoteException{
        Jugador jugadorAux = buscarJugador(jugador);
        mazoDeJuego.sacarCartaMazo(jugadorAux);
        notificarObservadores("continuar turno jugador");
    }

    @Override
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
    public ITapete getJugadas() throws RemoteException{
        return mesaDeJuego;
    }


    @Override
    public void comprobarRummy(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador)throws RemoteException {
        Jugador jugadorActual = buscarJugador(nombreJugador);
        if (!tieneJugada(jugadorActual)) {
            if (posicionesSeleccionadas.size() == jugadorActual.getCartasEnMano().size()) {
                ArrayList<Carta> cartasSeleccionadas = new ArrayList<>();
                obtenerCartasOrdenadas(cartasSeleccionadas, jugadorActual);
                if (esRummy(cartasSeleccionadas)) {
                    System.out.println("es rummy");
                    agregarJugada(nombreJugador, cartasSeleccionadas);
                    notificarObservadores("jugada agregada");
                    finalizarPartida(nombreJugador); //ojo capaz no anda
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

    @Override
    public void comprobarCombinacion(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador)throws RemoteException {
        Jugador jugadorActual = buscarJugador(nombreJugador);
        ArrayList<Carta> cartasSeleccionadas = new ArrayList<>();
        obtenerCartasPorPosicion(posicionesSeleccionadas, jugadorActual, cartasSeleccionadas);
        if (esCombinacion( cartasSeleccionadas)){
            agregarJugada(nombreJugador, cartasSeleccionadas);
            System.out.println("es combinacion");
            notificarObservadores("jugada agregada");
        }else {
            devolverCartas(jugadorActual, cartasSeleccionadas);
            //excepcion
            System.out.println("error no es combinacion");
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

    private void agregarJugada(String nombreJugador, ArrayList<Carta> cartasJugada) {
        Jugada nuevaJugada = new Jugada();
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
    public void crearTapeteConJugada(ArrayList<Carta> jugada){
        //borrar
    }


    @Override
    public void comprobarEscalera(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador)throws RemoteException{
        Jugador jugadorActual = buscarJugador(nombreJugador);
        System.out.println("llego hasta aca1");
        if (posicionesSeleccionadas.size() <= jugadorActual.getCartasEnMano().size() && posicionesSeleccionadas.size() >= 3) {
            System.out.println("llego hasta aca");
            ArrayList<Carta> cartasSeleccionadas = new ArrayList<>();
            obtenerCartasPorPosicion(posicionesSeleccionadas, jugadorActual, cartasSeleccionadas);
            //solo acomoda los valores en el caso de que esten el as al principio y la k al final
            System.out.println("llego hasta aca2");
            acomodarValoresExtremos(cartasSeleccionadas);
            System.out.println("llego hasta aca3");
            if (esEscalera(cartasSeleccionadas)){
                System.out.println("es escalera");
                agregarJugada(nombreJugador, cartasSeleccionadas);
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
                if (nuevaJugada.get(i).numero == 13 && nuevaJugada.get(i + 1).numero != 1) {
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
                    //si el numero anterior sumado 1 no es igual al siguiente
                    // es posible que sea el inicio de la escalera
                    siguiente = nuevaJugada.get(i + 1).numero;
                    actual = (nuevaJugada.get(i).numero + 1);
                    if (siguiente > actual) {
                        cartaAux = nuevaJugada.remove(i + 1);
                        nuevaJugada.add(posAgregada, cartaAux);
                        posAgregada++;
                        //posAgregada sirve para saber en que posicion mover la carta mal ordenada
                    }
                }
            }
        }
    }


    @Override
    public ArrayList<Carta> generarPosibleEscalera(Carta carta1, Carta carta2, Carta carta3) {
        ArrayList<Carta> posibleJugada = new ArrayList<>();
        posibleJugada.add(carta1);
        agregarCartaOrdenada(posibleJugada, carta2);
        agregarCartaOrdenada(posibleJugada, carta3);
        return posibleJugada;
    }

    @Override
    public boolean crearJugada(Carta carta1, Carta carta2, Carta carta3) {
        ArrayList<Carta> nuevaJugada;
        if (carta1.numero == carta2.numero && carta2.numero == carta3.numero) {
            nuevaJugada = new ArrayList<>();
            nuevaJugada.add(carta1);
            nuevaJugada.add(carta2);
            nuevaJugada.add(carta3);
            crearTapeteConJugada(nuevaJugada);
        } else if (carta1.palo.equals(carta2.palo) && carta2.palo.equals(carta3.palo)) {
            nuevaJugada = generarPosibleEscalera(carta1, carta2, carta3);
            if (tieneExtremos(carta1,carta2,carta3)){
                //esta comprobacion sirve para acomodar los numeros extremos en la escalera
                // para que se forme correctamente
                acomodarValoresExtremos(nuevaJugada);
            }
            boolean es_escalera = esEscalera(nuevaJugada);
            if (es_escalera) {
                crearTapeteConJugada(nuevaJugada);
            }
        }
        return false;
    }

    @Override
    public boolean tieneExtremos(Carta carta1, Carta carta2, Carta carta3) {
        boolean resultado = false;
        if (carta1.numero == 1 || carta2.numero == 1 || carta3.numero == 1) {
            if (carta1.numero == 13 || carta2.numero == 13 || carta3.numero == 13) {
                resultado = true;
            }
        }
        return resultado;
    }

    @Override
    public boolean comprobarValores(Carta carta1, Carta carta2, Carta carta3, Carta carta4) throws RemoteException{
        //comprueba que no sean nulas las cartas antes de acceder a la accion de listo
        boolean resultado = false;
        if (carta1 != null && carta2 != null && carta3 != null && carta4 == null){
            accionLista(carta1, carta2, carta3, null);
            resultado = true;
        } else if (carta1 != null && carta2 != null && carta3 != null) {
            accionLista(carta1, carta2, carta3, carta4);
            resultado = true;
        }
        return resultado;
    }
    /*private void agregarJugadaATapete(ArrayList<Carta> jugada){

    }*/
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
                //comporbamos si con las cartas agregadas siga siendo una escalera
                if (esEscalera(jugada)){
                    notificarObservadores("cartas agregadas");
                }else{
                    quitarCartasSeleccionadas(jugada, cartasSeleccionadas);
                    devolverCartas(jugadorActual,cartasSeleccionadas);
                    //excepcion
                }
            }else{
                agregarCartasSeleccionadas(cartasSeleccionadas, jugada);
                if (esCombinacion(jugada)){
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
    }

    private void agregarCartasSeleccionadas(ArrayList<Carta> cartasSeleccionadas, ArrayList<Carta> jugada) {
        for (Carta carta: cartasSeleccionadas) {
            agregarCartaOrdenada(jugada, carta);
        }
    }

    private void quitarCartasSeleccionadas(ArrayList<Carta> jugada, ArrayList<Carta> cartasSeleccionadas) {
        System.out.println("llego aca?");
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
        }else {
            mazoDeJuego.agregarCartaBocaArriba(cartaTirada);
            Jugador jugadorSiguiente = buscarJugadorIzquierda(jugadorActual);
            siguienteTurno(jugadorSiguiente);
        }
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
        return jugadorAux;
    }

    @Override
    public void cancelarAccion()throws RemoteException{
        //deberia de avisar al controlador?
    }

    @Override
    public void accionLista(Carta carta1, Carta carta2, Carta carta3, Carta carta4)throws RemoteException{
        /*boolean es_escalera;
        if (carta4 == null){
            es_escalera = comprobarEscalera(carta1, carta2, carta3);
            if (es_escalera){
                crearJugada(carta1, carta2, carta3);
            }
        } else if (carta1 != null && carta2 != null && carta3 != null) {
            //este if sirve para confirmar que se esten agregando de 3 a 4 cartas
            es_escalera = comprobarEscalera(carta1, carta2, carta3, carta4);
            if (es_escalera){
                crearJugada(carta1, carta2, carta3,  carta4);
            }
        }*/
    }


    /*public boolean comprobarEscalera(Carta carta1, Carta carta2, Carta carta3, Carta carta4) {
        boolean resultado = false;
        ArrayList<Carta> nuevaJugada;
        if (carta1.numero == carta2.numero && carta2.numero == carta3.numero && carta3.numero == carta4.numero){
            resultado = true;
        } else if (carta1.palo.equals(carta2.palo) && carta2.palo.equals(carta3.palo) && carta3.palo.equals(carta4.palo)){
            nuevaJugada = generarPosibleEscalera(carta1, carta2, carta3, carta4);
            if (tieneExtremos(carta1,carta2,carta3,carta4)){
                //esta comprobacion sirve para acomodar los numeros extremos en la escalera
                // para que se forme correctamente
                acomodarValoresExtremos(nuevaJugada);
            }
            boolean es_escalera = esEscalera(nuevaJugada);
            if (es_escalera){
                resultado = true;
            }
        }
        return resultado;
    }*/

    @Override
    public boolean crearJugada(Carta carta1, Carta carta2, Carta carta3, Carta carta4) {
        ArrayList<Carta> nuevaJugada;
        if (carta1.numero == carta2.numero && carta2.numero == carta3.numero && carta3.numero == carta4.numero) {
            nuevaJugada = new ArrayList<>();
            nuevaJugada.add(carta1);
            nuevaJugada.add(carta2);
            nuevaJugada.add(carta3);
            nuevaJugada.add(carta4);
            crearTapeteConJugada(nuevaJugada);
        } else if (carta1.palo.equals(carta2.palo) && carta2.palo.equals(carta3.palo) && carta3.palo.equals(carta4.palo)) {
            nuevaJugada = generarPosibleEscalera(carta1, carta2, carta3, carta4);
            if (tieneExtremos(carta1, carta2, carta3, carta4)) {
                //esta comprobacion sirve para acomodar los numeros extremos en la escalera
                // para que se forme correctamente
                acomodarValoresExtremos(nuevaJugada);
            }
            boolean es_escalera = esEscalera(nuevaJugada);
            if (es_escalera) {
                crearTapeteConJugada(nuevaJugada);
            }
        }
        return false;
    }
    @Override
    public boolean tieneExtremos(Carta carta1, Carta carta2, Carta carta3, Carta carta4) {
        boolean resultado = false;
        if (carta1.numero == 1 || carta2.numero == 1 || carta3.numero == 1 || carta4.numero == 1){
            if (carta1.numero == 13 || carta2.numero == 13 || carta3.numero == 13 || carta4.numero == 13){
                resultado = true;
            }
        }
        return resultado;
    }



    @Override
    public ArrayList<Carta> generarPosibleEscalera(Carta carta1, Carta carta2, Carta carta3, Carta carta4) {
        ArrayList<Carta> posibleJugada = new ArrayList<>();
        posibleJugada.add(carta1);
        agregarCartaOrdenada(posibleJugada,carta2);
        agregarCartaOrdenada(posibleJugada, carta3);
        agregarCartaOrdenada(posibleJugada, carta4);
        return posibleJugada;
    }

    @Override
    public void finalizarPartida(String nombreJugador) throws RemoteException {
        Jugador jugadorActual = buscarJugador(nombreJugador);
        if (jugadorActual.getCartasEnMano().isEmpty()){
            notificarObservadores("fin de partida");
            //dialogo de mensaje que avise que la partida fue finalizada y tal vez algun boton de nueva partida
            //mostrar clasificacion y sumar los puntos del jugador a la clasificacion
        }
    }

    @Override
    public void sumarPuntos(){

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
        return jugadores.get(posicionTurnoActual).getNombre();
    }




    /*public boolean esAnfitrion(String nombreJugador) {
        for (int i = 0; i < jugadores.size();i++){
            if (jugadores.get(i).getNombre().equals(nombreJugador)){
                if (jugadores.get(i).get)
            }
        }
    }*/

    /*public void addObserver(Observer o){
        observadores.add(o);
    }*/

    /*private void notificarObservadores(int i){
        for (Observer o : observadores)
            o.notificarCambio(i);
    }*/
}
