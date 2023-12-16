package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

public class FlujoOpcionesDeMesa extends Flujo{
    public FlujoOpcionesDeMesa(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
    }

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        if (controlador.esAnfitrion()) {
            switch (txtIngresado) {
                case "1" -> {
                    return new FlujoOpcionesTiempo(vistaConsola, controlador);
                }
                case "2" -> controlador.modificarPartidasCompetitivas();
                case "3" -> controlador.modificarOpcionChat();
                case "4" -> controlador.activarModoExpres();
                case "5" -> controlador.activarModoPuntos();
                case "0" -> {
                    return continuarEnFlujoAnterior();
                }
                default -> vistaConsola.opcionIncorrecta();
            }
        }else {
            if (txtIngresado.equals("0")){
                return continuarEnFlujoAnterior();
            }else {
                vistaConsola.opcionIncorrecta();
            }
        }
        return this;
    }

    private Flujo continuarEnFlujoAnterior() {
        Flujo flujoAnterior;
        if (controlador.juegoIniciado()){
            flujoAnterior = new FlujoContinuarTurno(vistaConsola, controlador);
        }else {
            flujoAnterior = new FlujoEsperaPartida(vistaConsola, controlador);
        }
        return flujoAnterior;
    }

    @Override
    public void mostrarSiguienteTexto() {
        vistaConsola.limpiarPantalla();
        vistaConsola.print("<Opciones de Mesa>");
        if (controlador.esAnfitrion()){
            vistaConsola.print("Seleccione las opciones que quiera cambiar: ");
            vistaConsola.print("1-Cambiar tiempo por cada turno (tiempo actual: " + vistaConsola.mostrarTiempoActual());
            vistaConsola.print("2-Activar/Desactivar partidas competitivas, Estado actual: " + vistaConsola.obtenerEstado(controlador.getEstadoCompetitivo()) + "(al desactivar esta opcion no se cuentan las fichas y los puntos obtenidos)");
            vistaConsola.print("3-Permitir publico y chat, Estado Actual: " + " ");
            vistaConsola.print("4-Activar Ronda Expres (es una partida rapida de una ronda en la que gana el jugador que cierra antes)");
            vistaConsola.print("5-Activar Modo Por Puntos (Cuando un jugador alcanza los 300 puntos queda eliminado. El último jugador es el ganador de la partida)");
            vistaConsola.print("Modo Actual: "+ controlador.getModoJuego());
            vistaConsola.print("0-Volver");
        }else {
            vistaConsola.print("No podes modificar las opciones de mesa solo esta disponible para el anfitrion.");
            vistaConsola.print("0-Volver");
        }
        vistaConsola.guardarTxtActual();
    }

}
