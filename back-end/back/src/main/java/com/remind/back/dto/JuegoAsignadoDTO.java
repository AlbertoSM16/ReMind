package com.remind.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JuegoAsignadoDTO {
    private int id;
    private String nombre;
    private String codigo;
    private int dificultad;
}