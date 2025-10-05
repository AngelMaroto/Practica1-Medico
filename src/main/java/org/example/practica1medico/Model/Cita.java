package org.example.practica1medico.Model;

import java.time.LocalDate;

public class Cita {
    private int NCita;
    private LocalDate fecha;
    private String especialidad;

    public Cita(LocalDate fecha, String especialidad) {
        this.NCita = NCita;
        this.fecha = fecha;
        this.especialidad = especialidad;
    }

    public int getNCita() {
        return NCita;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getEspecialidad() {
        return especialidad;
    }
}
