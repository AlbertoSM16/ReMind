package com.remind.back.services;

import java.util.List;
import java.util.Optional;

import com.remind.back.dto.PacienteDTO;
import com.remind.back.entities.Paciente;
import com.remind.back.entities.PacienteTerapeuta;
import com.remind.back.entities.Terapeuta;
import com.remind.back.repositories.PacienteRepository;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Override
    @Transactional
    public Paciente createPaciente(PacienteDTO pacienteDTO) {
        Paciente paciente = new Paciente();
        paciente.setNombre(pacienteDTO.getNombre());
        paciente.setApellido(pacienteDTO.getApellido());
        paciente.setEmail(pacienteDTO.getEmail());
        paciente.setContraseña(pacienteDTO.getContraseña());
        paciente.setTelefono(pacienteDTO.getTelefono());
        paciente.setEnfermedad(pacienteDTO.getEnfermedad());
        paciente.setEdad(pacienteDTO.getEdad());
        paciente.setNombreResponsable(pacienteDTO.getNombreResponsable());
        PacienteTerapeuta pacienteTerapeuta = new PacienteTerapeuta();
        pacienteTerapeuta.setTerapeuta(pacienteDTO.getTerapeutaId());
        pacienteRepository.save(paciente);
        pacienteTerapeuta.setPaciente(paciente);
        return paciente;
    }

    @Override
    public List<Paciente> getAllPacientes() {
        return pacienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paciente> getPacienteById(Integer id) {
        return pacienteRepository.findById(id);
    }

    @Override
    @Transactional
    public void deletePaciente(Integer id) {
        pacienteRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updatePaciente(Integer id, PacienteDTO pacienteDTO) {
        Paciente paciente = pacienteRepository.findById(id).get();
        paciente.setNombre(pacienteDTO.getNombre());
        paciente.setApellido(pacienteDTO.getApellido());
        paciente.setEmail(pacienteDTO.getEmail());
        paciente.setContraseña(pacienteDTO.getContraseña());
        paciente.setTelefono(pacienteDTO.getTelefono());
        paciente.setEnfermedad(pacienteDTO.getEnfermedad());
        paciente.setEdad(pacienteDTO.getEdad());
        paciente.setNombreResponsable(pacienteDTO.getNombreResponsable());
        // parte de modificar
        
        pacienteRepository.save(paciente);
    }

}
