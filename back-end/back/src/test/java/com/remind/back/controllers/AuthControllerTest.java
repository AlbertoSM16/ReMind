package com.remind.back.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remind.back.entities.*;
import com.remind.back.repositories.AdministradorRepository;
import com.remind.back.repositories.PacienteRepository;
import com.remind.back.repositories.TerapeutaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdministradorRepository administradorRepository;

    @MockBean
    private PacienteRepository pacienteRepository;

    @MockBean
    private TerapeutaRepository terapeutaRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLogin_AdminSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest("adminUser", "adminPass");
        
        Administrador admin = new Administrador();
        admin.setId(1);
        admin.setUsuario("adminUser");
        admin.setContrasena("encodedPass");
        admin.setNombre("AdminName");
        admin.setApellido("AdminLastName");
        admin.setEmail("admin@mail.com");
        admin.setTelefono("987654321");

        when(administradorRepository.findByUsuario("adminUser")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("adminPass", "encodedPass")).thenReturn(true);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("administrador"))
                .andExpect(jsonPath("$.nombre").value("AdminName"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void testLogin_TerapeutaSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest("terapeutaUser", "terapeutaPass");
        
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setId(2);
        terapeuta.setUsuario("terapeutaUser");
        terapeuta.setContrasena("encodedPass");
        terapeuta.setNombre("TerapeutaName");
        terapeuta.setApellido("TerapeutaLastName");
        terapeuta.setEmail("terapeuta@mail.com");
        terapeuta.setTelefono("123456789");
        terapeuta.setFechaNacimiento(new Date());
        terapeuta.setEspecialidad("Psicologia");

        when(administradorRepository.findByUsuario("terapeutaUser")).thenReturn(Optional.empty());
        when(terapeutaRepository.findByUsuario("terapeutaUser")).thenReturn(Optional.of(terapeuta));
        when(passwordEncoder.matches("terapeutaPass", "encodedPass")).thenReturn(true);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("terapeuta"))
                .andExpect(jsonPath("$.nombre").value("TerapeutaName"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void testLogin_PacienteSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest("pacienteUser", "pacientePass");
        
        Paciente paciente = new Paciente();
        paciente.setId(3);
        paciente.setUsuario("pacienteUser");
        paciente.setContrasenia("encodedPass");
        paciente.setNombre("PacienteName");
        paciente.setApellido("PacienteLastName");
        paciente.setTelefono("555555555");
        paciente.setFechaNacimiento(new Date());
        paciente.setEdad(30);
        paciente.setEnfermedad("Alzheimer");
        paciente.setNombreResponsable("ResponsableName");

        when(administradorRepository.findByUsuario("pacienteUser")).thenReturn(Optional.empty());
        when(terapeutaRepository.findByUsuario("pacienteUser")).thenReturn(Optional.empty());
        when(pacienteRepository.findByUsuario("pacienteUser")).thenReturn(Optional.of(paciente));
        when(passwordEncoder.matches("pacientePass", "encodedPass")).thenReturn(true);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("paciente"))
                .andExpect(jsonPath("$.nombre").value("PacienteName"))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void testLogin_Failure() throws Exception {
        LoginRequest loginRequest = new LoginRequest("badUser", "badPass");

        when(administradorRepository.findByUsuario("badUser")).thenReturn(Optional.empty());
        when(terapeutaRepository.findByUsuario("badUser")).thenReturn(Optional.empty());
        when(pacienteRepository.findByUsuario("badUser")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciales incorrectas"));
    }
}
