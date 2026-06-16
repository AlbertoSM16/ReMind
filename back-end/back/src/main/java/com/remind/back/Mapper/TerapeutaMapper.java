package com.remind.back.Mapper;

import org.mapstruct.Mapper;

import com.remind.back.dto.TerapeutaCreatedDTO;
import com.remind.back.dto.TerapeutaInputDTO;
import com.remind.back.dto.TerapeutaOutputDTO;
import com.remind.back.entities.Terapeuta;

@Mapper(componentModel = "spring")
public interface TerapeutaMapper {

    Terapeuta toTerapeuta(TerapeutaInputDTO terapeutaInputDTO);

    TerapeutaOutputDTO toDTO(Terapeuta terapeuta);

    TerapeutaCreatedDTO toCreatedDTO(TerapeutaOutputDTO terapeuta);

}
