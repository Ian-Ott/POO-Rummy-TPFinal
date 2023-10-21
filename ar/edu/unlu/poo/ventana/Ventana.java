package ar.edu.unlu.poo.ventana;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ventana implements ActionListener {
    JFrame frame;
    JLabel etiqueta2;
    public Ventana(){
        iniciarVentana();
    }

    private void iniciarVentana() {
        frame = new JFrame("Rummy Beta - Version 0.0");
        frame.setSize(1500,1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel etiqueta1 = new JLabel("Seleccione una opcion");
        JPanel panelPrincipal = (JPanel) frame.getContentPane();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.add(etiqueta1,BorderLayout.NORTH);

        //jugador de abajo
        JPanel menuJugadorAbajo = new JPanel();
        menuJugadorAbajo.setLayout(new BorderLayout());

        JButton boton1 = new JButton("carta1");
        boton1.addActionListener(this);
        boton1.setMinimumSize(new Dimension(20,20));
        boton1.setMaximumSize(new Dimension(240,240));
        boton1.setPreferredSize(new Dimension(120,200));

        JButton boton2 = new JButton("carta2");
        boton2.addActionListener(this);
        boton2.setMinimumSize(new Dimension(20,20));
        boton2.setMaximumSize(new Dimension(240,240));
        boton2.setPreferredSize(new Dimension(120,200));
        JButton boton3 = new JButton("carta3");
        boton3.addActionListener(this);
        boton3.setMinimumSize(new Dimension(20,20));
        boton3.setMaximumSize(new Dimension(240,240));
        boton3.setPreferredSize(new Dimension(120,200));
        JPanel cartasJugadorAbajo = new JPanel();
        cartasJugadorAbajo.setLayout(new FlowLayout());

        cartasJugadorAbajo.add(boton1);
        cartasJugadorAbajo.add(boton2);
        cartasJugadorAbajo.add(boton3);


        etiqueta2 = new JLabel("jugador 1", SwingConstants.CENTER);

        menuJugadorAbajo.add(etiqueta2,BorderLayout.NORTH);

        menuJugadorAbajo.add(cartasJugadorAbajo, BorderLayout.CENTER);

        panelPrincipal.add(menuJugadorAbajo,BorderLayout.SOUTH);

        //jugador de la derecha
        JPanel menuJugadorDerecha = new JPanel();
        menuJugadorDerecha.setLayout(new BorderLayout());

        JButton botonDerecha1 = new JButton("carta1");
        botonDerecha1.addActionListener(this);
        botonDerecha1.setMinimumSize(new Dimension(20,20));
        botonDerecha1.setMaximumSize(new Dimension(40,60));
        botonDerecha1.setPreferredSize(new Dimension(40,60));

        JButton botonDerecha2 = new JButton("carta2");
        botonDerecha2.addActionListener(this);
        botonDerecha2.setMinimumSize(new Dimension(20,20));
        botonDerecha2.setMaximumSize(new Dimension(40,60));
        botonDerecha2.setPreferredSize(new Dimension(40,60));
        JButton botonDerecha3 = new JButton("carta3");
        botonDerecha3.addActionListener(this);
        botonDerecha3.setMinimumSize(new Dimension(20,20));
        botonDerecha3.setMaximumSize(new Dimension(40,60));
        botonDerecha3.setPreferredSize(new Dimension(40,60));
        JPanel cartasJugadorDerecha = new JPanel();
        cartasJugadorDerecha.setLayout(new FlowLayout());

        cartasJugadorDerecha.add(botonDerecha1);
        cartasJugadorDerecha.add(botonDerecha2);
        cartasJugadorDerecha.add(botonDerecha3);

        etiqueta2 = new JLabel("jugador 2",SwingConstants.CENTER);
        menuJugadorDerecha.add(etiqueta2,BorderLayout.CENTER);
        menuJugadorDerecha.add(cartasJugadorDerecha, BorderLayout.SOUTH);
        panelPrincipal.add(menuJugadorDerecha,BorderLayout.EAST);

        //jugador de la izquierda
        JPanel menuJugadorIzquierda = new JPanel();
        menuJugadorIzquierda.setLayout(new BorderLayout());

        JButton botonIzquierda1 = new JButton("carta1");
        botonIzquierda1.addActionListener(this);
        botonIzquierda1.setMinimumSize(new Dimension(20,20));
        botonIzquierda1.setMaximumSize(new Dimension(40,60));
        botonIzquierda1.setPreferredSize(new Dimension(40,60));

        JButton botonIzquierda2 = new JButton("carta2");
        botonIzquierda2.addActionListener(this);
        botonIzquierda2.setMinimumSize(new Dimension(20,20));
        botonIzquierda2.setMaximumSize(new Dimension(40,60));
        botonIzquierda2.setPreferredSize(new Dimension(40,60));
        JButton botonIzquierda3 = new JButton("carta3");
        botonIzquierda3.addActionListener(this);
        botonIzquierda3.setMinimumSize(new Dimension(20,20));
        botonIzquierda3.setMaximumSize(new Dimension(40,60));
        botonIzquierda3.setPreferredSize(new Dimension(40,60));
        JPanel cartasJugadorIzquierda = new JPanel();
        cartasJugadorIzquierda.setLayout(new FlowLayout());

        cartasJugadorIzquierda.add(botonIzquierda1);
        cartasJugadorIzquierda.add(botonIzquierda2);
        cartasJugadorIzquierda.add(botonIzquierda3);

        etiqueta2 = new JLabel("jugador 4",SwingConstants.CENTER);
        menuJugadorIzquierda.add(etiqueta2,BorderLayout.CENTER);
        menuJugadorIzquierda.add(cartasJugadorIzquierda, BorderLayout.SOUTH);
        panelPrincipal.add(menuJugadorIzquierda,BorderLayout.WEST);

        //jugador de arriba
        JPanel menuJugadorArriba = new JPanel();
        menuJugadorArriba.setLayout(new BorderLayout());

        JButton botonArriba1 = new JButton("carta1");
        botonArriba1.addActionListener(this);
        botonArriba1.setMinimumSize(new Dimension(20,20));
        botonArriba1.setMaximumSize(new Dimension(40,60));
        botonArriba1.setPreferredSize(new Dimension(40,60));

        JButton botonArriba2 = new JButton("carta2");
        botonArriba2.addActionListener(this);
        botonArriba2.setMinimumSize(new Dimension(20,20));
        botonArriba2.setMaximumSize(new Dimension(40,60));
        botonArriba2.setPreferredSize(new Dimension(40,60));
        JButton botonArriba3 = new JButton("carta3");
        botonArriba3.addActionListener(this);
        botonArriba3.setMinimumSize(new Dimension(20,20));
        botonArriba3.setMaximumSize(new Dimension(40,60));
        botonArriba3.setPreferredSize(new Dimension(40,60));
        JPanel cartasJugadorArriba = new JPanel();
        cartasJugadorArriba.setLayout(new FlowLayout());

        cartasJugadorArriba.add(botonArriba1);
        cartasJugadorArriba.add(botonArriba2);
        cartasJugadorArriba.add(botonArriba3);

        etiqueta2 = new JLabel("jugador 3",SwingConstants.CENTER);
        menuJugadorArriba.add(etiqueta2,BorderLayout.NORTH);
        menuJugadorArriba.add(cartasJugadorArriba, BorderLayout.CENTER);
        panelPrincipal.add(menuJugadorArriba,BorderLayout.NORTH);

        //tablero del centro
        JPanel menuTablero = new JPanel();
        menuTablero.setLayout(new BorderLayout());

        JButton mazo = new JButton("mazo");
        mazo.addActionListener(this);
        mazo.setMinimumSize(new Dimension(20,20));
        mazo.setMaximumSize(new Dimension(80,120));
        mazo.setPreferredSize(new Dimension(80,120));

        JButton bocaArriba = new JButton("cartas Boca Arriba");
        bocaArriba.addActionListener(this);
        bocaArriba.setMinimumSize(new Dimension(20,20));
        bocaArriba.setMaximumSize(new Dimension(80,120));
        bocaArriba.setPreferredSize(new Dimension(80,120));
        JButton jugada1 = new JButton("carta Jugada 1");
        //arrayList de Jbutton?
        jugada1.addActionListener(this);
        jugada1.setMinimumSize(new Dimension(20,20));
        jugada1.setMaximumSize(new Dimension(50,70));
        jugada1.setPreferredSize(new Dimension(50,70));

        JButton jugada2 = new JButton("carta Jugada 2");
        //arrayList de Jbutton?
        jugada2.addActionListener(this);
        jugada2.setMinimumSize(new Dimension(20,20));
        jugada2.setMaximumSize(new Dimension(50,70));
        jugada2.setPreferredSize(new Dimension(50,70));
        JButton jugada3 = new JButton("carta Jugada 3");
        //arrayList de Jbutton?
        jugada3.addActionListener(this);
        jugada3.setMinimumSize(new Dimension(20,20));
        jugada3.setMaximumSize(new Dimension(50,70));
        jugada3.setPreferredSize(new Dimension(50,70));

        JPanel cartasJugadas = new JPanel();
        cartasJugadas.setLayout(new FlowLayout());
        cartasJugadas.add(jugada1);
        cartasJugadas.add(jugada2);
        cartasJugadas.add(jugada3);

        JPanel mazoYbocaArriba = new JPanel();
        mazoYbocaArriba.setLayout(new FlowLayout());
        mazoYbocaArriba.add(mazo);
        mazoYbocaArriba.add(bocaArriba);

        menuTablero.add(mazoYbocaArriba, BorderLayout.EAST);
        menuTablero.add(cartasJugadas,BorderLayout.CENTER);
        panelPrincipal.add(menuTablero,BorderLayout.CENTER);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*String comando = e.getActionCommand();
        if (comando.equals("pulsame")){
            etiqueta2.setText("Pulsaste un boton");
        } else if (comando.equals("pulsame a mi")){
            etiqueta2.setText("Pulsaste otro boton");
        } else if (comando.equals("pulsa aqui")){
            etiqueta2.setText("Pulsaste un boton especial");
        }else {
            etiqueta2.setText("ERROR");
        }*/
    }
}
