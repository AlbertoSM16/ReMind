// albertosm16/remind/ReMind-backend/back-end/back/src/main/java/com/remind/back/Mapper/PacienteMapper.java
package com.remind.back.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping; // Asegúrate de importar esto

import com.remind.back.dto.PacienteInputDTO;
import com.remind.back.dto.PacienteOutputDTO;
import com.remind.back.entities.Paciente;
// import com.remind.back.entities.PacienteTerapeuta; // Puede que necesites importar esta entidad si usas expresiones Java complejas

@Mapper(componentModel = "spring")
public interface PacienteMapper {

    Paciente PacienteInputDTOToPaciente(PacienteInputDTO paciente);

    @Mapping(source = "pacienteTerapeuta.terapeuta.id", target = "terapeuta_id") // <--- AÑADIDO ESTE MAPEADO
    PacienteOutputDTO PacienteToPacienteOutputDTO(Paciente paciente);

}