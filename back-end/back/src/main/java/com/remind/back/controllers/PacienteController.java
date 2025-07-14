package com.remind.back.controllers;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.remind.back.dto.PacienteInputDTO;
import com.remind.back.dto.PacienteOutputDTO;
import com.remind.back.services.PacienteService;

@RestController
@RequestMapping("/api/paciente")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<?> getAllPacientes(int page, int size) {
        List<PacienteOutputDTO> pacientes = pacienteService.getAllPacientes(page, size);
        if (pacientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } else {
            return ResponseEntity.ok(pacientes);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPacienteById(@PathVariable Integer id) {
        PacienteOutputDTO pacienteOpt = pacienteService.getPacienteById(id);
        if (pacienteOpt != null) {
            return ResponseEntity.ok(pacienteOpt);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente no encontrado");
        }
    }

    @PostMapping
    public ResponseEntity<?> createPaciente(@RequestBody PacienteInputDTO pacienteInputDTO) {
        try {           

            PacienteOutputDTO createdPaciente = pacienteService.createPaciente(pacienteInputDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPaciente);
        
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el paciente: " + e.getMessage());
        }

    }

    @DeleteMapping("/id")
    public ResponseEntity<?> deletePaciente(@RequestParam("id") Integer id) {
        try {
            pacienteService.deletePaciente(id);
            return ResponseEntity.status(HttpStatus.OK).body("El paciente ha sido eliminado de la base de datos");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puedo eliminar el paciente");
        }
    }

    @PutMapping("/id")
    public ResponseEntity<?> updatePaciente(@RequestParam("id") Integer id,@RequestParam("pacienteDTO")PacienteInputDTO pacienteInputDTO) {
        try {
            pacienteService.updatePaciente(id, pacienteInputDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Los datos el paciente han sido modificados");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede editar el paciente" +id );

        }
    }

    

}
