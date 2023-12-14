package ar.edu.unlu.poo.controlador;

import ar.edu.unlu.poo.Serializacion.services.Serializador;
import ar.edu.unlu.poo.exceptions.JugadorInexistente;
import ar.edu.unlu.poo.modelo.*;
import ar.edu.unlu.poo.vistas.IVista;
import ar.edu.unlu.poo.vistas.VistaConsola;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Controlador implements IControladorRemoto {
    private IRummy rummy;
    private IVista vista;
    private String nombreJugador;
    private boolean anfitrion;

    private static Serializador serializador;

    public Controlador(IVista vista){
        this.vista = vista;
    }


    /*public void agregameComoObservador(Observer observador){
        rummy.addObserver(observador);
    }*/

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto){
        this.rummy = (IRummy) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto modelo, Object cambio){
            if (cambio.toString().equals("cartas repartidas")){
                obtenerCartas();
            } else if (cambio.equals("nueva apuesta")) {
                vista.avisarSobreApuesta();
            } else if (cambio.equals("apuesta cancelada")) {
                vista.mostrarErrorApuesta();
            } else if (cambio.equals("nuevo jugador")) {
                vista.actualizarCantJugadores();
            } else if (cambio.equals("jugador eliminado")) {
                vista.actualizarCantJugadores();
            } else if (cambio.equals("jugador sacado del juego")) {
                vista.mostrarJugadorSalioDelJuego();
            } else if (cambio.equals("cartas agregadas")) {
                vista.actualizarJugadas();
                vista.continuarTurnoActual();
            } else if (cambio.equals("nuevo turno")){
                vista.nuevoTurno();
            } else if (cambio.equals("continuar turno jugador")) {
                vista.continuarTurnoActual();
            } else if (cambio.equals("jugada agregada")) {
                vista.actualizarJugadas();
            } else if (cambio.equals("fin de partida")) {
                vista.finalizarPartida();
            } else if (cambio.equals("partida cerrada")) {
                vista.cerrarPartida();
            } else if (cambio.equals("partida cerrada modo expres")) {
                vista.cerrarPartida();
                vista.finalizarPartida();
            } else if (cambio.equals("pedido anular partida")) {
                vista.eleccionAnularPartida();
            } else if (cambio.equals("finalizo partida amistosamente")) {
                vista.finalizarPartidaAmistosamente();
            } else if (cambio.equals("fin ronda")) {
                resultadoRonda();
                //vista.pantallaEspera();
            } else if (cambio.equals("nuevo juego")) {
                vista.pantallaEspera();
            } else if (cambio.equals("cerrar juego")) {
                vista.cerrarJuego();
            } else if (cambio.equals("continuar espera")) {
                vista.pantallaEspera();
            } else if (cambio.equals("cambios en opciones de mesa")) {
                vista.avisarCambiosOpcionesMesa();
            }
    }

    private void resultadoRonda() {
        ArrayList<IJugador> jugadores;
        try {
            jugadores =  rummy.getIJugadores();
            vista.mostrarResultadosPuntosRonda(jugadores);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public boolean esAnfitrion() {
        return anfitrion;
    }

    public void setAnfitrion(boolean anfitrion) {
        this.anfitrion = anfitrion;
    }

    public ArrayList<ICarta> obtenerCartas(){
        ArrayList<Carta> cartasJugadorAux = null;
        //aunque se inicialice con null nunca se va a devolver null
        try {
            cartasJugadorAux = rummy.getCartasJugador(nombreJugador);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
            cartasJugadorAux = new ArrayList<>();
        }
        ArrayList<ICarta> cartasJugador = cambiarTipoCartas(cartasJugadorAux);
        return  cartasJugador;
    }

    private ArrayList<ICarta> cambiarTipoCartas(ArrayList<Carta> cartasJugadorAux) {
        //pasa a crear un arrayList conteniendo las cartas que recibe pero pasadas a Icarta
        return new ArrayList<>(cartasJugadorAux);
    }

    public int getCartasSize(){
        try {
            return rummy.getCartasJugador(nombreJugador).size();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return 0;
    }

    public ICarta getCartaDescarte(){
        try {
            return rummy.getCartaBocaArriba();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
            //tirar excepcion?
        }
        return null;
    }


    public boolean juegoIniciado(){
        try {
            return rummy.isJuegoIniciado();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return false;
    }

    public boolean iniciarJuego(){
        boolean resultado = false;
        try {
            if (rummy.getJugadores().size() >= 2){
                rummy.iniciarJuego();
                resultado = true;
            }else {
                /*if (vista instanceof VistaConsola){
                    JOptionPane.showMessageDialog(null,"ERROR: faltan jugadores para iniciar el juego");
                }*/
                vista.errorCantidadJugadores();
            }
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return resultado;
    }

    public void nuevoJugador(boolean anfitrion, String nombreJugador){
        Jugador nuevoJugador = new Jugador(nombreJugador);
        this.nombreJugador = nombreJugador;
        try {
            rummy.agregarJugador(nuevoJugador, anfitrion);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }


    public boolean estaEnElJuego(String nombreJugador){
        boolean resultado = false;
        ArrayList<String> nombres = null;
        try {
            nombres = rummy.getNombreJugadores();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
            nombres = new ArrayList<>();
        }
        if (!nombres.isEmpty()){
            for (int i = 0; i < nombres.size(); i++) {
                if (nombres.get(i).equals(nombreJugador)){
                    resultado = true;
                }
            }
        }
        return resultado;
    }

    public boolean primerJugador()  {
        boolean resultado = false;
        try {
            if (rummy.getJugadores().isEmpty()){
                resultado = true;
            }
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return resultado;
    }

    public int cantJugadores()  {
        try {
            return rummy.getJugadoresSize();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return 0;
    }

    public ArrayList<String> nombreOponentes(String nombreJugador) {
        try{
            return rummy.getNombreOponentes(nombreJugador);
        }catch(RemoteException e){
            vista.mostrarErrorConexion();
            return new ArrayList<>();
        }
    }

    public void terminarTurno(ArrayList<Integer> posicionesSeleccionadas){
        try {
            if (posicionesSeleccionadas.size() == 1){
                rummy.terminarTurno(posicionesSeleccionadas.get(0),nombreJugador);

            } else if (posicionesSeleccionadas.isEmpty()) {
                rummy.terminarTurno(-1,nombreJugador);
            }
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public boolean esTurnoJugador(){
        boolean resultado = false;
        try {
            if (rummy.getNombreTurnoActual().equals(nombreJugador)){
                 resultado = true;
            }
        } catch (RemoteException e) {
           vista.mostrarErrorConexion();
        }
        return resultado;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void tomarCartaMazo(){
        try {
            rummy.sacarCartaMazo(nombreJugador);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }


    public void tomarCartaDescarte(){
        try {
            rummy.agarrarCartaBocaArriba(nombreJugador);
        } catch (RemoteException e) {
           vista.mostrarErrorConexion();
        }
    }

    public void armarRummy(ArrayList<Integer> posicionesSeleccionadas){
        try {
            rummy.comprobarRummy(posicionesSeleccionadas, nombreJugador);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public void armarEscalera(ArrayList<Integer> posicionesSeleccionadas){
        try {
            if (posicionesSeleccionadas.size() >= 3 && posicionesSeleccionadas.size() <= getCartasSize()){
                rummy.comprobarEscalera(posicionesSeleccionadas, nombreJugador);
            }else {
                vista.continuarTurnoActual();
            }
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public void armarCombinacionIguales(ArrayList<Integer> posicionesSeleccionadas){
        try {
            if (posicionesSeleccionadas.size() >= 3 && posicionesSeleccionadas.size() <= getCartasSize()){
                rummy.comprobarCombinacion(posicionesSeleccionadas,nombreJugador);
            }else {
                //agregar excepcion cartas insuficientes
                vista.continuarTurnoActual();
                System.out.println("error cantidad incorrecta de posiciones");
            }
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public void agregarCartasAJugada(ArrayList<Integer> posicionesSeleccionadas, int posicionJugada){
        try {
            rummy.agregarCartaAJugada(posicionesSeleccionadas, posicionJugada, nombreJugador);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public ITapete obtenerJugadas(){
        try {
            return rummy.getMesaJugadas();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();

        }
        //arreglar
        return null;
    }

    public int cantCartasOponente(String oponente){
        try {
            return rummy.getCantCartasOponente(oponente);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return 0;
    }

    public void activarModoExpres(){
        try {
            rummy.modoExpres();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public int getJugadasSize()  {
        try {
            return rummy.getMesaJugadas().getJugada().size();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return 0;
    }

    public void activarModoPuntos() {
        try {
            rummy.modoPuntos();
        } catch (RemoteException e) {
           vista.mostrarErrorConexion();
        }
    }

    public int cantFichas() {
        try {
            return rummy.getCantidadFichas(nombreJugador);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return 0;
    }

    public void apostar(int apuesta) {
        try {
            if (rummy.puedenApostarJugadores(apuesta)){
                rummy.apostarFichas(apuesta);
            }else {
                vista.mostrarErrorApuesta();
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int getcantidadApostada() {
        try {
            return rummy.getCantApostada(nombreJugador);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return 0;
    }

    public void cancelarApuesta() {
        try {
            rummy.cancelarApuestas();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public boolean apuestasActivadas() {
        try {
            return rummy.isApuestasActivas();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return false;
    }

    public void restarFichas() {
        try {
            rummy.apostarFichasJugador(nombreJugador);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public int getcantidadFichasBote() {
        try {
            return rummy.getCantidadTotalApuesta();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return 0;
    }

    public String getGanador() {
        try {
            return rummy.getNombreGanador();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return "";
    }

    public int getCantidadPuntosGanador() {
        try {
            return rummy.getPuntosGanador();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return 0;
    }

    public void iniciarNuevaRonda() {
        try {
            rummy.iniciarNuevaRonda();
        } catch (RemoteException e) {
           vista.mostrarErrorConexion();
        }
    }

    public String getModoJuego() {
        try {
            return rummy.getModoActual();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return "";
    }

    public void solicitarAnularPartida() {
        try {
            rummy.pedidoAnularPartidaAmistosamente();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public void tomarDecisionDePartida(String eleccion) {
        try {
            if (eleccion.equals("Y")){
                rummy.anularPartida(true);
            }else {
                rummy.anularPartida(false);
            }
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public String obtenerJugador(int posicion) {
        try {
            return rummy.getJugador(posicion);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return "";
    }

    public void hacerReenganche() {
        try {
            rummy.reengancharJugador(nombreJugador);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public boolean isEliminado() {
        try {
            return rummy.estaEliminado(nombreJugador);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return false;
    }

    public int getCantDisponibles() {
        try {
            return (rummy.getJugadoresSize() - rummy.contarEliminados());
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return 0;
    }

    public void nuevoJuego() {
        try {
            rummy.nuevoJuego();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public void obtenerPosiciones() {
        ArrayList<IJugador> jugadores = new ArrayList<>();
        serializador = new Serializador("top5.dat");
        /*if (serializador.readFirstObject() == null){
            serializador.writeOneObject()
        }*/
        Object[] recuperado = serializador.readObjects();
        if (recuperado != null){
            for (int i = 0; i < recuperado.length; i++) {
                jugadores.add((IJugador) recuperado[i]);
            }
        }
        try {
            jugadores = rummy.obtenerJugadoresPorPuntos(jugadores);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
            //verificar si esto esta bien
        }
        vista.mostrarTablaPosiciones(jugadores);
        serializador.writeOneObject(jugadores.get(0));
        for (int i = 1; i < jugadores.size(); i++) {
            serializador.addOneObject(jugadores.get(i));
        }
    }

    public void eliminarJugador() {
        try {
            if (nombreJugador != null) {
                rummy.removerObservador(this);
                rummy.eliminarJugador(nombreJugador);
            }else {
                rummy.removerObservador(this);
            }
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public void comprobarAnfitrion() {
        try {
            if (rummy.isJefeMesa(nombreJugador)){
                anfitrion = true;
            }
        } catch (RemoteException | JugadorInexistente e) {
            if (e instanceof JugadorInexistente){
                System.exit(0);
            }else {
                vista.mostrarErrorConexion();
            }
        }
    }

    public void modificarOpcionChat() {

    }

    public void modificarPartidasCompetitivas() {
        try {
            rummy.modificarCompetitivo();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public boolean getEstadoCompetitivo() {
        try {
            return rummy.getEstadoCompetitivo();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return false;
    }

    public void setTiempoTurno(int tiempoEstablecido) {
        try {
            rummy.cambiarTiempoTurno(tiempoEstablecido);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public int getTiempoPorTurno(){
        try {
            return rummy.getCantidadTiempoTurno();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return 0;
    }

    public String getNombreAnfitrion() {
        try {
            return rummy.getNombreJefeMesa();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return "";
    }

    public String getNombreTurnoActual() {
        try {
            return rummy.getNombreTurnoActual();
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return "";
    }

    public int getpuntosJugador() {
        return 0;//escribir
    }

    public void iniciarJuegoAutomatico() {
        try {
            rummy.juegoAutomatico(nombreJugador);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }

    public boolean jugadorEnAutomatico() {
        try {
            return rummy.isJugadorEnAutomatico(nombreJugador);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
        return false;
    }

    public void desactivarJuegoAutomatico() {
        try {
            rummy.desactivarJuegoAutomatico(nombreJugador);
        } catch (RemoteException e) {
            vista.mostrarErrorConexion();
        }
    }
}
