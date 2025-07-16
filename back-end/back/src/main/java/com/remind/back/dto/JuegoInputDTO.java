package com.remind.back.dto;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor; 
import lombok.AllArgsConstructor; 


@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class JuegoInputDTO {

//esta clase es para asociar un juego a un paciente y agenda 

    @NotNull(message = "El ID del terapeuta no puede ser nulo")
    private Integer paciente_id;
    @NotNull(message = "El ID de la agenda no puede ser nulo")
    private Integer agenda_id;

    @NotBlank (message = "la fecha de asignación no puede ser nula")
    private Date fechaAsignación = new Date(System.currentTimeMillis());
    
    private Date fechaRealizacion = null;

    
}
