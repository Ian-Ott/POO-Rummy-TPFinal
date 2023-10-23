package ar.edu.unlu.poo.app;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.ventana.Ventana;

import javax.swing.*;

public class NuevoJuego {
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try{
                Controlador controlador = new Controlador();
                Ventana v = new Ventana(controlador);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
