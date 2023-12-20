package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

public class FlujoEsperaNuevaRonda extends Flujo{
    public FlujoEsperaNuevaRonda(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
        mostrarSiguienteTexto();
    }

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        if (controlador.esAnfitrion()){
            controlador.iniciarNuevaRonda();
            return vistaConsola.flujoActual();
        }
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {
        vistaConsola.limpiarPantalla();
        if (controlador.esAnfitrion()){
            vistaConsola.print("\nPresione Enter para comenzar una nueva ronda.");
        }else {
            vistaConsola.print("\nEspere a que se inicie una nueva ronda.");
        }
    }
}
