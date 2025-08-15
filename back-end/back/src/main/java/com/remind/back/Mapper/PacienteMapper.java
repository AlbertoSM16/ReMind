package com.remind.back.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping; // Asegúrate de importar esto

import com.remind.back.dto.PacienteCreatedDTO;
import com.remind.back.dto.PacienteInputDTO;
import com.remind.back.dto.PacienteOutputDTO;
import com.remind.back.entities.Paciente;

@Mapper(componentModel = "spring")
public interface PacienteMapper {

    Paciente PacienteInputDTOToPaciente(PacienteInputDTO paciente);

    @Mapping(source = "pacienteTerapeuta.terapeuta.id", target = "terapeuta_id") 
    PacienteOutputDTO PacienteToPacienteOutputDTO(Paciente paciente);

    
    PacienteCreatedDTO PacienteOutputDTOToPacienteCreatedDTO(PacienteOutputDTO paciente);


}