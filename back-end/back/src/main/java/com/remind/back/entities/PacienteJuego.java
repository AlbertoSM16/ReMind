package com.remind.back.entities;


import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data; 
import lombok.NoArgsConstructor; 
import lombok.AllArgsConstructor; 

@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class PacienteJuego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juego_id")
    private Juego juego;

    @Column(name = "fecha_realizacion")
    private Date fecha_realizacion;

    @Column(name = "completado" )
    private Boolean completado;
}

