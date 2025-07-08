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


@Entity 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteAgenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Integer paciente;

    @ManyToOne
    @JoinColumn(name = "agenda_id")
    private Integer Agenda;

    private LocalDateTime fecha = LocalDateTime.now();
}