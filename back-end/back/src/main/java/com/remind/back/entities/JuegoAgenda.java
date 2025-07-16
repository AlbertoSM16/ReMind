package com.remind.back.entities;

import java.sql.Date;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Entity; 
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;



@Entity 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JuegoAgenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "juego_id")
    private Juego juego;

    @ManyToOne
    @JoinColumn(name = "agenda_id")
    private Agenda agenda;

    @NotBlank
    private Date fechaAsignacion = new Date(System.currentTimeMillis());
    
    @NotBlank
    private Date fechaRealizacion;

    @NotBlank
    private Boolean realizado = false;

}
