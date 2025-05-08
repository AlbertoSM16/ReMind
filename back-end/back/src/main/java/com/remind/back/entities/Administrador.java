package com.remind.back.entities;

public class Administrador extends Usuario {
  


    public Administrador() {
        super();
    }

    public Administrador(int id, String nombre, String apellido, String email, String contraseña, String telefono) {
        super(id, nombre, apellido, email, contraseña, telefono);
    }
}
