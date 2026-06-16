package com.remind.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remind.back.entities.AgendaTerapeuta;

public interface AgendaTerapeutaRepository  extends JpaRepository<AgendaTerapeuta, Integer> {
    
    void deleteByAgendaId(Integer agendaId);
    void deleteByTerapeutaId(Integer terapeutaId);

    boolean existsByAgendaId(Integer agendaId);
    
    boolean existsByTerapeutaId(Integer terapeutaId);

    boolean existsByAgendaIdAndTerapeutaId(Integer agendaId, Integer terapeutaId);

    AgendaTerapeuta findByAgendaIdAndTerapeutaId(Integer agendaId, Integer terapeutaId);
}   
