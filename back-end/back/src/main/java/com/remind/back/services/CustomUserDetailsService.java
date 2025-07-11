package com.remind.back.services;

import com.remind.back.entities.Administrador;
import com.remind.back.entities.Paciente;
import com.remind.back.entities.Terapeuta;
import com.remind.back.repositories.AdministradorRepository;
import com.remind.back.repositories.PacienteRepository;
import com.remind.back.repositories.TerapeutaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Collections; // Para Collections.singletonList si solo hay un rol

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private TerapeutaRepository terapeutaRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Primero busca en Pacientes
        Optional<Paciente> pacienteOpt = pacienteRepository.findByEmail(email);
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            // IMPORTANTE: La contraseña aquí DEBE ser la contraseña ENCRIPTADA de tu base de datos.
            // Los roles se añaden como GrantedAuthority.
            return new User(paciente.getEmail(), paciente.getContrasenia(),
                    Collections.singletonList(() -> "ROLE_" + paciente.getRol().name())); // Asigna el rol
        }

        // Luego busca en Terapeutas
        Optional<Terapeuta> terapeutaOpt = terapeutaRepository.findByEmail(email);
        if (terapeutaOpt.isPresent()) {
            Terapeuta terapeuta = terapeutaOpt.get();
            return new User(terapeuta.getEmail(), terapeuta.getContrasenia(),
                    Collections.singletonList(() -> "ROLE_" + terapeuta.getRol().name())); // Asigna el rol
        }

        // Finalmente busca en Administradores
        Optional<Administrador> administradorOpt = administradorRepository.findByEmail(email);
        if (administradorOpt.isPresent()) {
            Administrador administrador = administradorOpt.get();
            return new User(administrador.getEmail(), administrador.getContrasenia(),
                    Collections.singletonList(() -> "ROLE_" + administrador.getRol().name())); // Asigna el rol
        }

        // Si no se encuentra en ninguna de las tablas
        throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
    }
}