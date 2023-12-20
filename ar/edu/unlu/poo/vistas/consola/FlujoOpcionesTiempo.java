package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

public class FlujoOpcionesTiempo extends Flujo{
    public FlujoOpcionesTiempo(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
        mostrarSiguienteTexto();
    }

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        switch (txtIngresado){
            case "1" -> {
                if (controlador.getTiempoPorTurno() != 60) {
                    controlador.setTiempoTurno(60);
                    return new FlujoOpcionesTiempo(vistaConsola, controlador);
                }else {
                    vistaConsola.print("Opcion ya Activa.");
                }
            }
            case "2" ->{
                if (controlador.getTiempoPorTurno() != 120) {
                    controlador.setTiempoTurno(120);
                    return new FlujoOpcionesTiempo(vistaConsola, controlador);
                }else {
                    vistaConsola.print("Opcion ya Activa.");
                }
            }
            case "3" -> {
                if (controlador.getTiempoPorTurno() != 0){
                    controlador.setTiempoTurno(0);
                    return new FlujoOpcionesTiempo(vistaConsola, controlador);
                }else {
                    vistaConsola.print("Opcion ya Activa.");
                }
            }
            case "0" -> {
                return new FlujoOpcionesDeMesa(vistaConsola, controlador);
            }
            default -> vistaConsola.opcionIncorrecta();
        }
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {
        vistaConsola.limpiarPantalla();
        vistaConsola.print("<Opciones de Tiempo>");
        vistaConsola.print("Seleccione la cantidad de tiempo que quiere por cada turno:");
        vistaConsola.print("1- 60 segundos por turno");
        vistaConsola.print("2- 120 segundos por turno");
        vistaConsola.print("3- Desactivar tiempo por turnos");
        vistaConsola.print("0- Volver");
        vistaConsola.print("Tiempo actual: " + vistaConsola.mostrarTiempoActual());
    }
}
