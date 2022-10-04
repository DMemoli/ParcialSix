package org.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.Iterator;
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

    public String getGrado(String nom1, String nom2) throws SQLException {
        int grad = 1; //inicializo grado en 1 ya que siempre realiza una busqueda
        boolean encontrado = false; //bandera para salir de la busqueda
        Set resultados = consulta(nom1); //Consulto en NOMBRE 1 y lo guardo en una colleccion de la clase SetTree con datos de tipo String
        System.out.println(consulta(nom1));// Uso este tipo de colleccion ya que me permite solo un campo con el mismo valor, no se repiten valores

        while(!encontrado){
            Iterator<String> nombres = resultados.iterator(); //Armo un iteretor para recorrer la coleccion, es como un for each.
            while(nombres.hasNext()){//cheque que haya un campo siguiete para iterar

                if(nom2.equalsIgnoreCase(nombres.next())){//verifica que el NOMBRE 2 este dentro de la coleccion
                    System.out.println("encontrado");
                    encontrado = true;// Si esta pasa la bandera a true y sale
                    break;
                }
            }
            if(!encontrado){// si termina de iterar y no encuentra el NOMBRE 2 en la coleccion
                            //vuelve a recorrer la coleccion pero consultando por cada nombre los nombres cercanos y los suma a la coleccion.

                grad++;//aumento el grado en 1
                Set masResultados = new TreeSet<String>();
                Iterator<String> agregarNombres = resultados.iterator();//Itero la coleccion
                while(agregarNombres.hasNext()){

                    masResultados.addAll(consulta(agregarNombres.next()));//Consulto el nombre y agrego los resultados a una coleccion de nuevos resultados

                    System.out.println(resultados);
                }
                resultados.addAll(masResultados);//Agrego los nuevos resultados a los primeros resultados.
            }
        }

        return String.valueOf(grad);
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
