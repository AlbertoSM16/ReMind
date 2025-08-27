package com.remind.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministradorOutputDTO {

    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
}