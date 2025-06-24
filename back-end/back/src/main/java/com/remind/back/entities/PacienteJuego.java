package com.remind.back.entities;

import java.time.LocalDateTime;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity; // Add @Entity annotation
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Entity // Make sure it's an entity for JPA
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteJuego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "juego_id")
    private Juego juego;

    private Boolean realizado;

    private LocalDateTime fecha = LocalDateTime.now(); // Keep initialization here if it's a default value
}