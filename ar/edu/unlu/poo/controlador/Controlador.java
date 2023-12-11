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
        try{
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void resultadoRonda() {
        ArrayList<IJugador> jugadores;
        try {
            jugadores =  rummy.getIJugadores();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        vista.mostrarResultadosPuntosRonda(jugadores);
    }

    public boolean esAnfitrion() {
        return anfitrion;
    }

    public void setAnfitrion(boolean anfitrion) {
        this.anfitrion = anfitrion;
    }

    public ArrayList<ICarta> obtenerCartas(){
        ArrayList<Carta> cartasJugadorAux = null;
        try {
            cartasJugadorAux = rummy.getCartasJugador(nombreJugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ArrayList<ICarta> cartasJugador = cambiarTipoCartas(cartasJugadorAux);
        if (vista instanceof VistaConsola){
            vista.actualizarCartas(cartasJugador);
        }
        return  cartasJugador;
        //comprobar si esto es necesario
    }

    private ArrayList<ICarta> cambiarTipoCartas(ArrayList<Carta> cartasJugadorAux) {
        //pasa a crear un arrayList conteniendo las cartas que recibe pero pasadas a Icarta
        return new ArrayList<>(cartasJugadorAux);
    }

    public int getCartasSize(){
        try {
            return rummy.getCartasJugador(nombreJugador).size();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public ICarta getCartaDescarte(){
        try {
            return rummy.getCartaBocaArriba();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean juegoIniciado(){
        try {
            return rummy.isJuegoIniciado();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
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
            throw new RuntimeException(e);
        }
        return resultado;
    }

    public void nuevoJugador(boolean anfitrion, String nombreJugador){
        Jugador nuevoJugador = new Jugador(nombreJugador);
        this.nombreJugador = nombreJugador;
        try {
            rummy.agregarJugador(nuevoJugador, anfitrion);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    //para la ventanaGrafica
    /*private String obtenerNombre(){
        boolean nombreRepetido = true;
        String nombre = "";
        while(nombreRepetido){
            nombre = (String) JOptionPane.showInputDialog("Seleccione su nombre de usuario");
            if (estaEnElJuego(nombre)){
                JOptionPane.showMessageDialog(null, "Error: hay alguien con ese nombre en la partida!!!");
            }else {
                nombreRepetido = false;
            }
        }
        return nombre;
    }*/

    public boolean estaEnElJuego(String nombreJugador){
        boolean resultado = false;
        ArrayList<String> nombres = null;
        try {
            nombres = rummy.getNombreJugadores();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
        return resultado;
    }

    public int cantJugadores()  {
        try {
            return rummy.getJugadoresSize();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> nombreOponentes(String nombreJugador) {
        try{
            return rummy.getNombreOponentes(nombreJugador);
        }catch(RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    public void terminarTurno(ArrayList<Integer> posicionesSeleccionadas){
        try {
            if (posicionesSeleccionadas.size() == 1){
                rummy.terminarTurno(posicionesSeleccionadas.get(0),nombreJugador);

            } else if (posicionesSeleccionadas.isEmpty()) {
                rummy.terminarTurno(-1,nombreJugador);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean esTurnoJugador(){
        boolean resultado = false;
        try {
            if (rummy.getNombreTurnoActual().equals(nombreJugador)){
                 resultado = true;
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }


    public void tomarCartaDescarte(){
        try {
            rummy.agarrarCartaBocaArriba(nombreJugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void armarRummy(ArrayList<Integer> posicionesSeleccionadas){
        try {
            rummy.comprobarRummy(posicionesSeleccionadas, nombreJugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    public void armarCombinacionIguales(ArrayList<Integer> posicionesSeleccionadas){
        try {
            if (posicionesSeleccionadas.size() >= 3 && posicionesSeleccionadas.size() <= getCartasSize()){
                rummy.comprobarCombinacion(posicionesSeleccionadas,nombreJugador);
            }else {
                //falta excepcion
                vista.continuarTurnoActual();
                System.out.println("error cantidad incorrecta de posiciones");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void agregarCartasAJugada(ArrayList<Integer> posicionesSeleccionadas, int posicionJugada){
        try {
            rummy.agregarCartaAJugada(posicionesSeleccionadas, posicionJugada, nombreJugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public ITapete obtenerJugadas(){
        try {
            return rummy.getMesaJugadas();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int cantCartasOponente(String oponente){
        try {
            return rummy.getCantCartasOponente(oponente);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void activarModoExpres(){
        try {
            rummy.modoExpres();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int getJugadasSize()  {
        try {
            return rummy.getMesaJugadas().getJugada().size();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void activarModoPuntos() {
        try {
            rummy.modoPuntos();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int cantFichas() {
        try {
            return rummy.getCantidadFichas(nombreJugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
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
            throw new RuntimeException(e);
        }
    }

    public void cancelarApuesta() {
        try {
            rummy.cancelarApuestas();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean apuestasActivadas() {
        try {
            return rummy.isApuestasActivas();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void restarFichas() {
        try {
            rummy.apostarFichasJugador(nombreJugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int getcantidadFichasBote() {
        try {
            return rummy.getCantidadTotalApuesta();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public String getGanador() {
        try {
            return rummy.getNombreGanador();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCantidadPuntosGanador() {
        try {
            return rummy.getPuntosGanador();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void iniciarNuevaRonda() {
        try {
            rummy.iniciarNuevaRonda();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public String getModoJuego() {
        try {
            return rummy.getModoActual();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void solicitarAnularPartida() {
        try {
            rummy.pedidoAnularPartidaAmistosamente();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    public String obtenerJugador(int posicion) {
        try {
            return rummy.getJugador(posicion);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void hacerReenganche() {
        try {
            rummy.reengancharJugador(nombreJugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEliminado() {
        try {
            return rummy.estaEliminado(nombreJugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCantDisponibles() {
        try {
            return (rummy.getJugadoresSize() - rummy.contarEliminados());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void nuevoJuego() {
        try {
            rummy.nuevoJuego();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /*public void cerrarJuego() {
        try {
            rummy.cerrarJuego();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }*/

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
            throw new RuntimeException(e);
        }
        vista.mostrarTablaPosiciones(jugadores);
        serializador.writeOneObject(jugadores.get(0));
        for (int i = 1; i < jugadores.size(); i++) {
            serializador.addOneObject(jugadores.get(i));
        }
    }

    public void eliminarJugador() {
        try {
            rummy.removerObservador(this);
            rummy.eliminarJugador(nombreJugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
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
                throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    public void setTiempoTurno(int tiempoEstablecido) {
        try {
            rummy.cambiarTiempoTurno(tiempoEstablecido);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int getTiempoPorTurno(){
        try {
            return rummy.getCantidadTiempoTurno();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public String getNombreAnfitrion() {
        try {
            return rummy.getNombreJefeMesa();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public String getNombreTurnoActual() {
        try {
            return rummy.getNombreTurnoActual();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int getpuntosJugador() {
        return 0;
    }

    public void iniciarJuegoAutomatico() {
        try {
            rummy.juegoAutomatico(nombreJugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean jugadorEnAutomatico() {
        try {
            return rummy.isJugadorEnAutomatico(nombreJugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void desactivarJuegoAutomatico() {
        try {
            rummy.desactivarJuegoAutomatico(nombreJugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
