package com.remind.back.Mapper;

import org.mapstruct.Mapper;

import com.remind.back.dto.PacienteInputDTO;
import com.remind.back.dto.PacienteOutputDTO;
import com.remind.back.entities.Paciente;

@Mapper(componentModel = "spring")
public interface PacienteMapper {

    Paciente PacienteInputDTOToPaciente(PacienteInputDTO paciente);
    PacienteOutputDTO PacienteToPacienteOutputDTO(Paciente paciente);
}


