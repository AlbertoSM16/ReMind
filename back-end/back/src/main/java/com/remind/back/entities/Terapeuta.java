package com.remind.back.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "terapeuta")
public class Terapeuta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String email;

    @NotBlank
    private String contrasena;

    @NotBlank
    private String telefono;

    @NotNull
    private Date fechaNacimiento;

    @Column(unique = true)
    private String usuario;

    @NotBlank
    String especialidad;

    private TipoUsuario rol = TipoUsuario.TERAPEUTA;

    @OneToMany(mappedBy = "terapeuta", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PacienteTerapeuta> pacienteTerapeutas;

    @OneToMany(mappedBy = "terapeuta", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AgendaTerapeuta> agendaTerapeutas;
}