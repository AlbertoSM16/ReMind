package com.remind.back.entities;

import com.remind.back.entities.Paciente;
import com.remind.back.entities.Terapeuta;

import lombok.Data; 
import lombok.NoArgsConstructor; 
import lombok.AllArgsConstructor; 

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class PacienteTerapeuta {
    
    private Paciente paciente;
    private Terapeuta terapeuta;

}
