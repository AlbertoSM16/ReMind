package com.remind.back.controllers;

import com.remind.back.entities.Administrador;
import com.remind.back.entities.LoginRequest;
import com.remind.back.entities.Paciente;
import com.remind.back.entities.Terapeuta;
import com.remind.back.repositories.AdministradorRepository;
import com.remind.back.repositories.PacienteRepository;
import com.remind.back.repositories.TerapeutaRepository;
import com.remind.back.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") 
public class AuthController {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private TerapeutaRepository terapeutaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest login) {
        String usuario = login.getUsuario();
        String contrasenia = login.getContrasenia();
        System.out.println(login.toString());
        Optional<Administrador> adminOpt = administradorRepository.findByUsuario(usuario);
        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();
            if (passwordEncoder.matches(contrasenia, admin.getContrasena())) {
                String rol = "administrador";
                String token = jwtUtil.generateToken(admin.getUsuario(), rol);
                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "rol", rol,
                        "nombre", admin.getNombre(),
                        "id", admin.getId()
                        
                ));
            }
        }
        
        Optional<Terapeuta> terapeutaOpt = terapeutaRepository.findByUsuario(usuario);
        if (terapeutaOpt.isPresent()) {
            Terapeuta terapeuta = terapeutaOpt.get();
            if (passwordEncoder.matches(contrasenia, terapeuta.getContrasena())) {
                String rol = "terapeuta";
                String token = jwtUtil.generateToken(terapeuta.getUsuario(), rol);
                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "rol", rol,
                        "nombre", terapeuta.getNombre(),
                        "id", terapeuta.getId()
                ));
            }
        }
        
        Optional<Paciente> pacienteOpt = pacienteRepository.findByUsuario(usuario);
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            if (passwordEncoder.matches(contrasenia, paciente.getContrasena())) {
                String rol = "paciente";
                String token = jwtUtil.generateToken(paciente.getUsuario(), rol);
                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "rol", rol,
                        "nombre", paciente.getNombre(),
                        "id", paciente.getId()
                ));
            }
        }
        
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }
}