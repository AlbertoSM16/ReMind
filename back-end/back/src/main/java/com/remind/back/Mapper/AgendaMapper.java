package com.remind.back.Mapper;


import com.remind.back.dto.AgendaInputDTO;
import com.remind.back.dto.AgendaOutputDTO;
import com.remind.back.entities.Agenda;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AgendaMapper {
    Agenda agendaInputDTOToAgenda(AgendaInputDTO agendaInputDTO);

    AgendaOutputDTO agendaToAgendaOutputDTO(Agenda agenda);
}
