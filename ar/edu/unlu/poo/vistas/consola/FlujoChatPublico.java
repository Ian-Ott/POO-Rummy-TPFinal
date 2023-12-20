package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
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
            }else if (txtIngresado.equals("/mostrarCartasJugadores")){

            } else if (txtIngresado.equals("/mostrarJugadas")) {

            } else if (txtIngresado.equals("/mostrarNombreTurnoActual")) {

            } else if (txtIngresado.equals("/puntosDePartida")) {

            } else if (txtIngresado.equals("/top5Jugadores")) {

            }else {
                controlador.mostrarMensaje(txtIngresado);
            }
        }
        return this;
    }

    private void mostrarListaComandos() {
        vistaConsola.print("_____________________________________________________________");
        vistaConsola.print("Lista de Comandos:");
        vistaConsola.print("/mostrarCartasJugadores\t/mostrarJugadas\t/mostrarNombreTurnoActual");
        vistaConsola.print("/puntosDePartida\t/top5Jugadores");
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
