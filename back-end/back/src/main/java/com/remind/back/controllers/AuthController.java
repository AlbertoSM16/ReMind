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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest login) {
        String usuario = login.getUsuario();
        String contrasenia = login.getContrasenia();

        Optional<Administrador> adminOpt = administradorRepository.findByUsuario(usuario);
        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();
            if (passwordEncoder.matches(contrasenia, admin.getContrasenia())) {
                String rol = "administrador";
                String token = JwtUtil.generateToken(admin.getUsuario(), rol);
                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "rol", rol,
                        "id", admin.getId(),
                        "nombre", admin.getNombre()
                ));
            }
        }
        // 2. Si no es admin, buscar en la tabla de Terapeutas
        Optional<Terapeuta> terapeutaOpt = terapeutaRepository.findByUsuario(usuario);
        if (terapeutaOpt.isPresent()) {
            Terapeuta terapeuta = terapeutaOpt.get();
            if (passwordEncoder.matches(contrasenia, terapeuta.getContrasenia())) {
                String rol = "terapeuta";
                String token = JwtUtil.generateToken(terapeuta.getUsuario(), rol);
                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "rol", rol,
                        "id", terapeuta.getId(),
                        "nombre", terapeuta.getNombre()
                ));
            }
        }
        // 3. Si no es admin ni terapeuta, buscar en la tabla de Pacientes
        Optional<Paciente> pacienteOpt = pacienteRepository.findByUsuario(usuario);
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            if (passwordEncoder.matches(contrasenia, paciente.getContrasenia())) {
                String rol = "paciente";
                String token = JwtUtil.generateToken(paciente.getUsuario(), rol);
                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "rol", rol,
                        "id", paciente.getId(),
                        "nombre", paciente.getNombre()
                ));
            }
        }
        
        // Si la contraseña es incorrecta para un usuario encontrado o el usuario no existe
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }
}