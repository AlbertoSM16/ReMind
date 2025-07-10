package com.remind.back.entities;


import java.util.Date;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@DiscriminatorValue("TERAPEUTA")
@Entity
@Data
@NoArgsConstructor
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
    
    private String usuario;

    String especialidad;
    
}