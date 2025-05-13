package com.remind.back.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@DiscriminatorValue("ADMINISTRADOR")
@Entity
public class Administrador extends Usuario {
  
    public Administrador() {
        super();
    }

    public Administrador(int id, String nombre, String apellido, String email, String contraseña, String telefono) {
        super(id, nombre, apellido, email, contraseña, telefono);
    }
}
