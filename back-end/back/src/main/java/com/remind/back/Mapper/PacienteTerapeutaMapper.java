package com.remind.back.Mapper;


import org.mapstruct.Mapper;

import com.remind.back.dto.PacienteTerapeutaInputDTO;
import com.remind.back.entities.PacienteTerapeuta;

@Mapper(componentModel = "spring")
public interface PacienteTerapeutaMapper {
    PacienteTerapeuta PacienteTerapeutaInputDTOToPacienteTerapeuta(PacienteTerapeutaInputDTO pacienteTerapeutaInputDTO);

}
