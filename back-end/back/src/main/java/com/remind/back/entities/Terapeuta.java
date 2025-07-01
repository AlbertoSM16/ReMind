package com.remind.back.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("TERAPEUTA")
@Entity
@Data
@NoArgsConstructor
public class Terapeuta extends Usuario {

}