package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

import java.util.ArrayList;

public class FlujoGuardarPartida extends Flujo{
    private ArrayList<String> partidaDisponible;
    private EstadosPosibles estadoActual = EstadosPosibles.ELECCION_NOMBRE;
    private String nombrePartidaActual;
    private enum EstadosPosibles{
        SIN_ESTADO,SELECCION_PARTIDA, ELECCION_NOMBRE
    }
    public FlujoGuardarPartida(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
        mostrarSiguienteTexto();
    }

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        if (estadoActual.equals(EstadosPosibles.ELECCION_NOMBRE)){
            nombrePartidaActual = txtIngresado;
            estadoActual = EstadosPosibles.SIN_ESTADO;
        }else if (estadoActual.equals(EstadosPosibles.SELECCION_PARTIDA)){
            if (vistaConsola.esNumero(txtIngresado)){
                int posicion = Integer.parseInt(txtIngresado);
                posicion = posicion - 1;
                if(partidaDisponible.size() > posicion && posicion > -1){
                    controlador.sobreescribirPartidaGuardada(posicion,nombrePartidaActual);
                    return vistaConsola.flujoActual();
                } else if (posicion == -1) {
                    estadoActual = EstadosPosibles.SIN_ESTADO;
                } else {
                    vistaConsola.errorRangoNumerico();
                }
            }else {
                vistaConsola.opcionIncorrecta();
            }
        } else if (txtIngresado.equals("1") && !partidaDisponible.isEmpty()) {
            estadoActual = EstadosPosibles.SELECCION_PARTIDA;
        } else if (txtIngresado.equals("2") && partidaDisponible.size() != 8) {
            controlador.guardarPartida(nombrePartidaActual);
            return vistaConsola.flujoActual();
        } else if (txtIngresado.equals("3")) {
            estadoActual = EstadosPosibles.ELECCION_NOMBRE;
        } else if (txtIngresado.equals("0")) {
            return new FlujoInicioTurno(vistaConsola, controlador);
        }else {
            vistaConsola.opcionIncorrecta();
        }
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {
        vistaConsola.limpiarPantalla();
        partidaDisponible = controlador.getPartidasDisponibles();
        vistaConsola.mostrarPartidasDisponibles(partidaDisponible);
        vistaConsola.print("__________________________________________________________");
        if (estadoActual.equals(EstadosPosibles.ELECCION_NOMBRE)){
          vistaConsola.print("Escriba el nombre de la partida que quiere guardar: ");
        } else if (partidaDisponible.isEmpty() && estadoActual.equals(EstadosPosibles.SIN_ESTADO)) {
            vistaConsola.print("2- Guardar partida en espacio libre");
            vistaConsola.print("3-Elegir otro nombre");
            vistaConsola.print("0-Volver al turno");
        } else if (partidaDisponible.size() == 8 && estadoActual.equals(EstadosPosibles.SIN_ESTADO)){
            vistaConsola.print("1- Sobreescribir partida");
            vistaConsola.print("3-Elegir otro nombre");
            vistaConsola.print("0-Volver al turno");
        }else if (estadoActual.equals(EstadosPosibles.SELECCION_PARTIDA)){
            vistaConsola.print("Seleccione la partida que desea sobreescribir por su numero de posicion (empezando por la 1)");
            vistaConsola.print("0- volver");
        }else{
            vistaConsola.print("1- Sobreescribir partida");
            vistaConsola.print("2- Guardar partida en espacio libre");
            vistaConsola.print("3- elegir otro nombre");
            vistaConsola.print("0-Volver al turno");
        }
        vistaConsola.guardarTxtActual();
    }
}
