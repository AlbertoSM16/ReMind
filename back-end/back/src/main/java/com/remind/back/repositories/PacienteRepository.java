package com.remind.back.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.remind.back.entities.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
   

}
