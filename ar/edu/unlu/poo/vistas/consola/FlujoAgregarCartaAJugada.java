package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.ITapete;
import ar.edu.unlu.poo.vistas.VistaConsola;

import java.util.ArrayList;

public class FlujoAgregarCartaAJugada extends Flujo{

    private enum EstadosPosibles{
        SELECCION_JUGADA, SELECCION_CARTAS
    }

    private EstadosPosibles estadoActual;
    public FlujoAgregarCartaAJugada(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
        estadoActual = EstadosPosibles.SELECCION_JUGADA;
        mostrarSiguienteTexto();
    }


    private int posicionJugada;
    private ArrayList<Integer> posicionesSeleccionadas = new ArrayList<>();

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
            switch (estadoActual) {
                case SELECCION_JUGADA -> {
                    if (vistaConsola.esNumero(txtIngresado)) {
                        return seleccionarJugada(txtIngresado);
                    } else {
                        vistaConsola.opcionIncorrecta();
                    }
                }
                case SELECCION_CARTAS -> {
                    System.out.println("llego aca");
                    if (vistaConsola.esNumero(txtIngresado)) {
                        return agregarPosicionCarta(txtIngresado);
                    } else {
                        vistaConsola.opcionIncorrecta();
                    }
                }
            }
        return this;
    }

    private Flujo agregarPosicionCarta(String txtIngresado) {
        int numero = Integer.parseInt(txtIngresado);
        if (numero <= controlador.getCartasSize() && numero > 0){
            posicionesSeleccionadas.add(numero - 1);
            vistaConsola.print("Carta " + numero + " seleccionada!!");
        } else if (numero == 0) {
            System.out.println("llego antes de agregar carta a jugada");
            controlador.agregarCartasAJugada(posicionesSeleccionadas, posicionJugada);
            posicionesSeleccionadas.clear();
            System.out.println("llego a agregar carta jugada");
            return new FlujoContinuarTurno(vistaConsola, controlador);//comprobar si esta bien
        }else {
            vistaConsola.errorRangoNumerico();
        }
        return this;
    }

    private Flujo seleccionarJugada(String txtIngresado) {
        int numero = Integer.parseInt(txtIngresado);
        if (numero <= controlador.getJugadasSize() && numero > 0){
            posicionJugada = numero - 1;
            estadoActual = EstadosPosibles.SELECCION_CARTAS;
            mostrarSiguienteTexto();
        } else if (numero == 0) {
            return new FlujoContinuarTurno(vistaConsola,controlador);
        }else {
            vistaConsola.errorRangoNumerico();
        }
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {
        vistaConsola.limpiarPantalla();
        switch (estadoActual) {
            case SELECCION_JUGADA -> {
                vistaConsola.jugadaYaVista();
                ITapete jugadasEnMesa = controlador.obtenerJugadas();
                vistaConsola.print(String.valueOf(jugadasEnMesa));
                vistaConsola.print("Seleccione la jugada a la que quiera agregar una carta \n(si desea agregar cartas a la jugada 1 solo seleccione el 1 y asi con las demas) \n\npresione 0 para cancelar");
                vistaConsola.guardarTxtActual();
            }
            case SELECCION_CARTAS -> {
                vistaConsola.mostrarSeleccionCartas();
                vistaConsola.guardarTxtActual();
            }
        }
        //vistaConsola.print("0- Volver a las opciones de turno");
    }
}
