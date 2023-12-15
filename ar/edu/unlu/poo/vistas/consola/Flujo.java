package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

public abstract class Flujo {
    protected final VistaConsola vistaConsola;
    protected final Controlador controlador;

    public Flujo(VistaConsola consola, Controlador controlador){
        this.vistaConsola = consola;
        this.controlador = controlador;
    }

    public abstract Flujo procesarEntrada(String txtIngresado);

    public abstract void mostrarSiguienteTexto();
}
