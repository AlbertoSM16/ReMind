package com.remind.back.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode; 

@EqualsAndHashCode(callSuper = true) 
@DiscriminatorValue("ADMINISTRADOR")
@Entity
@Data
public class Administrador extends Usuario {
   
}