package com.remind.back.entities;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

public class Paciente extends Usuario{

    @NotBlank
    private String enfermedad;

    @NotBlank
    private String edad;
    
    @NotBlank
    private String nombreResponsable;


    @ManyToOne
    @JoinColumn(name = "terapeuta_id")
    private Terapeuta terapeuta;

    public Paciente() {
        super();
    }
    
    public Paciente(int id, @NotBlank String nombre, @NotBlank String apellido, @NotBlank String email,
            @NotBlank String contraseña, @NotBlank String telefono, @NotBlank String enfermedad, @NotBlank String edad,
            @NotBlank String nombreResponsable, Terapeuta terapeuta) {
        super(id, nombre, apellido, email, contraseña, telefono);
        this.enfermedad = enfermedad;
        this.edad = edad;
        this.nombreResponsable = nombreResponsable;
        this.terapeuta = terapeuta;
    }

    public String getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(String enfermedad) {
        this.enfermedad = enfermedad;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }
  
    public Terapeuta getTerapeuta() {
        return terapeuta;
    }

    public void setTerapeuta(Terapeuta terapeuta) {
        this.terapeuta = terapeuta;
    }

}
