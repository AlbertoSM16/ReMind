package com.remind.back.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.remind.back.entities.LoginRequest;
import com.remind.back.entities.Usuario;
import com.remind.back.repositories.UsuarioRepository;
import com.remind.back.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest login) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(login.getEmail());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getContraseña().equals(login.getContraseña())) {
            return ResponseEntity.status(401).body("Contraseña incorrecta");
        }

        String tipo = usuario.getClass().getSimpleName().toUpperCase(); 
        String token = JwtUtil.generateToken(usuario.getEmail(), tipo);

        return ResponseEntity.ok(Map.of(
            "token", token,
            "rol", tipo,
            "id", usuario.getId(),
            "nombre", usuario.getNombre()
        ));
    }
}
