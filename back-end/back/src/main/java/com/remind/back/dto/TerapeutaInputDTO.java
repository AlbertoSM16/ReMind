package com.remind.back.dto;


import lombok.Data; 
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import com.remind.back.entities.Agenda;
import com.remind.back.entities.Paciente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor; 

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class TerapeutaInputDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    private String usuario;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String contrasenia;

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;

    @NotNull(message = "La especialidad no debe ser nula")
    private String especialidad; 
    
    @NotNull(message = "La fecha de nacimiento no puede estar vacía")
    private Date fechaNacimiento;

    private List<Paciente> pacientes = null;

    private List<Agenda> agendas = null;
}
