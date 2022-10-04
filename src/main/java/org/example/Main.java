package org.example;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {


        Modelo m = new Modelo();
        Vista v = new Vista(m);
        Controlador c = new Controlador(m,v);
        c.ejecutar();
        m.consulta("Pedro Lima");

    }

}