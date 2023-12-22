package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.exceptions.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Tapete implements Serializable, ITapete {
    private ArrayList<Jugada> listaJugada;

    private int boteApuesta;

    public Tapete(){
        listaJugada = new ArrayList<>();
        boteApuesta = 0;
    }

    public void agregarApuesta(int apuesta) {
        this.boteApuesta += apuesta;
    }

    public void sacarApuesta(int apuesta) {
        this.boteApuesta -= apuesta;
    }

    public int getBoteApuesta() {
        return boteApuesta;
    }

    public int otorgarFichasAlGanador(){
        //le otorga al jugador el 80% de las fichas en el bote
        // y el resto se mantiene en el bote para partidas siguientes
        int fichasGanadas = (int) (boteApuesta * 0.8);
        boteApuesta = (int) (boteApuesta * 0.2);
        return fichasGanadas;
    }

    @Override
    public void agregarJugada(Jugada jugada) {
        this.listaJugada.add(jugada);
    }

    @Override
    public ArrayList<Jugada> getListaJugada() {
        return listaJugada;
    }

    public void verificarRummy(ArrayList<Carta> cartasSeleccionadas, Jugador jugadorActual) throws JugadorHizoRummy, NoEsJugada, NoPuedeHacerRummy, FaltanCartas {
        if (!tieneJugada(jugadorActual) && !agregoJugada(jugadorActual)) {
                if (esRummy(cartasSeleccionadas)) {
                    jugadorActual.setHizoRummy(true);
                    agregarNuevaJugada(jugadorActual.getNombre(), cartasSeleccionadas, false);
                    throw new JugadorHizoRummy();
                }else {
                    devolverCartas(jugadorActual,cartasSeleccionadas);
                    throw new NoEsJugada();
                }
        }else {
            throw new NoPuedeHacerRummy();
        }
    }

    public void verificarEscalera(ArrayList<Carta> cartasSeleccionadas, Jugador jugadorActual) throws EsJugadaValida, NoEsJugada, FaltanCartas {
        boolean estaLLena = false;
            //solo acomoda los valores en el caso de que esten el as al principio y la k al final
            acomodarValoresExtremos(cartasSeleccionadas);
            if (esEscalera(cartasSeleccionadas)){
                if (cartasSeleccionadas.size() == 13){
                    estaLLena = true;
                }
                agregarNuevaJugada(jugadorActual.getNombre(), cartasSeleccionadas, estaLLena);
                throw new EsJugadaValida();
            }else {
                devolverCartas(jugadorActual, cartasSeleccionadas);
                throw new NoEsJugada();
            }
    }

    public void verificarCombinacion(ArrayList<Carta> cartasSeleccionadas, Jugador jugadorActual) throws EsJugadaValida, NoEsJugada {
        boolean estaLLena = false;
        if (esCombinacion(cartasSeleccionadas)){
            if (cartasSeleccionadas.size() == 4){
                estaLLena = true;
            }
            agregarNuevaJugada(jugadorActual.getNombre(), cartasSeleccionadas, estaLLena);
            throw new EsJugadaValida();
        }else {
            devolverCartas(jugadorActual, cartasSeleccionadas);
            throw new NoEsJugada();
        }
    }

    public void verificarCartaParaJugada(ArrayList<Carta> cartasSeleccionadas, int posicionJugada, Jugador jugadorActual) throws RemoteException, CartasAgregadasAJugada, NoSeAgregaronAJugada, JugadaLLena {
        ArrayList<Carta> jugada = buscarJugada(posicionJugada);
        if (jugada != null) {
            //primero se comprueba si se quiere agregar la carta a una jugada del mismo numero o del mismo palo
            if (esEscalera(jugada)){
                if (jugada.size() != 13) {
                    agregarCartasSeleccionadas(cartasSeleccionadas, jugada);
                    ArrayList<Carta> jugadaOrdenada = ordenarCartasFormaAscendente(jugada);
                    acomodarValoresExtremos(jugadaOrdenada);
                    //comprobamos si con las cartas agregadas sigue siendo una escalera
                    if (esEscalera(jugadaOrdenada)) {
                        listaJugada.remove(posicionJugada);
                        Jugada jugadaActualizada = new Jugada();
                        jugadaActualizada.setCartasJugada(jugadaOrdenada);
                        jugadaActualizada.setNombreCreadorJugada(jugadorActual.getNombre());
                        if (jugadaOrdenada.size() == 13) {
                            jugadaActualizada.setJugadaLlena(true);
                        }
                        listaJugada.add(posicionJugada, jugadaActualizada);
                        jugadorActual.setAgregoJugada(true);
                        throw new CartasAgregadasAJugada();
                    } else {
                        quitarCartasSeleccionadas(jugada, cartasSeleccionadas);
                        devolverCartas(jugadorActual, cartasSeleccionadas);
                        throw new NoSeAgregaronAJugada();
                    }
                }else {
                    throw new JugadaLLena();
                }
            }else {
                if (jugada.size() != 4) {
                    agregarCartasSeleccionadas(cartasSeleccionadas, jugada);
                    if (esCombinacion(jugada)) {
                        if (jugada.size() == 4) {
                            listaJugada.get(posicionJugada).setJugadaLlena(true);
                        }
                        jugadorActual.setAgregoJugada(true);
                        throw new CartasAgregadasAJugada();
                    } else {
                        quitarCartasSeleccionadas(jugada, cartasSeleccionadas);
                        devolverCartas(jugadorActual, cartasSeleccionadas);
                        throw new NoSeAgregaronAJugada();
                    }
                }else {
                    throw new JugadaLLena();
                }
            }
        }else{
            System.out.println("no existe la jugada");
            //podria ser una excepcion pero nunca llega a esto
        }
    }

    private ArrayList<Carta> ordenarCartasFormaAscendente(ArrayList<Carta> jugada) {
        ArrayList<Carta> cartasOrdenadas = new ArrayList<>();
        for (Carta carta: jugada) {
            System.out.println("ordenada");
            agregarCartaOrdenada(cartasOrdenadas, carta);
        }
        return cartasOrdenadas;
    }

    public void quitarCartasSeleccionadas(ArrayList<Carta> cartasActuales, ArrayList<Carta> cartasSeleccionadas) {
        for (Carta carta:cartasSeleccionadas) {
            cartasActuales.remove(carta);
        }
    }
    private void agregarCartasSeleccionadas(ArrayList<Carta> cartasSeleccionadas, ArrayList<Carta> jugada) {
        for (Carta carta: cartasSeleccionadas) {
            agregarCartaOrdenada(jugada, carta);
        }
    }
    public ArrayList<Carta> buscarJugada(int posicionDeLaJugada){
        return listaJugada.get(posicionDeLaJugada).getCartasJugada();
    }

    public boolean esCombinacion(ArrayList<Carta> cartasSeleccionadas) {
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
    public void devolverCartas(Jugador jugadorActual, ArrayList<Carta> cartasAux) {
        while (!cartasAux.isEmpty()){
            jugadorActual.devolverCarta(cartasAux.remove(0));
        }
    }

    public void agregarNuevaJugada(String nombreJugador, ArrayList<Carta> cartasJugada, boolean esLlena) {
        Jugada nuevaJugada = new Jugada();
        nuevaJugada.setJugadaLlena(esLlena);
        nuevaJugada.setNombreCreadorJugada(nombreJugador);
        nuevaJugada.setCartasJugada(cartasJugada);
        agregarJugada(nuevaJugada);
    }

    public boolean esRummy(ArrayList<Carta> cartasAux) {
        //solo se verifica que sea una escalera porque el rummy debe ser una jugada unica
        // y es imposible hacer una unica jugada con solo numeros iguales
        return esEscalera(cartasAux);

    }
    public boolean esEscalera(ArrayList<Carta> nuevaJugada) {
        boolean resultado = true;
        Palo paloActual = null;
        for (int i = 0; i < nuevaJugada.size(); i++) {
            if (i == 0){
                paloActual = nuevaJugada.get(i).getPalo();
            }
            if ((i +1) != nuevaJugada.size()) {
                if (!paloActual.equals(nuevaJugada.get(i + 1).getPalo())) {
                    resultado = false;
                    System.out.println("no es del mismo palo");
                }else if (nuevaJugada.get(i).numero == 13 && nuevaJugada.get(i + 1).numero == 1){

                }else if (nuevaJugada.get(i).numero == 13 && nuevaJugada.get(i + 1).numero != 1) {
                    resultado = false;
                    System.out.println("no es escalera extremos");
                    //compruebo que el siguiente a la k en la escalera sea el As
                } else if ((nuevaJugada.get(i).numero + 1) != nuevaJugada.get(i + 1).numero) {
                    //compruebo que el numero actual sumado 1 sea igual al numero siguiente
                    //(ejemplo: carta1 = 7 y carta2 = 8 entonces si carta1 + 1 valor es igual a carta 2 se confirma que esta en escalera)
                    resultado = false;
                    System.out.println("no es escalera");
                }
            }
        }
        return resultado;
    }


    private boolean tieneJugada(Jugador jugadorActual) {
        boolean resultado = false;
        for (Jugada jugada: listaJugada) {
            if (jugada.getNombreCreadorJugada().equals(jugadorActual.getNombre())){
                resultado = true;
            }
        }
        return resultado;
    }

    private boolean agregoJugada(Jugador jugadorActual) {
        return jugadorActual.getAgregoJugada();
    }
    @Override
    public String toString() {
        String acumulador = "\n";
        if (!listaJugada.isEmpty()){
            for (int i = 0; i < listaJugada.size(); i++) {
                acumulador += "\nJugada " + (i+1) + ":\n" + listaJugada.get(i);
            }
        }else {acumulador += "No hay jugadas en la mesa";}
        return acumulador;
    }
}
