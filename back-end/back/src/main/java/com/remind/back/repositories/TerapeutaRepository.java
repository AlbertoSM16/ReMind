package com.remind.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.remind.back.entities.Terapeuta;
import java.util.Optional;
import java.util.List;


public interface TerapeutaRepository extends JpaRepository<Terapeuta, Integer> {
    
    Optional<Terapeuta> findByEmail(String email);

    Optional<Terapeuta> findById(Integer id);

    Optional<Terapeuta> findByUsuario(String usuario);
}