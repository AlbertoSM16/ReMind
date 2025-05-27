package com.remind.back.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@DiscriminatorValue("TERAPEUTA")
@Entity
public class Terapeuta extends Usuario {

    @OneToMany(mappedBy = "terapeuta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Agenda> agendas = new ArrayList<>();
    
    @OneToMany(mappedBy = "terapeuta", cascade = CascadeType.ALL)
    private List<Paciente> pacientes;

    public Terapeuta() {
        super();
    }

    public Terapeuta(int id, String nombre, String apellido, String email, String contraseña, String telefono,
            List<Paciente> pacientes) {
        super(id, nombre, apellido, email, contraseña, telefono);
        this.pacientes = pacientes;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public List<Agenda> getAgendas() {
        return agendas;
    }

    public void setAgendas(List<Agenda> agendas) {
        this.agendas = agendas;
    }

}
