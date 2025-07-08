package com.remind.back.entities;


import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data; 
import lombok.NoArgsConstructor; 
import lombok.AllArgsConstructor; 

@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class PacienteJuego {
    
    @EmbeddedId
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pacienteId") 
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("juegoId") 
    @JoinColumn(name = "juego_id")
    private Juego juego;

    @Column(name = "fecha_realizacion")
    private Date fecha_realizacion;

    @Column(name = "completado" )
    private Boolean completado;
}
