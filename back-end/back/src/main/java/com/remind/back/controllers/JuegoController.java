package com.remind.back.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.remind.back.services.JuegoService;
import com.remind.back.entities.Juego;
import java.util.List;

@RestController
@RequestMapping("/api/juegos")
@CrossOrigin(origins = "*")
public class JuegoController {
    
    private final JuegoService juegoService;

    public JuegoController(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    @GetMapping
    public ResponseEntity<List<Juego>> getAllJuegos() {
        return ResponseEntity.ok(juegoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Juego> getJuegoById(@PathVariable Integer id) {
        return juegoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Juego> createJuego(@RequestBody Juego juego) {
        return ResponseEntity.ok(juegoService.save(juego));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJuego(@PathVariable Integer id) {
        juegoService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
