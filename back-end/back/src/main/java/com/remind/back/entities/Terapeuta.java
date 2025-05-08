package com.remind.back.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

public class Terapeuta extends Usuario {

    public Terapeuta() {
        super();
    }

  
    @OneToMany(mappedBy = "terapeuta", cascade = CascadeType.ALL)
    private List<Paciente> pacientes;

    public Terapeuta(int id, String nombre, String apellido, String email, String contraseña, String telefono, List<Paciente> pacientes) {
        super(id, nombre, apellido, email, contraseña, telefono);
        this.pacientes = pacientes;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

}

