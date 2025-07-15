package com.remind.back.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data; 
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class AgendaInputDTO {
    
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotNull(message = "El ID del paciente no puede ser nulo")
    private Integer paciente_id;

    @NotNull(message = "El ID del terapeuta no puede ser nulo")
    private Integer terapeuta_id;
    
}
