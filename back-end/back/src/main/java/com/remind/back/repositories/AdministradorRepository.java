package com.remind.back.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.remind.back.entities.Administrador;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
        
}
