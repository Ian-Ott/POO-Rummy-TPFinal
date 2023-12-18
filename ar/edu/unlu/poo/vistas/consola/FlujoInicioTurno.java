package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

public class FlujoInicioTurno extends Flujo{
    public FlujoInicioTurno(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
    }

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        switch (txtIngresado){
            case "1" -> {
                controlador.tomarCartaMazo();
                return vistaConsola.flujoActual();
            }
            case "2" -> {
                controlador.tomarCartaDescarte();
                return vistaConsola.flujoActual();
            }
            case "3" -> {
                return new FlujoGuardarPartida(vistaConsola, controlador);
            }
            default -> vistaConsola.opcionIncorrecta();
        }
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {
        verificarEstado();
        vistaConsola.limpiarPantalla();
        vistaConsola.print("----------------------------------------------------------");
        vistaConsola.print("1-tomar Carta del mazo");
        vistaConsola.print("2-tomar carta descartada");
        vistaConsola.print("3-Guardar Partida");
        vistaConsola.print("Carta disponible en la pila de descartes:" + controlador.getCartaDescarte());
        vistaConsola.mostrarCartas();
        vistaConsola.guardarTxtActual();
        vistaConsola.cambiarEstadoConsola(true);
    }

    private void verificarEstado() {
        if (controlador.jugadorEnAutomatico()) {
            System.out.println("entro a juego automatico aca");
            controlador.iniciarJuegoAutomatico();
        }else if (controlador.getTiempoPorTurno() != 0){
            vistaConsola.activarTiempoDeTurno();
        }
    }
}
