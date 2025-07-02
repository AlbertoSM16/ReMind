package com.remind.back.entities;


import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data; 
import lombok.NoArgsConstructor; 
import lombok.AllArgsConstructor; 

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class PacienteTerapeuta {
    //lo pongo como PK debido a que un paciente solo tiene un terapeuta
    @Id
    @Column(name = "paciente_id")
    private Integer pacienteId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "paciente_id", referencedColumnName = "id")
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "terapeuta_id", nullable = false) 
    private Terapeuta terapeuta;
}



// paciente-> relacion(guarda los id de ambos) <- terapeuta