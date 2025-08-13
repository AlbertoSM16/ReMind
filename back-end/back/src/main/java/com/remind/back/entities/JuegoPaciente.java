package com.remind.back.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "juego_paciente")
public class JuegoPaciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juego_id")
    private Juego juego;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_asignacion")
    private Date fechaAsignacion = new Date();

    @NotNull
    private int dificultad;
}
