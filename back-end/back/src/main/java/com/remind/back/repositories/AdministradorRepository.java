package com.remind.back.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.remind.back.entities.Administrador;
import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    Optional<Administrador> findByEmail(String email);
}
