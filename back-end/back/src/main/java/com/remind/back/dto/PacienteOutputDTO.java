package com.remind.back.dto;

import lombok.Data; 
import lombok.NoArgsConstructor;

import java.util.Date;

import lombok.AllArgsConstructor; 

@Data 
@NoArgsConstructor 
@AllArgsConstructor 

//para mostrar solo los datos necesarios
public class PacienteOutputDTO {

    private String nombre;

    private String apellido;

    private String email;

    private String telefono;

    private String enfermedad;

    private String usuario;

    private Integer edad;

    private String nombreResponsable;

    private Date fechaNacimiento;

    private Integer terapeuta_id; 
}
