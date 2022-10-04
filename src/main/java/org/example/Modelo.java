package org.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;


public class Modelo {
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String ip = "localhost";
    private String bd = "seisgrados";
    private String prefijoConexion = "jdbc:mysql://";
    private String usr = "";
    private String psw = "";
    private Connection connection;
    private ActionListener listener;

    public Modelo() {
        listener = null;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            reportException(e.getMessage());
        }

    }


    public Set consulta(String nombre) throws SQLException {
        Set resultados = new TreeSet<String>();
        try {
            connection = obtenerConexion();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM seisgrados.conocidos where \""+nombre+"\" in (Nombre, Conoce);");
            while (rs.next()) {
                if(!Objects.equals(rs.getString(1), nombre)){
                    resultados.add(rs.getString(1));
                }
                if(!Objects.equals(rs.getString(2), nombre)){
                    resultados.add(rs.getString(2));
                }
            }

            rs.close();
            statement.close();
            //System.out.println(resultados);
        } catch (SQLException e) {
        reportException(e.getMessage());
        }
        return resultados;
    }

    public void addExceptionListener(ActionListener listener) {
        this.listener = listener;
    }
    private void reportException(String exception) {
        if (listener != null) {
            ActionEvent evt = new ActionEvent(this, 0, exception);
            listener.actionPerformed(evt);
        }
    }
    private Connection obtenerConexion() {
        if (connection == null) {
            try {
                Class.forName(driver); // driver = "com.mysql.jdbc.Driver";
            } catch (ClassNotFoundException ex) {
                reportException(ex.getMessage());
            }
            try {
                connection =
                        DriverManager.getConnection(prefijoConexion + ip + "/" + bd, usr, psw); // prefijoConexion = "jdbc:mysql://";
            } catch (Exception ex) {
                reportException(ex.getMessage());
            }
            Runtime.getRuntime().addShutdownHook(new ShutDownHook());
        }
        return connection;
    }
    private class ShutDownHook extends Thread {
        public void run() {
            try {
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                reportException(ex.getMessage());
            }
        }
    }
}
