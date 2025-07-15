package com.remind.back.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remind.back.entities.PacienteAgenda;

public interface PacienteAgendaRepository extends JpaRepository<PacienteAgenda, Integer> {
    
    
    List<PacienteAgenda> findByPacienteId(Integer pacienteId);

}
