package org.example.practica1medico.UTIL;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {


    private static Connection conn = null;

    public static Connection getConnection() {
        try {

            if (conn == null || conn.isClosed()) {
                Properties properties = new Properties();
                try (FileInputStream fis = new FileInputStream("src/main/resources/configuration/database.properties")) {
                    properties.load(fis);
                }

                String host = properties.getProperty("host");
                String port = properties.getProperty("port");
                String name = properties.getProperty("name");
                String username = properties.getProperty("username");
                String password = properties.getProperty("password");

                String url = "jdbc:mysql://" + host + ":" + port + "/" + name + "?serverTimezone=UTC";

                conn = DriverManager.getConnection(url, username, password);
                System.out.println("Conexión establecida con la base de datos: " + name);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Archivo de propiedades no encontrado");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error leyendo archivo de propiedades");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error conectando a la base de datos");
        }

        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    System.out.println("Conexión cerrada.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conn = null;
            }
        }
    }
}
