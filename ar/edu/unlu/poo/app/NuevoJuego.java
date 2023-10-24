package ar.edu.unlu.poo.app;

import ar.edu.unlu.poo.controlador.Controlador;
import ar.edu.unlu.poo.modelo.IRummy;
import ar.edu.unlu.poo.modelo.Rummy;
import ar.edu.unlu.poo.ventana.Ventana;

import javax.swing.*;

public class NuevoJuego {
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try{
                    IRummy modelo = Rummy.getInstancia();
                    Ventana v = new Ventana();
                    Controlador controlador = new Controlador(v,modelo);
                    v.setControlador(controlador);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
