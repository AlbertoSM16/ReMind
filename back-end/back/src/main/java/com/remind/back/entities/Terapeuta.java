package com.remind.back.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("TERAPEUTA")
@Entity
@Data
@NoArgsConstructor
public class Terapeuta extends Usuario {

    @OneToMany(mappedBy = "terapeuta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Agenda> agendas = new ArrayList<>(); // Keep initialization for empty list

    @OneToMany(mappedBy = "terapeuta", cascade = CascadeType.ALL)
    private List<Paciente> pacientes;

    // Custom constructor needed for specific initialization if AllArgsConstructor isn't sufficient
    public Terapeuta(int id, String nombre, String apellido, String email, String contraseña, String telefono,
            List<Paciente> pacientes) {
        super(id, nombre, apellido, email, contraseña, telefono);
        this.pacientes = pacientes;
    }
}