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
import java.util.Collections; 

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private TerapeutaRepository terapeutaRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Paciente> pacienteOpt = pacienteRepository.findByUsuario(username);
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            return new User(paciente.getUsuario(), paciente.getContrasenia(),
                    Collections.singletonList(() -> "ROLE_" + paciente.getRol().name()));
        }

        Optional<Terapeuta> terapeutaOpt = terapeutaRepository.findByUsuario(username);
        if (terapeutaOpt.isPresent()) {
            Terapeuta terapeuta = terapeutaOpt.get();
            return new User(terapeuta.getUsuario(), terapeuta.getContrasena(),
                    Collections.singletonList(() -> "ROLE_" + terapeuta.getRol().name()));
        }

        Optional<Administrador> administradorOpt = administradorRepository.findByUsuario(username);
        if (administradorOpt.isPresent()) {
            Administrador administrador = administradorOpt.get();
            return new User(administrador.getUsuario(), administrador.getContrasena(),
                    Collections.singletonList(() -> "ROLE_" + administrador.getRol().name()));
        }

        throw new UsernameNotFoundException("Usuario no encontrado con el nombre de usuario: " + username);
    }
}