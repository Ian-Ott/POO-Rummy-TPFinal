package ar.edu.unlu.poo.vistas;

import ar.edu.unlu.poo.modelo.Carta;
import ar.edu.unlu.poo.modelo.Jugador;
import ar.edu.unlu.poo.modelo.Rummy;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

public class VistaGrafica {
    private JFrame frame;
    private JTabbedPane tabbedPane1;
    private JList listaAbajo;
    private JPanel panelVentana;
    private JTabbedPane OpcionMesa;
    private JList list2;
    private JList list4;
    private DefaultListModel<String> listaModelo;

    public VistaGrafica() throws RemoteException {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(panelVentana);
        frame.pack();
        listaModelo = new DefaultListModel<>();
        listaAbajo.setModel(listaModelo);
        Rummy moddelo = new Rummy();
        moddelo.agregarJugador(new Jugador("pepito"));
        moddelo.agregarJugador(new Jugador("un jugador"));
        moddelo.iniciarJuego();
        mostrarResultadosBusqueda(moddelo.getCartasJugador("pepito"));
        frame.setVisible(true);
    }

    public void mostrarResultadosBusqueda(List<Carta> cartasJugador) {
        listaModelo.clear();

        for (Carta carta: cartasJugador) {
            //JLabel a = new JLabel();
            //a.setText(carta.toString());
            listaModelo.addElement(carta.toString());
        }

        //cbLibroDevolucion.removeAllItems();
        /*for (Libro libro: librosEncontrados) {
            if (libro.hayPrestados()) {
                cbLibroDevolucion.addItem(libro);
            }
        }*/
    }

}
