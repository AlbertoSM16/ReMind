package com.remind.back.services;

import java.util.List;

import com.remind.back.dto.PacienteInputDTO;
import com.remind.back.dto.PacienteOutputDTO;


public interface PacienteService {

    PacienteOutputDTO createPaciente(PacienteInputDTO pacienteInputDTO);

    PacienteOutputDTO getPacienteById(Integer id);

    List<PacienteOutputDTO> getAllPacientes(int page,int size);

    void deletePaciente(Integer id);

    PacienteOutputDTO updatePaciente(Integer id, PacienteInputDTO pacienteDTO);
}
