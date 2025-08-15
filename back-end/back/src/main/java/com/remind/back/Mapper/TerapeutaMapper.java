package com.remind.back.Mapper;
import org.mapstruct.Mapper;

import com.remind.back.dto.TerapeutaInputDTO;
import com.remind.back.dto.TerapeutaOutputDTO;
import com.remind.back.entities.Terapeuta;

@Mapper(componentModel = "spring")
public interface TerapeutaMapper {

    Terapeuta TerapeutaInputDTOToTerapeuta(TerapeutaInputDTO terapeuta);
    TerapeutaOutputDTO TerapeutaToTerapeutaOutputDTO(Terapeuta terapeuta);

    
    
}
