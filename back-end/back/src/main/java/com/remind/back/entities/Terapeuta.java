package com.remind.back.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String contrasenia;

    @NotBlank
    private String telefono;

    @NotNull
    private Date fechaNacimiento;
    @NotBlank
    private String usuario;

    @NotBlank
    String especialidad;

    private TipoUsuario rol = TipoUsuario.TERAPEUTA;

}