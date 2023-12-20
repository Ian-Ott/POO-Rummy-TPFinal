package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

import java.util.ArrayList;

public class FlujoCargarPartida extends Flujo{
    public FlujoCargarPartida(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
        mostrarSiguienteTexto();
    }

    private ArrayList<String> partidaDisponible;

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        if (!partidaDisponible.isEmpty()){
            if (vistaConsola.esNumero(txtIngresado)) {
                int posicion = Integer.parseInt(txtIngresado);
                posicion = posicion - 1;
                if (partidaDisponible.size() > posicion && posicion > -1) {
                    controlador.cargarPartida(posicion);
                    return vistaConsola.flujoActual();
                } else if (posicion == -1) {
                    return new FlujoEsperaPartida(vistaConsola, controlador);
                } else {
                    vistaConsola.errorRangoNumerico();
                }
            }else {
                vistaConsola.opcionIncorrecta();
            }
        } else if (txtIngresado.equals("0")) {
            return new FlujoEsperaPartida(vistaConsola, controlador);
        }
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {
        vistaConsola.limpiarPantalla();
        partidaDisponible = controlador.getPartidasDisponibles();
        vistaConsola.mostrarPartidasDisponibles(partidaDisponible);
        vistaConsola.print("__________________________________________________________");
        if (!partidaDisponible.isEmpty()) {
            vistaConsola.print("Seleccione la partida que desea cargar por su numero de posicion (empezando por la 1)");
        }
        vistaConsola.print("0- volver");
    }
}
