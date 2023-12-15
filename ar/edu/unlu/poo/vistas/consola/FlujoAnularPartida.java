package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

public class FlujoAnularPartida extends Flujo{
    public FlujoAnularPartida(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
    }
    private boolean tomoDecision = false;

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        txtIngresado = txtIngresado.toUpperCase();
        switch (txtIngresado){
            case "Y" -> {
                tomoDecision = true;
                vistaConsola.print("Usted tomo su eleccion. Esperando a que el resto de jugadores decida...");
                controlador.tomarDecisionDePartida(txtIngresado);
            }
            case "N" -> controlador.tomarDecisionDePartida(txtIngresado);
            default -> vistaConsola.opcionIncorrecta();
        }
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {
        if (!tomoDecision) {
            vistaConsola.print("Se solicito anular la partida.");
            vistaConsola.print("¿Usted desea anular la partida? (Y/N)");
        }
    }
}
