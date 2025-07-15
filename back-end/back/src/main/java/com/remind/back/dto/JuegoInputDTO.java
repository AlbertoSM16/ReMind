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


    @NotNull(message = "El ID del terapeuta no puede ser nulo")
    private Integer paciente_id;
    @NotBlank(message = "Las instrucciones del juego no pueden estar vacías")
    private String instrucciones;

    @NotBlank (message = "")
    private Date fechaRealizacion;
    


    
}
