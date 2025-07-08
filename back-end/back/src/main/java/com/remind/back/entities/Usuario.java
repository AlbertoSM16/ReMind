package com.remind.back.entities;

import java.util.Date;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor; // This might need adjustment if you have specific constructor needs

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
// AllArgsConstructor might be problematic for abstract classes or with specific constructor logic.
// Consider generating constructors manually or using @Builder if you need more control.
// For this example, I'll keep it, but be aware of potential issues depending on usage.
@AllArgsConstructor
public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private String nombre;
    @NotBlank
    private String apellido;
    @NotBlank
    private String email;
    @NotBlank
    private String contrasenia;
    @NotBlank
    private String telefono;
    @NotBlank
    private Date fechaNacimiento;

    private String rol;
    private String usuario;
    private TipoUsuario tipo;

    public Usuario(int id, @NotBlank String nombre, @NotBlank String apellido, @NotBlank String email,
            @NotBlank String contrasenia, @NotBlank String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasenia = contrasenia;
        this.telefono = telefono;
    }
}