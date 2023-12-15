package ar.edu.unlu.poo.vistas.consola;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.ITapete;
import ar.edu.unlu.poo.vistas.VistaConsola;

import java.util.ArrayList;

public class FlujoContinuarTurno extends Flujo{
    public FlujoContinuarTurno(VistaConsola consola, Controlador controlador) {
        super(consola, controlador);
    }


    @Override
    public Flujo procesarEntrada(String txtIngresado) {
        if (controlador.isEliminado() && controlador.apuestasActivadas()){
            if (txtIngresado.equals("1")){
                controlador.hacerReenganche();
            }else {
                vistaConsola.opcionIncorrecta();
            }
        }else if (controlador.esTurnoJugador()){
            switch (txtIngresado) {
                case "1" -> {
                    return new FlujoAgregarJugada(vistaConsola, controlador);
                }
                case "2" -> {
                    return new FlujoAgregarCartaAJugada(vistaConsola, controlador);
                }
                case "3" -> mostrarCantidadCartas();
                case "4" -> {
                    return new FlujoOpcionesDeMesa(vistaConsola, controlador);
                }
                case "5" -> controlador.solicitarAnularPartida();
                case "0" -> {
                    return new FlujoTerminarTurno(vistaConsola, controlador);
                }
                default -> vistaConsola.opcionIncorrecta();
            }
        } else if (!controlador.esTurnoJugador() && controlador.jugadorEnAutomatico()) {
            if (txtIngresado.equals("1")){
                controlador.desactivarJuegoAutomatico();
            }else {
                vistaConsola.opcionIncorrecta();
            }
        }
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {
        vistaConsola.limpiarPantalla();
        if (controlador.isEliminado()){
            if (controlador.apuestasActivadas()) {
                vistaConsola.print("Usted ha sido eliminado de la partida por sobrepasar la cantidad de puntos.");
                vistaConsola.print("¿Quiere reengancharse? (si acepta debe apostar la mitad de lo que aposto en un inicio)");
                vistaConsola.print("Tus fichas: " + controlador.cantFichas() + " | Cantidad apostada: " + controlador.getcantidadApostada());
                vistaConsola.print("1-Para Reengancharse");
            }else {
                vistaConsola.print("Usted ha sido eliminado de la partida por sobrepasar la cantidad de puntos.");
                vistaConsola.print("No puede reengancharse porque no hay apuestas activas :(");
            }
        } else if (controlador.esTurnoJugador()){
            mostrarMenuTurnoActual();
            vistaConsola.mostrarCartas();
            if (vistaConsola.hayJugadasSinVer()){
                vistaConsola.print("\nHay nuevas jugadas disponibles en la mesa!!!");
            }
            vistaConsola.guardarTxtActual();
        }else {
            esperarTurno();
        }
    }

    private void mostrarMenuTurnoActual() {
        String bienvenida = getTxtBienvenida();
        vistaConsola.print("----------------------------------------------------------");
        vistaConsola.print("| Modo " + controlador.getModoJuego() + " |");
        vistaConsola.print(bienvenida);
        vistaConsola.print("Tus fichas: " + controlador.cantFichas() + " Tu apuesta: " + controlador.getcantidadApostada());
        vistaConsola.print("Cantidad de fichas en el bote de apuestas: " + controlador.getcantidadFichasBote());
        vistaConsola.print("Jugadores Restantes en la partida: " + controlador.getCantDisponibles());
        if (controlador.getModoJuego().equals("JUEGOAPUNTOS")){
            vistaConsola.print("Tus puntos de partida: "+ controlador.getpuntosJugador());
        }
        vistaConsola.print("----------------------------------------------------------");
        vistaConsola.print("Seleccione una opcion para continuar su turno:");
        vistaConsola.print("1-Hacer nueva Jugada" );
        vistaConsola.print("2-Ver jugadas en mesa / Agregar carta a una jugada");
        vistaConsola.print("3-ver cartas restantes jugadores");
        vistaConsola.print(vistaConsola.procesarCambiosMesa());
        vistaConsola.print("5-Anular Partida");
        vistaConsola.print("0-terminar turno");
        vistaConsola.print("----------------------------------------------------------");
    }

    private String getTxtBienvenida() {
        String bienvenida;
        if (controlador.esAnfitrion()){
            bienvenida = "\nBienvenido " + controlador.getNombreJugador() + "*";
        }else {
            bienvenida = "\nBienvenido " + controlador.getNombreJugador();
        }
        return bienvenida;
    }

    private void esperarTurno() {
        vistaConsola.print("______________________________________________");
        vistaConsola.print("Ha iniciado un nuevo turno, pero no es suyo. Espere su siguiente turno...");
        vistaConsola.print("______________________________________________");

        if (controlador.jugadorEnAutomatico()){
            vistaConsola.cambiarEstadoConsola(true);
            vistaConsola.print("\nSe te activo el juego automatico por no terminar tu turno a tiempo.");
            vistaConsola.print("\nAVISO: si todos los jugadores entran en modo automatico la partida finalizara amistosamente.");
            vistaConsola.print("1-Para cancelar el juego automatico sino siga esperando su turno.");
        }else {
            vistaConsola.cambiarEstadoConsola(false);
        }
    }

    private void mostrarCantidadCartas(){
        ArrayList<String> oponentes = controlador.nombreOponentes(controlador.getNombreJugador());
        String nombreActual;
        for (int i = 0; i < controlador.cantJugadores() - 1;i++){
            nombreActual = oponentes.get(i);
            if (controlador.getNombreAnfitrion().equals(nombreActual)){
                nombreActual += " *";
            }
            vistaConsola.print("\nJugador " + nombreActual);
            vistaConsola.print("Cantidad de cartas: " + controlador.cantCartasOponente(oponentes.get(i)));
        }
    }
}
