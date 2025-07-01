package com.remind.back.services;

import java.util.List;
import java.util.Optional;

import com.remind.back.dto.PacienteDTO;
import com.remind.back.entities.Paciente;

public interface PacienteService {

    Paciente createPaciente(PacienteDTO pacienteDTO);

    Optional<Paciente> getPacienteById(Integer id);

    List<Paciente> getAllPacientes();

    void deletePaciente(Integer id);

    void updatePaciente(Integer id, PacienteDTO pacienteDTO);
}
