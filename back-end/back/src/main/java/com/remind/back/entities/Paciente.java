package com.remind.back.entities;


import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("PACIENTE")
@Entity
@Data
@NoArgsConstructor
public class Paciente extends Usuario {

    @NotBlank
    private String enfermedad;

    @NotBlank
    private Integer edad;

    @NotBlank
    private String nombreResponsable;
    
    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL)
    private Agenda agenda;

    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private PacienteTerapeuta pacienteTerapeuta;

    public Paciente(int id, @NotBlank String nombre, @NotBlank String apellido, @NotBlank String email,
            @NotBlank String contraseña, @NotBlank String telefono, @NotBlank String enfermedad, @NotBlank Integer edad,
            @NotBlank String nombreResponsable) {
        super(id, nombre, apellido, email, contraseña, telefono);
        this.enfermedad = enfermedad;
        this.edad = edad;
        this.nombreResponsable = nombreResponsable;
        setTipo(TipoUsuario.PACIENTE);
    }
}