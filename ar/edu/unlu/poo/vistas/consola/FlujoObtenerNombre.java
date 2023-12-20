package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

public class FlujoObtenerNombre extends Flujo{
    public FlujoObtenerNombre(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
        mostrarSiguienteTexto();
    }

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        if (txtIngresado.isEmpty()){
            mostrarSiguienteTexto();
            vistaConsola.print("El nombre no puede estar vacio.");
        } else if (!controlador.estaEnElJuego(txtIngresado)){
            controlador.nuevoJugador(controlador.esAnfitrion(), txtIngresado);
            return vistaConsola.flujoActual();
        }else {
            mostrarSiguienteTexto();
            vistaConsola.print("Error: hay alguien con ese nombre en la partida!!!");
        }
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {
        vistaConsola.limpiarPantalla();
        vistaConsola.print("Bienvenido, Escriba su nombre...");
    }
}
