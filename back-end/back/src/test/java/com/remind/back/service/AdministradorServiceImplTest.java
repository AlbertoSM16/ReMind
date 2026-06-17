package com.remind.back.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.remind.back.Mapper.AdministradorMapper;
import com.remind.back.dto.AdministradorInputDTO;
import com.remind.back.dto.AdministradorOutputDTO;
import com.remind.back.entities.Administrador;
import com.remind.back.repositories.AdministradorRepository;
import com.remind.back.services.AdministradorServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AdministradorServiceImplTest {

    @Mock
    private AdministradorRepository administradorRepository;

    @Mock
    private AdministradorMapper administradorMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdministradorServiceImpl administradorService;

    @Test
    void testCreateAdministrador() {
        // Arrange
        AdministradorInputDTO inputDTO = new AdministradorInputDTO();
        inputDTO.setContrasena("miPasswordSeguro");

        Administrador administradorMapeado = new Administrador();
        Administrador administradorGuardado = new Administrador();

        AdministradorOutputDTO outputDTO = new AdministradorOutputDTO();

        when(administradorMapper.toAdministrador(inputDTO)).thenReturn(administradorMapeado);
        when(passwordEncoder.encode("miPasswordSeguro")).thenReturn("passwordEncriptado");
        when(administradorRepository.save(administradorMapeado)).thenReturn(administradorGuardado);
        when(administradorMapper.toDTO(administradorGuardado)).thenReturn(outputDTO);

        // Act
        AdministradorOutputDTO result = administradorService.createAdministrador(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals("passwordEncriptado", administradorMapeado.getContrasena());
        verify(passwordEncoder).encode("miPasswordSeguro");
        verify(administradorRepository).save(administradorMapeado);
    }
}