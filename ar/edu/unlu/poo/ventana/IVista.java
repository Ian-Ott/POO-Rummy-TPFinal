package ar.edu.unlu.poo.ventana;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.Carta;

public interface IVista {
    void iniciarVentana();

    void actualizarCarta(Carta cambio);

    void setControlador(Controlador controlador);

    void pantallaEspera(boolean anfitrion, int size);
}
