package com.remind.back.services;

import java.util.List;


import com.remind.back.entities.Paciente;

public interface PacienteService {
    
    Paciente createPaciente(Paciente paciente);
    Paciente getPacienteById(Integer id);
    List<Paciente> getAllPacientes();
    void deletePaciente(Integer id);
    void updatePaciente(Integer id);
}
