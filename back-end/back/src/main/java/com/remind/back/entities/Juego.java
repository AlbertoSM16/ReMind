package com.remind.back.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data; // Import Lombok's Data annotation
import lombok.NoArgsConstructor; // Import Lombok's NoArgsConstructor
import lombok.AllArgsConstructor; // Import Lombok's AllArgsConstructor


@Entity
@Data // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class Juego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String instrucciones;

    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL)
    private List<PacienteJuego> historialJugadores;
}