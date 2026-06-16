package com.remind.back.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remind.back.entities.JuegoAgenda;

public interface JuegoAgendaRepository extends JpaRepository<JuegoAgenda, Long> {
    List<JuegoAgenda> findByAgendaId(Integer agendaId);

    Optional<JuegoAgenda> findByAgendaIdAndJuegoId(Integer agendaId, Integer juegoId);

    void deleteByAgendaId(Integer agendaId);

}
