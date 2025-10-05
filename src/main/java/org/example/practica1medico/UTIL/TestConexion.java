package org.example.practica1medico.UTIL;

import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("¡Conexión exitosa a la base de datos!");
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No se pudo conectar a la base de datos.");
        }
    }
}
