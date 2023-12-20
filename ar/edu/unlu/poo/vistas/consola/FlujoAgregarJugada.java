package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

import java.util.ArrayList;

public class FlujoAgregarJugada extends Flujo{
    public FlujoAgregarJugada(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
        mostrarSiguienteTexto();
    }

    private ArrayList<Integer> posicionesSeleccionadas = new ArrayList<>();

    private enum EstadosPosibles{
        SELECCION_TIPO_JUGADA,POSIBLE_RUMMY,POSIBLE_ESCALERA,POSIBLE_COMBINACION_CARTAS_IGUALES
    }
    private EstadosPosibles estadoActual = EstadosPosibles.SELECCION_TIPO_JUGADA;
    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        vistaConsola.limpiarPantalla();
        switch (estadoActual){
            case SELECCION_TIPO_JUGADA -> {
                if (txtIngresado.equals("1")) {
                    estadoActual = EstadosPosibles.POSIBLE_RUMMY;
                } else if (txtIngresado.equals("2")) {
                    estadoActual = EstadosPosibles.POSIBLE_ESCALERA;
                } else if (txtIngresado.equals("3")) {
                    estadoActual = EstadosPosibles.POSIBLE_COMBINACION_CARTAS_IGUALES;
                }else {
                    vistaConsola.opcionIncorrecta();
                }
            }
            case POSIBLE_RUMMY, POSIBLE_ESCALERA, POSIBLE_COMBINACION_CARTAS_IGUALES -> {
                return agregarPosicionCarta(txtIngresado);
            }
        }
        return this;
    }

    private Flujo agregarPosicionCarta(String txtIngresado) {
        int posicion = Integer.parseInt(txtIngresado);
        if (posicion <= controlador.getJugadasSize() && posicion > 0){
            posicionesSeleccionadas.add(posicion - 1);
        } else if (posicion == 0) {
            switch (estadoActual) {
                case POSIBLE_RUMMY ->  controlador.armarRummy(posicionesSeleccionadas);
                case POSIBLE_ESCALERA -> controlador.armarEscalera(posicionesSeleccionadas);
                case POSIBLE_COMBINACION_CARTAS_IGUALES -> controlador.armarCombinacionIguales(posicionesSeleccionadas);
            }
            return vistaConsola.flujoActual();//revisar
        }else {
            vistaConsola.errorRangoNumerico();
        }
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {
        switch (estadoActual){
            case SELECCION_TIPO_JUGADA -> {
                vistaConsola.print("Seleccione el tipo de jugada que quiere hacer:");
                vistaConsola.print("1-Hacer Rummy");
                vistaConsola.print("2-Hacer Escalera");
                vistaConsola.print("3-Hacer Combinacion de cartas con numeros iguales");
                vistaConsola.mostrarCartas();
                vistaConsola.guardarTxtActual();
            }
            case POSIBLE_RUMMY,POSIBLE_ESCALERA,POSIBLE_COMBINACION_CARTAS_IGUALES -> {
                vistaConsola.mostrarSeleccionCartas();//cambiar
                vistaConsola.guardarTxtActual();
            }
        }
    }
}
