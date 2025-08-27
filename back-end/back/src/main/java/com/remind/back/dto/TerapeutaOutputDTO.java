package com.remind.back.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// para mostrar solo los datos necesarios
public class TerapeutaOutputDTO {
    
    private int id;

    private String nombre;

    private String apellido;

    private String email;

    private String telefono;

    private String usuario;

    private String contrasena;

    private Date fechaNacimiento;

    private String especialidad;

    private List<Integer> pacientesId;

}
