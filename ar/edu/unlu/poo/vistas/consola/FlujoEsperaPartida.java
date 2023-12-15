package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.vistas.VistaConsola;

public class FlujoEsperaPartida extends Flujo{
    public FlujoEsperaPartida(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
    }

    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        if (vistaConsola.esNumero(txtIngresado)){
            int numeroIngresado = Integer.parseInt(txtIngresado);
            if (controlador.esAnfitrion()) {
                if (numeroIngresado >= 250 && numeroIngresado <= controlador.cantFichas() && !controlador.apuestasActivadas()) {
                    controlador.apostar(numeroIngresado);
                } else if (numeroIngresado == 1) {
                    vistaConsola.reiniciarTablaRonda();
                    controlador.iniciarJuego();
                } else if (numeroIngresado == 2) {
                    return new FlujoOpcionesDeMesa(vistaConsola, controlador);
                } else if (numeroIngresado == 0) {
                    controlador.cancelarApuesta();
                }else {
                    vistaConsola.errorRangoNumerico();
                }
            }else {
                if (numeroIngresado == 0 && controlador.apuestasActivadas()){
                    controlador.cancelarApuesta();
                }else {
                    vistaConsola.errorRangoNumerico();
                }
            }
        }else {
            vistaConsola.opcionIncorrecta();
        }
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {
        if (controlador.esAnfitrion()){
            mostrarEsperaAnfitrion();
        }else {
            mostrarEspera();
        }
    }

    private void mostrarEsperaAnfitrion(){
        vistaConsola.print("__________________________________________");
        vistaConsola.print("esperando a que se unan jugadores (se necesitan entre 2-4 jugadores para empezar a jugar)");
        vistaConsola.print("Cantidad de jugadores:" + controlador.cantJugadores());
        vistaConsola.print("__________________________________________");
        vistaConsola.print("Cuando este la cantidad de jugadores necesaria podra iniciar la partida:");
        vistaConsola.print("1-Iniciar Partida");
        vistaConsola.print("2-Cambiar Opciones de Mesa");
        vistaConsola.print("__________________________________________");
        vistaConsola.print("Si desea Apostar solo ingrese la cantidad que desea apostar y se definira la situacion de la apuesta segun la decision del resto de jugadores");
        vistaConsola.print("Minimo de Apuesta: 250");
        vistaConsola.print("Tus fichas:" + controlador.cantFichas());
        if (controlador.apuestasActivadas()){
            vistaConsola.print("\nApuestas Activadas!!!");
            vistaConsola.print("Si las quiere desactivar seleccione la opcion 0.");
        }
    }

    private void mostrarEspera(){
        vistaConsola.print("__________________________________________");
        vistaConsola.print("esperando a que se unan jugadores (se necesitan entre 2-4 jugadores para empezar a jugar)");
        vistaConsola.print("Cantidad de jugadores:" + controlador.cantJugadores());
        vistaConsola.print("__________________________________________");
        if (controlador.apuestasActivadas()){
            controlador.restarFichas();
            vistaConsola.print("\nAVISO:");
            vistaConsola.print("Las apuestas han sido activadas. (en el caso que quiera cancelar la apuesta presione 0)");
            vistaConsola.print("Tu apuesta seria de: " + controlador.getcantidadApostada() + " fichas.");
            vistaConsola.cambiarEstadoConsola(true);
        }else {
            vistaConsola.cambiarEstadoConsola(false);
        }
    }
}
