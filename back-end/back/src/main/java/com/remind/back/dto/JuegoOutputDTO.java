package com.remind.back.dto;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor; 
import lombok.AllArgsConstructor; 

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class JuegoOutputDTO {

    private Integer paciente_id;

    private Integer agenda_id;

    private Date fechaAsignación = new Date(System.currentTimeMillis());
    
    private Date fechaRealizacion = null;
    
    private String codigo;
    
    private String nombre;

    private int dificultad;


}
