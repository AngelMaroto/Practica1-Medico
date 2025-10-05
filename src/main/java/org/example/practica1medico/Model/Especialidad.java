package org.example.practica1medico.Model;

public class Especialidad {
    private int id;
    private String tipo;

    public Especialidad(int id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }
/*
    @Override
    public String toString() {
        return tipo;
    }*/

}
