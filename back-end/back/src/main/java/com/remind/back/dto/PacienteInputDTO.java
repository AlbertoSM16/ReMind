package com.remind.back.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// esta clase sirve para crear pacientes
public class PacienteInputDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    private String usuario;

    private String contrasenia;

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;

    @NotBlank(message = "La enfermedad no puede estar vacía")
    private String enfermedad;

    @NotNull(message = "La edad no puede estar vacía")
    private Integer edad;

    @NotBlank(message = "El nombre del responsable no puede estar vacío")
    private String nombreResponsable;

    @NotNull(message = "La fecha de nacimiento no puede estar vacía")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;

    @NotNull(message = "El ID del terapeuta no puede ser nulo")
    private Integer terapeuta_id;
}