package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

import java.util.ArrayList;

public class FlujoTerminarTurno extends Flujo{
    public FlujoTerminarTurno(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
    }

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        if (vistaConsola.esNumero(txtIngresado)) {
            int posicion = Integer.parseInt(txtIngresado);
            if (posicion <= controlador.getJugadasSize() && posicion > 0) {
                ArrayList<Integer> posicionSeleccionada = new ArrayList<>();
                posicionSeleccionada.add(posicion - 1);
                controlador.terminarTurno(posicionSeleccionada);
            } else if (posicion == 0) {
                controlador.terminarTurno(new ArrayList<>());
            } else {
                vistaConsola.errorRangoNumerico();
            }
        }else {
            vistaConsola.opcionIncorrecta();
        }
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {
        vistaConsola.limpiarPantalla();
        vistaConsola.print("Para finalizar su turno, seleccione una carta para descartar (en el caso de que no tenga cartas escriba un 0)");
        vistaConsola.mostrarSeleccionCartas();
    }
}
