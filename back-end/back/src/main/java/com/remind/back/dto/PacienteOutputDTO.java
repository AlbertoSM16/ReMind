package com.remind.back.dto;

import lombok.Data; 
import lombok.NoArgsConstructor; 
import lombok.AllArgsConstructor; 

@Data 
@NoArgsConstructor 
@AllArgsConstructor 

//para mostrar solo los datos necesarios
public class PacienteOutputDTO {

    private String nombre;

    private String apellido;

    private String email;

    private String telefono;

    private String enfermedad;

    private String edad;

    private String nombreResponsable;

    private Integer terapeutaId; 
}
