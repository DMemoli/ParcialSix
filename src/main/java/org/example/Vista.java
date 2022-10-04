package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class Vista {
    private Modelo m;
    private JButton boton;
    private JFrame f;
    private JTextField nombre1, nombre2, grado;
    private JLabel textoNombre1, textoNombre2, textoGrado;


    public Vista(Modelo m){
        this.m = m;
        boton = new JButton("Determinar");
        nombre1 = new JTextField("Pedro Lima",10);
        nombre2 = new JTextField("Elsa Payo",10);
        grado = new JTextField(10);
        textoNombre1 = new JLabel("Nombre:");
        textoNombre2 = new JLabel("Nombre:");
        textoGrado = new JLabel("Grado de separacion:");


        f = new JFrame("Seis grados");


        f.getContentPane().setBackground(Color.BLUE);
        f.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));
        f.getContentPane().add(textoNombre1);
        f.getContentPane().add(nombre1);
        f.getContentPane().add(textoNombre2);
        f.getContentPane().add(nombre2);
        f.getContentPane().add(boton);
        f.getContentPane().add(textoGrado);

        f.getContentPane().add(grado);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 270);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

    }
    public void botonDeterminar(ActionListener av) {
        boton.addActionListener(av);
    }

    public void getGrado() throws SQLException {

        String nom1 = nombre1.getText();
        String nom2 = nombre2.getText();
        int grad = 1;
        boolean encontrado = false;
        Set resultados = new TreeSet<String>();
        resultados = m.consulta(nom1);
        System.out.println(m.consulta(nom1));

        while(!encontrado){
            Iterator<String> nombres = resultados.iterator();
            while(nombres.hasNext()){

                if(nom2.equalsIgnoreCase(nombres.next())){
                    System.out.println("encontrado");
                    encontrado = true;
                    break;
                }
            }
            if(!encontrado){
                System.out.println("entro if");
                grad++;
                Set masResultados = new TreeSet<String>();
                Iterator<String> agregarNombres = resultados.iterator();
                while(agregarNombres.hasNext()){

                    masResultados.addAll(m.consulta(agregarNombres.next()));

                    System.out.println(resultados);
                }
                resultados.addAll(masResultados);
            }
        }


        grado.setText(String.valueOf(grad));

    }

}
