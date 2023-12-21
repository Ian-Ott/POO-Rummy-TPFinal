package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.ITapete;
import ar.edu.unlu.poo.vistas.VistaConsola;

public class FlujoChatPublico extends Flujo{
    public FlujoChatPublico(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
        mostrarSiguienteTexto();
    }

    enum EstadosPosibles{
        ELECCION_NOMBRE, MODO_CHAT
    }
    private EstadosPosibles estadoActual = EstadosPosibles.ELECCION_NOMBRE;

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        if (estadoActual.equals(EstadosPosibles.ELECCION_NOMBRE)){
            controlador.nuevoEspectador(txtIngresado);
            estadoActual = EstadosPosibles.MODO_CHAT;
        }else {
            if (txtIngresado.equals("/ayuda")){
                mostrarListaComandos();
            }else if (txtIngresado.equals("/mostrarCantidadCartasJugadores")){
                vistaConsola.print("Jugador " + controlador.getNombreAnfitrion() + ": " +controlador.cantCartasOponente(controlador.getNombreAnfitrion()));
                for (String oponenteAnfitrion: controlador.nombreOponentes(controlador.getNombreAnfitrion())) {
                    vistaConsola.print("Jugador " + controlador.getNombreAnfitrion() + ": " + controlador.cantCartasOponente(oponenteAnfitrion));
                }
            } else if (txtIngresado.equals("/mostrarJugadas")) {
                ITapete jugadasEnMesa = controlador.obtenerJugadas();
                vistaConsola.print(String.valueOf(jugadasEnMesa));
            } else if (txtIngresado.equals("/mostrarNombreTurnoActual")) {
                vistaConsola.print("Jugador con el turno Actual: " + controlador.getNombreTurnoActual());
            }else {
                controlador.mostrarMensaje(txtIngresado);
            }
        }
        return this;
    }

    private void mostrarListaComandos() {
        vistaConsola.print("_____________________________________________________________");
        vistaConsola.print("Lista de Comandos:");
        vistaConsola.print("/mostrarCantidadCartasJugadores\t/mostrarJugadas\t/mostrarNombreTurnoActual");
        vistaConsola.print("-Recuerda usar siempre / cuando quieras escribir un comando");
        vistaConsola.print("_____________________________________________________________");
    }

    @Override
    public void mostrarSiguienteTexto() {
        if (controlador.publicoPermitido()) {
            if (estadoActual.equals(EstadosPosibles.ELECCION_NOMBRE)) {
                vistaConsola.print("Bienvenido, Escriba su nombre...");
                vistaConsola.print("Una vez que este en el chat puede presionar /ayuda para obtener la lista de comandos");
            }else {
                vistaConsola.print("La opcion de chat fue reactivada!!!");
            }
        }else {
            vistaConsola.print("El anfitrion desactivo la opcion de chat publico :(");
        }
    }
}
