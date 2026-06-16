package com.remind.back.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignJuegoDTO {

    @NotNull
    private Integer juegoId;

    @NotNull
    @Min(1)
    @Max(3)
    private Integer dificultad;
}