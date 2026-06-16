package com.remind.back.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.remind.back.entities.Paciente;
import com.remind.back.entities.TipoUsuario;
import com.remind.back.repositories.AdministradorRepository;
import com.remind.back.repositories.PacienteRepository;
import com.remind.back.repositories.TerapeutaRepository;
import com.remind.back.services.CustomUserDetailsService;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private TerapeutaRepository terapeutaRepository;

    @Mock
    private AdministradorRepository administradorRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void testLoadUserByUsername_WhenIsPaciente() {
        // Arrange
        Paciente paciente = new Paciente();
        paciente.setUsuario("paciente1");
        paciente.setContrasena("pass123");
        paciente.setRol(TipoUsuario.PACIENTE);

        when(pacienteRepository.findByUsuario("paciente1")).thenReturn(Optional.of(paciente));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("paciente1");

        // Assert
        assertNotNull(userDetails);
        assertEquals("paciente1", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE")));
    }

    @Test
    void testLoadUserByUsername_WhenUserNotFound() {
        // Arrange
        when(pacienteRepository.findByUsuario("desconocido")).thenReturn(Optional.empty());
        when(terapeutaRepository.findByUsuario("desconocido")).thenReturn(Optional.empty());
        when(administradorRepository.findByUsuario("desconocido")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("desconocido");
        });
    }
}