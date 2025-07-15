package com.remind.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministradorInputDTO {
    
    private String nombre;
    private String apellido;
    private String email;
    private String contrasenia;
    private String telefono;
    private Date fechaNacimiento;
}