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
import org.springframework.web.bind.annotation.RestController;

import com.remind.back.services.TerapeutaService;
import com.remind.back.dto.TerapeutaInputDTO;
import com.remind.back.dto.TerapeutaOutputDTO;

@RestController
@RequestMapping("/api/terapeuta")
public class TerapeutaController {

    @Autowired
    private TerapeutaService terapeutaService;

    @GetMapping
    public ResponseEntity<?> getAllTerapeutas(int page, int size) {
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

    @PostMapping
    public ResponseEntity<?> createTerapeuta(@RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("email") String email,
            @RequestParam("contraseña") String contraseña,
            @RequestParam("telefono") String telefono,
            @RequestParam("especialidad") String especialidad,
            @RequestParam("fechaNacimiento") Date fechaNacimiento) {

        try {
            TerapeutaInputDTO terapeutaInputDTO = new TerapeutaInputDTO(nombre, apellido, email, contraseña, telefono,
                    especialidad, fechaNacimiento, null, null);
            TerapeutaOutputDTO createdTerapeuta = terapeutaService.createTerapeuta(terapeutaInputDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTerapeuta);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el terapeuta: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTerapeuta(@RequestParam("id") Integer id) {

        try {
            terapeutaService.deleteTerapeuta(id);
            return ResponseEntity.status(HttpStatus.OK).body("El terapeuta ha sido eliminado de la base de datos");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puedo eliminar el terapeuta");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTerapeuta(@RequestParam("id") Integer id, @RequestParam("terapeutaDTO") TerapeutaInputDTO terapeutaInputDTO){
    
        try {
            terapeutaService.updateTerapeuta(id, terapeutaInputDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Los datos del terapeuta han sido modificados");

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede editar el terapeuta"  + id );

        }
    }
    

}

