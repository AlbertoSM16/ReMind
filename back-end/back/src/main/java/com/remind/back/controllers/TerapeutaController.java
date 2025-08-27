package com.remind.back.controllers;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.remind.back.services.TerapeutaService;
import com.remind.back.dto.AgendaOutputDTO;
import com.remind.back.dto.PasswordResetDTO;
import com.remind.back.dto.TerapeutaCreatedDTO;
import com.remind.back.dto.TerapeutaInputDTO;
import com.remind.back.dto.TerapeutaOutputDTO;
import com.remind.back.dto.TerapeutaSeguimientoDTO;

@RestController
@RequestMapping("/api/terapeuta")
@CrossOrigin(origins = "*")
public class TerapeutaController {

    @Autowired
    private TerapeutaService terapeutaService;

    @GetMapping
    public ResponseEntity<?> getAllTerapeutas(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<TerapeutaOutputDTO> terapeutas = terapeutaService.getAllTerapeutas(page, size);
        if (terapeutas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } else {
            return ResponseEntity.ok(terapeutas);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTerapeutaById(@PathVariable Integer id) {
        TerapeutaOutputDTO terapuetaOpt = terapeutaService.getTerapeutaById(id);
        if (terapuetaOpt != null) {
            return ResponseEntity.ok(terapuetaOpt);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente no encontrado");
        }
    }

    // NUEVO ENDPOINT AÑADIDO
    @GetMapping("/{id}/seguimiento")
    public ResponseEntity<TerapeutaSeguimientoDTO> getSeguimientoIndividual(@PathVariable Integer id) {
        try {
            TerapeutaSeguimientoDTO seguimientoData = terapeutaService.getSeguimientoByTerapeutaId(id);
            return ResponseEntity.ok(seguimientoData);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createTerapeuta(@RequestBody TerapeutaInputDTO terapeutaInputDTO) {

        try {
            TerapeutaCreatedDTO createdTerapeuta = terapeutaService.createTerapeuta(terapeutaInputDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTerapeuta);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el terapeuta: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTerapeuta(@PathVariable("id") Integer id) {

        try {
            terapeutaService.deleteTerapeuta(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se puede eliminar el terapeuta con id: " + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TerapeutaOutputDTO> updateTerapeuta(@PathVariable Integer id, @RequestBody TerapeutaInputDTO terapeutaInputDTO) {
        TerapeutaOutputDTO terapeutaActualizado = terapeutaService.updateTerapeuta(id, terapeutaInputDTO);
        
        return new ResponseEntity<>(terapeutaActualizado, HttpStatus.OK);
    }

    @GetMapping("/{id}/agendas")
    public ResponseEntity<?> getAgendasDelTerapeuta(@PathVariable Integer id) {
        try {
            List<AgendaOutputDTO> agendas = terapeutaService.getAgendasByTerapeutaId(id);
            if (agendas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron agendas para este terapeuta.");
            }
            return ResponseEntity.ok(agendas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al recuperar las agendas.");
        }
    }

    @PostMapping("/{id}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable Integer id) {
        try {
            PasswordResetDTO newPassword = terapeutaService.resetPassword(id);
            return ResponseEntity.ok(newPassword);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al reiniciar la contraseña");
        }
    }

}