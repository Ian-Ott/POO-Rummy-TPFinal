package no.Usar;

import ar.edu.unlu.poo.ventana.VistaGrafica;

import javax.swing.*;

public class NuevoJuego {
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try{
                    /*IRummy modelo = Rummy.getInstancia();
                    Ventana v = new Ventana();
                    Controlador controlador = new Controlador(v);
                    v.setControlador(controlador);*/
                    VistaGrafica vista = new VistaGrafica();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
