package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

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
    public void comprobarRummy(ArrayList<Integer> posicionesSeleccionadas, Jugador jugador) throws RemoteException {

    }


    @Override
    public void comprobarRummy(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador)throws RemoteException {
        Jugador jugadorActual = buscarJugador(nombreJugador);
        if (!tieneJugada(jugadorActual)) {
            if (posicionesSeleccionadas.size() == jugadorActual.getCartasEnMano().size()) {
                ArrayList<Carta> cartasAux = new ArrayList<>();
                obtenerCartasOrdenadas(cartasAux, jugadorActual);
                if (EsRummy(cartasAux)) {
                    agregarJugada(nombreJugador, cartasAux);
                }
            } else {
                //excepcion de que no selecciono todas las cartas
            }
        }
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

    private boolean EsRummy(ArrayList<Carta> cartasAux) {
        //comprobar que el jugador no haya hecho una jugada antes y luego verificar si hace escalera con todas las cartas
        //parece que pueden ser escaleras o combinaciones
        return false;
    }

    private void obtenerCartasOrdenadas(ArrayList<Carta> cartasAux, Jugador jugadorActual) {
        for (int i = 0; i < jugadorActual.getCartasEnMano().size();i++) {
            //le saca todas las cartas al jugador para que no queden duplicadas y si ocurre un error luego se devuelven
            agregarCartaOrdenada(cartasAux,jugadorActual.getCartasEnMano().remove(i));
        }
    }

    private ArrayList<Carta> obtenerCartasPorPosicion(ArrayList<Integer> posicionesSeleccionadas, Jugador jugadorActual) {
        ArrayList<Carta> cartas = new ArrayList<>();
        for (Integer posicion:posicionesSeleccionadas) {
            //le saco la carta al jugador para que no quede duplicada
            agregarCartaOrdenada(cartas,jugadorActual.getCartasEnMano().remove((int)posicion));
        }
        return cartas;
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
    public boolean comprobarEscalera(ArrayList<Integer> posicionesSeleccionadas, String nombreJugador)throws RemoteException{
        boolean resultado = false;
        Jugador jugadorActual = buscarJugador(nombreJugador);
        if (posicionesSeleccionadas.size() <= jugadorActual.getCartasEnMano().size() && posicionesSeleccionadas.size() >= 3) {
            ArrayList<Carta> cartasAux = obtenerCartasPorPosicion(posicionesSeleccionadas, jugadorActual);
            acomodarValoresExtremos(cartasAux);
            if (esEscalera(cartasAux)){
                resultado = true;
                agregarJugada(nombreJugador, cartasAux);
                notificarObservadores("jugada agregada");
            }else {
                devolverCartas(jugadorActual, cartasAux);
                //excepcion?
            }
            //solo acomoda los valores en el caso de que esten el as al principio y la k al final
        }else {
            //excepcion de que no selecciono todas las cartas
        }
        /*if (carta1.numero == carta2.numero && carta2.numero == carta3.numero){
            resultado = true;
        } else */    /*if (carta1.palo.equals(carta2.palo) && carta2.palo.equals(carta3.palo)){
            nuevaJugada = generarPosibleEscalera(carta1, carta2, carta3);
            if (carta1.numero == 1 || carta2.numero == 1 || carta3.numero == 1){
                if (carta1.numero == 13 || carta2.numero == 13 || carta3.numero == 13){
                    //esta comprobacion sirve para acomodar los numeros extremos en la escalera
                    // para que se forme correctamente
                    acomodarValoresExtremos(nuevaJugada);
                }
            }
            boolean es_escalera = esEscalera(nuevaJugada);
            if (es_escalera){
                resultado = true;
            }*/

        return resultado;
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
            if (nuevaJugada.get(i + 1) != null) {
                if (nuevaJugada.get(i).numero == 13 && nuevaJugada.get(i + 1).numero != 1) {
                    resultado = false;
                    //compruebo que el siguiente a la k en la escalera sea el As
                } else if ((nuevaJugada.get(i).numero + 1) != nuevaJugada.get(i).numero) {
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
        int anterior;
        int posUltimaCarta = nuevaJugada.size() - 1;
        if (nuevaJugada.get(0).numero == 1){
            if (nuevaJugada.get(posUltimaCarta).numero == 13) {
                for (int i = 0; i < nuevaJugada.size(); i++) {
                    //si el numero anterior sumado 1 no es igual al siguiente
                    // es posible que sea el inicio de la escalera
                    siguiente = nuevaJugada.get(i + 1).numero;
                    anterior = (nuevaJugada.get(i).numero + 1);
                    if (siguiente > anterior) {
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
    public void agregarCartaAJugada(Carta cartaElegida, Carta cartaDeLaJugada){
        boolean mismoPalo = true;
        boolean mismoNumero = true;
        ArrayList<Carta> jugada = buscarJugada(cartaDeLaJugada);
        if (jugada != null) {
            //primero se comprueba si se quiere agregar la carta a una jugada del mismo numero o del mismo palo
            for (int i = 0; i < jugada.size(); i++) {
                    if (cartaElegida.numero != jugada.get(i).numero){
                        mismoNumero = false;
                    }
                    if (!cartaElegida.palo.equals(jugada.get(i).palo)){
                        mismoPalo = false;
                    }
            }
            if (mismoNumero){
                jugada.add(cartaElegida);
            } else if (mismoPalo) {
                agregarCartaOrdenada(jugada, cartaElegida);
                if (cartaElegida.numero == 13 || cartaElegida.numero == 1){
                    acomodarValoresExtremos(jugada);
                    //sino poner dos condiciones que acomoden la cartaElegida
                }
                if (!esEscalera(jugada)){
                    jugada.remove(cartaElegida);
                    //mensaje de error o señal
                }
            }
        }
    }

    @Override
    public void agregarCartaOrdenada(ArrayList<Carta> jugada, Carta cartaElegida) {
        boolean agregada = false;
        for (int i = 0; i < jugada.size(); i++){
            if (cartaElegida.numero < jugada.get(i).numero){
                jugada.add(i, cartaElegida);
            }
        }
        if (!agregada){
            jugada.add(cartaElegida);
        }
    }

    @Override
    public ArrayList<Carta> buscarJugada(Carta cartaDeLaJugada) {
        Tapete tapeteAux;
        Carta cartaAux;
        /*for (int i = 0; i < tapeteJugadas.size(); i++){
            tapeteAux = tapeteJugadas.get(i);
            for (int j = 0; j < tapeteAux.getJugada().size(); j++){
                cartaAux = tapeteAux.getJugada().get(j);
                if (cartaAux.equals(cartaDeLaJugada)){
                    return tapeteAux.getJugada();
                }
            }
        }*/ //este metodo sera cambiado y las jugadas seran buscadas por posicion
        return null;
    }

    @Override
    public void terminarTurno(Carta carta1, String jugadorActual)throws RemoteException{
        Jugador jugadorAux = buscarJugador(jugadorActual);
        if (jugadorAux.getCartasEnMano().isEmpty()){
            finalizarPartida(jugadorAux);
        }else {
            jugadorAux.tirarCarta(carta1.getPalo(), carta1.getNumero());
            mazoDeJuego.agregarCartaBocaArriba(carta1);
            Jugador jugadorSiguiente = buscarJugadorIzquierda(jugadorAux);
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
    public void finalizarPartida(Jugador jugador){
        if (jugador.getCartasEnMano().isEmpty()){
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
