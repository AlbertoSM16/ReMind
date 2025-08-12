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

import com.remind.back.dto.AgendaOutputDTO;
import com.remind.back.entities.Juego;
import com.remind.back.dto.AgendaInputDTO;

import com.remind.back.services.AgendaService;

@RestController
@RequestMapping("/api/agenda")
@CrossOrigin(origins = "*")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @GetMapping
    public ResponseEntity<?> getAllAgendas(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<AgendaOutputDTO> agendas = agendaService.getAllAgendas(page, size);
        if (agendas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } else {
            return ResponseEntity.ok(agendas);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAgendaById(@PathVariable Integer id) {
        AgendaOutputDTO agendaOpt = agendaService.getAgendaById(id);
        if (agendaOpt != null) {
            return ResponseEntity.ok(agendaOpt);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agenda no encontrada");
        }
    }

    @PostMapping
    public ResponseEntity<?> createAgenda(@RequestBody AgendaInputDTO agendaInputDTO) {
        try {
            AgendaOutputDTO createdAgenda = agendaService.createAgenda(agendaInputDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAgenda);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la agenda");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAgenda(@PathVariable Integer id, @RequestBody AgendaInputDTO agendaInputDTO) {
        try {
            AgendaOutputDTO updatedAgenda = agendaService.updateAgenda(id, agendaInputDTO);
            if (updatedAgenda != null) {
                return ResponseEntity.ok(updatedAgenda);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agenda no encontrada");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la agenda");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAgenda(@PathVariable Integer id) {
        try {
            agendaService.deleteAgenda(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la agenda");
        }
    }

    @PostMapping("/{agendaId}/juego/{juegoId}")
    public ResponseEntity<?> assignJuegoToAgenda(@PathVariable Integer agendaId, @PathVariable Integer juegoId) {
        try {
            agendaService.assignJuegoToAgenda(agendaId, juegoId);
            return ResponseEntity.ok().body("Juego asignado correctamente a la agenda.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al asignar el juego.");
        }
    }

    @GetMapping("/{agendaId}/juegos")
    public ResponseEntity<?> getJuegosDeLaAgenda(@PathVariable Integer agendaId) {
        try {
            List<Juego> juegos = agendaService.getJuegosByAgendaId(agendaId);
            return ResponseEntity.ok(juegos);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{agendaId}/juego/{juegoId}")
    public ResponseEntity<?> removeJuegoDeAgenda(@PathVariable Integer agendaId, @PathVariable Integer juegoId) {
        try {
            agendaService.removeJuegoFromAgenda(agendaId, juegoId);
            return ResponseEntity.ok().body("Juego eliminado de la agenda correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el juego de la agenda.");
        }
    }
}
