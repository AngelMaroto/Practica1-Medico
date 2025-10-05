package org.example.practica1medico.Model;

public class Paciente {
    private String emailID;
    private String dni;
    private String nombre;
    private String direccion;
    private String telefono;

    public Paciente(String emailID, String dni, String nombre, String direccion, String telefono) {
        this.emailID = emailID;
        this.dni = dni;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }
}
