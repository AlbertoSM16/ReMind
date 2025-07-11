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

    // Mapeo para terapeutaId: Asume que Paciente tiene un getter como getPacienteTerapeuta()
    // y que PacienteTerapeuta tiene getTerapeuta() y Terapeuta tiene getId().
    // Si la relación Paciente -> PacienteTerapeuta es @OneToOne, el getter sería algo como getPacienteTerapeuta().
    // Si un Paciente puede tener múltiples PacienteTerapeuta, la lógica de mapeo sería más compleja
    // (ej. tomar el ID del primer terapeuta en una lista, o devolver una lista de IDs).
    @Mapping(source = "pacienteTerapeuta.terapeuta.id", target = "terapeutaId") // <--- AÑADIDO ESTE MAPEADO
    PacienteOutputDTO PacienteToPacienteOutputDTO(Paciente paciente);

    // ALTERNATIVA (Más robusta para nulos, si PacienteTerapeuta puede ser null para un Paciente):
    // @Mapping(target = "terapeutaId", expression = "java(paciente.getPacienteTerapeuta() != null && paciente.getPacienteTerapeuta().getTerapeuta() != null ? paciente.getPacienteTerapeuta().getTerapeuta().getId() : null)")
    // PacienteOutputDTO PacienteToPacienteOutputDTO(Paciente paciente);

}