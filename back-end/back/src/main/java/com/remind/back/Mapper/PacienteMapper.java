package com.remind.back.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.remind.back.dto.PacienteInputDTO;
import com.remind.back.dto.PacienteOutputDTO;
import com.remind.back.entities.Paciente;

@Mapper(componentModel = "spring")
public interface PacienteMapper {

    Paciente PacienteInputDTOToPaciente(PacienteInputDTO paciente);

    @Mapping(source = "pacienteTerapeuta.terapeuta.id", target = "terapeutaId")
    PacienteOutputDTO PacienteToPacienteOutputDTO(Paciente paciente);
}
