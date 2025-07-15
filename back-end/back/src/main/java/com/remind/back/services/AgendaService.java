package com.remind.back.services;

import com.remind.back.dto.AgendaInputDTO;
import com.remind.back.dto.AgendaOutputDTO;
import com.remind.back.entities.Agenda;

import java.util.List;


public interface AgendaService {

    AgendaOutputDTO createAgenda(AgendaInputDTO agendaInputDTO);

    AgendaOutputDTO getAgendaById(Integer id);

    List<AgendaOutputDTO> getAllAgendas(int page, int size);

    void deleteAgenda(Integer id);

    AgendaOutputDTO updateAgenda(Integer id, AgendaInputDTO agendaInputDTO);
}
