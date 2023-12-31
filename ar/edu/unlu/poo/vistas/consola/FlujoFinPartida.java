package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

public class FlujoFinPartida extends Flujo{
    public FlujoFinPartida(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
        mostrarSiguienteTexto();
    }

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
       return vistaConsola.mostrarOpcionesNuevaPartida(txtIngresado,this);
    }

    @Override
    public void mostrarSiguienteTexto() {
        vistaConsola.limpiarPantalla();
        vistaConsola.print("La partida ha finalizado!!!");
        vistaConsola.print("El ganador es..." + controlador.getGanador() +
        " con " + controlador.getCantidadPuntosGanador()+" puntos");
        if (!controlador.getEstadoCompetitivo()){
            vistaConsola.print("\nNo se ganaron ni puntos ni fichas apostadas porque el competitivo esta desactivado.");
        }
        vistaConsola.mostrarTablaPosiciones(controlador.obtenerPosiciones());
        //controlador.obtenerPosiciones();
        vistaConsola.mostrarOpcionesNuevaPartida();
    }
}
