package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

public class FlujoPartidaCerrada extends Flujo{
    public FlujoPartidaCerrada(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
        mostrarSiguienteTexto();
    }

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        if (controlador.getModoJuego().equals("EXPRES")) {
            return vistaConsola.mostrarOpcionesNuevaPartida(txtIngresado, this);
        }else {
            controlador.resultadoRonda();//esto cambia el flujo a fin de ronda
        }
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {
        vistaConsola.limpiarPantalla();
        if (controlador.getModoJuego().equals("EXPRES")){
            vistaConsola.print("La partida fue cerrada ya que no se pueden  hacer combinaciones o añadir cartas ");
            if (!controlador.getEstadoCompetitivo()){
                vistaConsola.print("No se ganaron ni puntos ni fichas apostadas porque el competitivo esta desactivado.");
            }
            //controlador.obtenerPosiciones();
            vistaConsola.mostrarTablaPosiciones(controlador.obtenerPosiciones());
            vistaConsola.mostrarOpcionesNuevaPartida();
        }else {
            vistaConsola.print("La ronda fue cerrada por lo que se sumaran los puntos sobrantes a cada jugador e iniciara una nueva ronda...");
            vistaConsola.print("Presione Enter para continuar");
        }
    }
}
