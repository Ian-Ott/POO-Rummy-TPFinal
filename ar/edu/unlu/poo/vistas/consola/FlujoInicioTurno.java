package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.exceptions.NoHayCartaBocaArriba;
import ar.edu.unlu.poo.vistas.VistaConsola;

public class FlujoInicioTurno extends Flujo{
    public FlujoInicioTurno(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
        mostrarSiguienteTexto();
    }

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        switch (txtIngresado){
            case "1" -> {
                controlador.tomarCartaMazo();
                return vistaConsola.flujoActual();
            }
            case "2" -> {
                try {
                    if (controlador.getCartaDescarte() != null) {
                        controlador.tomarCartaDescarte();
                    }
                } catch (NoHayCartaBocaArriba e) {
                    vistaConsola.opcionIncorrecta();
                }
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
        try {
            if (controlador.getCartaDescarte() != null) {
                vistaConsola.print("2-tomar carta descartada");
            }
        } catch (NoHayCartaBocaArriba e) {
            System.out.println(" no hay carta de descarte por lo que no esta disponible la opcion 2");
        }
        vistaConsola.print("3-Guardar Partida");
        vistaConsola.print("Carta disponible en la pila de descartes:" + obtenerCartaDescarte());
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

    private String obtenerCartaDescarte() {
        try {
            return controlador.getCartaDescarte().toString();
        } catch (NoHayCartaBocaArriba e) {
            return " No hay carta de descarte";
        }
    }
}
