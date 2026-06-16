package com.remind.back.services;

import com.remind.back.dto.AgendaInputDTO;
import com.remind.back.dto.AgendaOutputDTO;
import com.remind.back.dto.JuegoAsignadoDTO;

import java.util.List;
import java.util.Map;

public interface AgendaService {

    AgendaOutputDTO createAgenda(AgendaInputDTO agendaInputDTO);

    AgendaOutputDTO getAgendaById(Integer id);

    List<AgendaOutputDTO> getAllAgendas(int page, int size);

    void deleteAgenda(Integer id);

    void assignJuegoToAgenda(Integer agendaId, Integer juegoId, Integer dificultad);

    AgendaOutputDTO updateAgenda(Integer id, AgendaInputDTO agendaInputDTO);

    void removeJuegoFromAgenda(Integer agendaId, Integer juegoId);

    List<JuegoAsignadoDTO> getJuegosByAgendaId(Integer agendaId);

    // List<JuegoAsignadoDTO> getJuegosByPacienteId(Integer pacienteId);
    Map<String, Object> getJuegosByPacienteId(Integer pacienteId);

    void completarJuego(Integer agendaId, Integer juegoId);

}
