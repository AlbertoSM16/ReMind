package com.remind.back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data; 
import lombok.NoArgsConstructor; 
import lombok.AllArgsConstructor; 

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class PacienteTerapeutaInputDTO {

    @NotNull(message = "El paciente no puede estar vacío")
    private Integer pacienteId;
    @NotNull(message = "El terapeuta no puede estar vacío")
    private Integer terapeutaId;

}
