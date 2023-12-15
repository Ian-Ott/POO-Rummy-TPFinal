package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

public class FlujoOpcionesTiempo extends Flujo{
    public FlujoOpcionesTiempo(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
    }

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        switch (txtIngresado){
            case "1" -> controlador.setTiempoTurno(60);
            case "2" -> controlador.setTiempoTurno(120);
            case "3" -> controlador.setTiempoTurno(0);
            case "0" -> {
                return new FlujoOpcionesDeMesa(vistaConsola, controlador);
            }
            default -> vistaConsola.opcionIncorrecta();
        }
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {
        vistaConsola.print("<Opciones de Tiempo>");
        vistaConsola.print("Seleccione la cantidad de tiempo que quiere por cada turno:");
        vistaConsola.print("1- 60 segundos por turno");
        vistaConsola.print("2- 120 segundos por turno");
        vistaConsola.print("3- Desactivar tiempo por turnos");
        vistaConsola.print("0- Volver");
        vistaConsola.print("Tiempo actual: " + vistaConsola.mostrarTiempoActual());
    }
}
