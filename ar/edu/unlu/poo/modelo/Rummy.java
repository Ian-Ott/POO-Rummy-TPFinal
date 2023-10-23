package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Rummy extends ObservableRemoto {
    private Mazo mazoDeJuego;
    private ArrayList<Jugador> jugadores;
    private ArrayList<Tapete> tapeteJugadas;
    //private ArrayList<Observer> observadores = new ArrayList<>();

    public Rummy() throws RemoteException {
        jugadores = new ArrayList<>();
        mazoDeJuego = new Mazo();
        if (jugadores.size() == 2){
        repartirCartasJugadores(10);
        } else if (jugadores.size() >= 3) {
            repartirCartasJugadores(7);
        }
        tapeteJugadas = new ArrayList<>();
    }

    private void repartirCartasJugadores(int cantidadCartas) throws RemoteException {
        for (int i = 0; i < jugadores.size(); i++){
            mazoDeJuego.repartir(cantidadCartas, jugadores.get(i));
        }
    }

    private void sacarCartaMazo(Jugador jugador) throws RemoteException{
        mazoDeJuego.sacarCartaMazo(jugador);
    }

    private void agarrarCartaBocaArriba(Jugador jugador) throws RemoteException{
        mazoDeJuego.sacarCartaBocaArriba(jugador);
    }

    private void mezclarMazo() throws RemoteException{
        //cuando el mazo se quede sin cartas se van a agregar de manera random al mazo las cartas que quedaron boca arriba
        mazoDeJuego.mezclarMazo();
    }
    private void crearTapeteConJugada(ArrayList<Carta> jugada){
        Tapete nuevoTapeteConJugada = new Tapete(jugada);
        tapeteJugadas.add(nuevoTapeteConJugada);
    }

    public boolean comprobarJugada(Carta carta1, Carta carta2, Carta carta3) throws RemoteException{
        boolean resultado = false;
        ArrayList<Carta> nuevaJugada;
        if (carta1.numero == carta2.numero && carta2.numero == carta3.numero){
            resultado = true;
        } else if (carta1.palo.equals(carta2.palo) && carta2.palo.equals(carta3.palo)){
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
            }
        }
        return resultado;
    }

    private boolean esEscalera(ArrayList<Carta> nuevaJugada) {
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
    private void acomodarValoresExtremos(ArrayList<Carta> nuevaJugada) {
        Carta cartaAux;
        int posUltimaCarta = nuevaJugada.size() - 1;
        //AVISO: este metodo acomoda tanto para 3 como para 4 cartas
        if (nuevaJugada.get(0).numero == 1){
            if (nuevaJugada.get(posUltimaCarta).numero == 13){
                cartaAux = nuevaJugada.remove(posUltimaCarta);
                nuevaJugada.add(0,cartaAux);
            }
            if (nuevaJugada.get(nuevaJugada.size() - 1).numero == 12){
                cartaAux = nuevaJugada.remove(posUltimaCarta);
                nuevaJugada.add(0,cartaAux);
            }
            if (nuevaJugada.get(nuevaJugada.size() - 1).numero == 11){
                cartaAux = nuevaJugada.remove(posUltimaCarta);
                nuevaJugada.add(0,cartaAux);
            }
        }
        //CASO 3 CARTAS: al estar ordenado de menor a mayor solo hay dos estados posibles con 3 cartas en los que puede quedar desordenada la escalera
        // y este metodo lo acomoda (escalera desordenada caso 1: 1-2-13) (escalera desordenada caso 2: 1-12-13)

        //CASO 4 CARTAS: al estar ordenado de menor a mayor solo hay tres estados posibles con 4 cartas en los que puede quedar desordenada la escaler
        // y este metodo lo acomoda (escalera desordenada caso 1: 1-2-12-13) (escalera desordenada caso 2: 1-2-3-13) (escalera desordenada caso 3: 1-11-12-13)
    }


    private ArrayList<Carta> generarPosibleEscalera(Carta carta1, Carta carta2, Carta carta3) {
        ArrayList<Carta> posibleJugada = new ArrayList<>();
        posibleJugada.add(carta1);
        agregarCartaOrdenada(posibleJugada, carta2);
        agregarCartaOrdenada(posibleJugada, carta3);
        return posibleJugada;
    }

    private void crearJugada(Carta carta1, Carta carta2, Carta carta3) {
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
    }

    private boolean tieneExtremos(Carta carta1, Carta carta2, Carta carta3) {
        boolean resultado = false;
        if (carta1.numero == 1 || carta2.numero == 1 || carta3.numero == 1) {
            if (carta1.numero == 13 || carta2.numero == 13 || carta3.numero == 13) {
                resultado = true;
            }
        }
        return resultado;
    }

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

    private void agregarCartaOrdenada(ArrayList<Carta> jugada, Carta cartaElegida) {
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

    private ArrayList<Carta> buscarJugada(Carta cartaDeLaJugada) {
        Tapete tapeteAux;
        Carta cartaAux;
        for (int i = 0; i < tapeteJugadas.size(); i++){
            tapeteAux = tapeteJugadas.get(i);
            for (int j = 0; j < tapeteAux.getJugada().size(); j++){
                cartaAux = tapeteAux.getJugada().get(j);
                if (cartaAux.equals(cartaDeLaJugada)){
                    return tapeteAux.getJugada();
                }
            }
        }
        return null;
    }

    public void terminarTurno(Carta carta1)throws RemoteException{
        mazoDeJuego.agregarCartaBocaArriba(carta1);
        //deberia de avisar al controlador?
    }

    public void cancelarAccion()throws RemoteException{
        //deberia de avisar al controlador?
    }

    public void accionLista(Carta carta1, Carta carta2, Carta carta3, Carta carta4)throws RemoteException{
        boolean es_escalera;
        if (carta4 == null){
            es_escalera = comprobarJugada(carta1, carta2, carta3);
            if (es_escalera){
                crearJugada(carta1, carta2, carta3);
            }
        } else if (carta1 != null && carta2 != null && carta3 != null) {
            //este if sirve para confirmar que se esten agregando de 3 a 4 cartas
            es_escalera = comprobarJugada(carta1, carta2, carta3, carta4);
            if (es_escalera){
                crearJugada(carta1, carta2, carta3,  carta4);
            }
        }
    }

    private boolean comprobarJugada(Carta carta1, Carta carta2, Carta carta3, Carta carta4) {
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
    }

    private void crearJugada(Carta carta1, Carta carta2, Carta carta3, Carta carta4) {
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
    }
    private boolean tieneExtremos(Carta carta1, Carta carta2, Carta carta3, Carta carta4) {
        boolean resultado = false;
        if (carta1.numero == 1 || carta2.numero == 1 || carta3.numero == 1 || carta4.numero == 1){
            if (carta1.numero == 13 || carta2.numero == 13 || carta3.numero == 13 || carta4.numero == 13){
                resultado = true;
            }
        }
        return resultado;
    }



    private ArrayList<Carta> generarPosibleEscalera(Carta carta1, Carta carta2, Carta carta3, Carta carta4) {
        ArrayList<Carta> posibleJugada = new ArrayList<>();
        posibleJugada.add(carta1);
        agregarCartaOrdenada(posibleJugada,carta2);
        agregarCartaOrdenada(posibleJugada, carta3);
        agregarCartaOrdenada(posibleJugada, carta4);
        return posibleJugada;
    }

    public void finalizarPartida(Jugador jugador){
        if (jugador.getCartasEnMano().isEmpty()){
            //dialogo de mensaje que avise que la partida fue finalizada y tal vez algun boton de nueva partida
            //mostrar clasificacion y sumar los puntos del jugador a la clasificacion
        }
    }

    public void sumarPuntos(){

    }

    public void agregarPuntosClasificacion(){

    }

    public void siguienteTurno(Jugador jugadorIzquierda){

    }



    /*public void addObserver(Observer o){
        observadores.add(o);
    }*/

    /*private void notificarObservadores(int i){
        for (Observer o : observadores)
            o.notificarCambio(i);
    }*/
}
