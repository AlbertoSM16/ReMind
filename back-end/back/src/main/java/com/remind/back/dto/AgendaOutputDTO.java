package com.remind.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data; 
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class AgendaOutputDTO {
    
    private String nombre; 
    
    private String pacienteNombre;

    private String terapeutaNombre;

}
