package com.remind.back.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data; 
import lombok.NoArgsConstructor; 
import lombok.AllArgsConstructor; 

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class PacienteTerapeutaInputDTO {

    @NotBlank(message = "El paciente no puede estar vacío")
    private Integer pacienteId;
    @NotBlank(message = "El terapeuta no puede estar vacío")
    private Integer terapeutaId;

}
