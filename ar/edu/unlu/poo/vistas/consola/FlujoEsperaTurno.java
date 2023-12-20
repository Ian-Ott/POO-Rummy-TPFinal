package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

public class FlujoEsperaTurno extends Flujo{
    public FlujoEsperaTurno(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
        mostrarSiguienteTexto();
    }

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        if (controlador.isEliminado() && controlador.apuestasActivadas()){
            if (txtIngresado.equals("1")){
                controlador.hacerReenganche();
            }else {
                vistaConsola.opcionIncorrecta();
            }
        }else if (!controlador.esTurnoJugador() && controlador.jugadorEnAutomatico()) {
            if (txtIngresado.equals("1")){
                controlador.desactivarJuegoAutomatico();
            }else {
                vistaConsola.opcionIncorrecta();
            }
        }
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {
        vistaConsola.limpiarPantalla();
        if (controlador.isEliminado()) {
            vistaConsola.mostrarAvisoEliminado();
        }else {
            esperarTurno();
        }
    }

    private void esperarTurno() {
        vistaConsola.limpiarPantalla();
        vistaConsola.print("______________________________________________");
        vistaConsola.print("Ha iniciado un nuevo turno, pero no es suyo. Espere su siguiente turno...");
        vistaConsola.print("______________________________________________");

        if (controlador.jugadorEnAutomatico()){
            vistaConsola.cambiarEstadoConsola(true);
            vistaConsola.print("\nSe te activo el juego automatico por no terminar tu turno a tiempo.");
            vistaConsola.print("\nAVISO: si todos los jugadores entran en modo automatico la partida finalizara amistosamente.");
            vistaConsola.print("1-Para cancelar el juego automatico sino siga esperando su turno.");
        }else {
            vistaConsola.cambiarEstadoConsola(false);
        }
    }
}
