package com.remind.back.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remind.back.entities.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    Optional<Paciente> findByUsuario(String usuario);

    List<Paciente> findByTerapeutaId(Integer terapeutaId);
}
