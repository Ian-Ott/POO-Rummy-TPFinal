package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

public class FlujoFinPartidaAmistosa extends Flujo{

    public FlujoFinPartidaAmistosa(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
    }

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        return vistaConsola.mostrarOpcionesNuevaPartida(txtIngresado, this);
    }



    @Override
    public void mostrarSiguienteTexto() {
        vistaConsola.print("La partida ha finalizado Amistosamente!!! Se devolvieron apuestas actuales y los puntos no cuentan");
        //controlador.obtenerPosiciones();
        vistaConsola.mostrarTablaPosiciones(controlador.obtenerPosiciones());
        vistaConsola.mostrarOpcionesNuevaPartida();
    }
}
