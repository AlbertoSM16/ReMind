package com.remind.back.controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remind.back.dto.PacienteDTO;
import com.remind.back.entities.Paciente;
import com.remind.back.entities.Terapeuta;
import com.remind.back.services.PacienteService;

@RestController
@RequestMapping("/api/paciente")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<?> getAllPacientes() {
        List<Paciente> pacientes = pacienteService.getAllPacientes();
        if (pacientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } else {
            return ResponseEntity.ok(pacientes);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPacienteById(@PathVariable Integer id) {
        Optional<Paciente> pacienteOpt = pacienteService.getPacienteById(id);
        if (pacienteOpt.isPresent()) {
            return ResponseEntity.ok(pacienteOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente no encontrado");
        }
    }

    @PostMapping
    public ResponseEntity<?> createPaciente(@RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("email") String email,
            @RequestParam("contraseña") String contraseña,
            @RequestParam("telefono") String telefono,
            @RequestParam("enfermedad") String enfermedad,
            @RequestParam("edad") Integer edad,
            @RequestParam("nombreResponsable") String nombreResponsable,
            @RequestParam("terapeutaId") Terapeuta terapeutaId) {
        try {
            PacienteDTO paciente = new PacienteDTO(nombre, apellido, email, contraseña, telefono, enfermedad, edad,
                    nombreResponsable, terapeutaId);
            Paciente createdPaciente = pacienteService.createPaciente(paciente);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPaciente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el paciente: " + e.getMessage());
        }

    }

}
