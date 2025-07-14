package com.remind.back.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data; 
import lombok.NoArgsConstructor; 
import lombok.AllArgsConstructor; 

@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Table(name = "paciente_terapeuta")
public class PacienteTerapeuta {

    @Id
    private Integer pacienteId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Esto le dice a JPA que la PK de esta entidad (pacienteId)
            // es también una FK que apunta a la PK de Paciente.
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terapeuta_id", nullable = false) 
    private Terapeuta terapeuta;
}