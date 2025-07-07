package com.remind.back.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remind.back.entities.PacienteTerapeuta;

public interface PacienteTerapeutaRepository extends JpaRepository<PacienteTerapeuta, Integer> {
    
    Optional<PacienteTerapeuta> findByPacienteId(Integer pacienteId);

}
