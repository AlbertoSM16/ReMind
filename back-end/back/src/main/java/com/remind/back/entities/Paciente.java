package com.remind.back.entities;

import java.util.Date;

import jakarta.persistence.Column; 

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

 
    @NotBlank
    private String contrasenia;

    @NotBlank
    private String telefono;

    @NotNull
    private Date fechaNacimiento;
    
    @Column(unique = true) 
    private String usuario;

    @NotBlank
    private String enfermedad;

    @NotNull
    private Integer edad;

    @NotBlank
    private String nombreResponsable;
    
    @Enumerated(EnumType.STRING)
    private TipoUsuario rol = TipoUsuario.PACIENTE;
    
    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PacienteTerapeuta pacienteTerapeuta;


}