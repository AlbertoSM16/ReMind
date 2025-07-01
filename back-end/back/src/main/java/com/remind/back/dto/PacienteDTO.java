package com.remind.back.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor 
public class PacienteDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String contraseña;

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;

    @NotBlank(message = "La enfermedad no puede estar vacía")
    private String enfermedad;

    @NotBlank(message = "La edad no puede estar vacía")
    private String edad;

    @NotBlank(message = "El nombre del responsable no puede estar vacío")
    private String nombreResponsable;

    @NotNull(message = "El ID del terapeuta no puede ser nulo")
    private Integer terapeutaId; 
}