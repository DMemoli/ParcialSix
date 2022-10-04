package org.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Controlador {
    Modelo m;
    Vista v;

    public Controlador(Modelo m, Vista v){
        this.m = m;
        this.v = v;
    }
    public void ejecutar(){
        v.botonDeterminar(new CodificarListener());
    }
    private class CodificarListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {

            try {
                v.getGrado();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

