package org.example.practica1medico.UTIL;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL="jdbc:mysql://localhost:3306/CentroMedico?serverTimezone=Europe/Madrid";
    private static final String USER="root";
    private static final String PASSWORD="toor";

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(URL,USER,PASSWORD);
        }catch (SQLException e){
            System.out.println("Error de conexión"+e.getMessage());
            return null;
        }
    }
}
