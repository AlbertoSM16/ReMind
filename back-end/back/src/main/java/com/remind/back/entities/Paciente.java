package com.remind.back.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true) // Include superclass fields in equals and hashCode
@DiscriminatorValue("PACIENTE")
@Entity
@Data
@NoArgsConstructor
public class Paciente extends Usuario {

    @NotBlank
    private String enfermedad;

    @NotBlank
    private String edad;

    @NotBlank
    private String nombreResponsable;
    
//añadir las relaciones
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    private List<PacienteJuego> juegosRealizados;

    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL)
    private Agenda agenda;

    public Paciente(int id, @NotBlank String nombre, @NotBlank String apellido, @NotBlank String email,
            @NotBlank String contraseña, @NotBlank String telefono, @NotBlank String enfermedad, @NotBlank String edad,
            @NotBlank String nombreResponsable, Terapeuta terapeuta) {
        super(id, nombre, apellido, email, contraseña, telefono);
        this.enfermedad = enfermedad;
        this.edad = edad;
        this.nombreResponsable = nombreResponsable;
        setTipo(TipoUsuario.PACIENTE);
    }
}