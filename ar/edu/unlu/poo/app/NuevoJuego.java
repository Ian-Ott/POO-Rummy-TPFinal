package ar.edu.unlu.poo.app;

import ar.edu.unlu.poo.ventana.Ventana;

import javax.swing.*;

public class NuevoJuego {
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Ventana v = new Ventana();
            }
        });
    }
}
