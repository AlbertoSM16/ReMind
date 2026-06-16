package com.remind.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.remind.back.entities.Juego;

public interface JuegoRepository extends JpaRepository<Juego, Integer> {
    // The @Repository annotation is not needed for Spring Data JPA repositories
}
