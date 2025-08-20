package com.remind.back.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteSeguimientoDTO {
    private int pacienteId;
    private String nombre;
    private long juegosCompletados;
    private long juegosTotales;
    private List<JuegoAsignadoDTO> juegos;

}