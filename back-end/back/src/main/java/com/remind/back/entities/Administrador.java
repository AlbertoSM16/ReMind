package com.remind.back.entities;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Administrador {
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
    @Column(unique = true) 
    private String usuario;

    private TipoUsuario rol = TipoUsuario.ADMINISTRADOR;


}