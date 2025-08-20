package com.remind.back.services;

import java.util.List;

import com.remind.back.Mapper.PacienteMapper;
import com.remind.back.dto.PacienteCreatedDTO;
import com.remind.back.dto.PacienteInputDTO;
import com.remind.back.dto.PacienteOutputDTO;
import com.remind.back.dto.PasswordResetDTO;
import com.remind.back.entities.Agenda;
import com.remind.back.entities.AgendaTerapeuta;
import com.remind.back.entities.Paciente;
import com.remind.back.entities.PacienteAgenda;
import com.remind.back.entities.PacienteTerapeuta;
import com.remind.back.entities.Terapeuta;
import com.remind.back.repositories.AgendaRepository;
import com.remind.back.repositories.AgendaTerapeutaRepository;
import com.remind.back.repositories.JuegoAgendaRepository;
import com.remind.back.repositories.PacienteAgendaRepository;
import com.remind.back.repositories.PacienteRepository;
import com.remind.back.repositories.PacienteTerapeutaRepository;
import com.remind.back.repositories.TerapeutaRepository;
import com.remind.back.utils.Utils;

import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PacienteMapper pacienteMapper;

    @Autowired
    private Utils utils;

    @Autowired
    private JuegoAgendaRepository juegoAgendaRepository;

    @Autowired
    private TerapeutaRepository terapeutaRepository;

    @Autowired
    private PacienteTerapeutaRepository pacienteTerapeutaRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private PacienteAgendaRepository pacienteAgendaRepository;

    @Autowired
    private AgendaTerapeutaRepository agendaTerapeutaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public PacienteCreatedDTO createPaciente(PacienteInputDTO pacienteInputDTO) {
        String passwordPlano = utils.generateRandomPassword(pacienteInputDTO.getNombre(),
                pacienteInputDTO.getApellido());
        String hashedPassword = passwordEncoder.encode(passwordPlano);
        pacienteInputDTO.setContrasenia(hashedPassword);

        String usuario = utils.generateRandomUsername(pacienteInputDTO.getNombre(), pacienteInputDTO.getApellido());
        pacienteInputDTO.setUsuario(usuario);

        Paciente paciente = pacienteMapper.PacienteInputDTOToPaciente(pacienteInputDTO);

        Integer terapeutaId = pacienteInputDTO.getTerapeuta_id();
        if (terapeutaId != null) {
            Terapeuta terapeuta = terapeutaRepository.findById(terapeutaId)
                    .orElseThrow(
                            () -> new NoSuchElementException("Terapeuta con ID " + terapeutaId + " no encontrado."));
            paciente.setTerapeuta(terapeuta); // Asignación directa
        }

        Paciente savedPaciente = pacienteRepository.save(paciente);

        // Crear agenda y asociarla (si se asignó un terapeuta)
        if (savedPaciente.getTerapeuta() != null) {
            Agenda nuevaAgenda = new Agenda();
            nuevaAgenda.setNombre("Agenda de " + savedPaciente.getNombre());
            Agenda savedAgenda = agendaRepository.save(nuevaAgenda);

            PacienteAgenda pacienteAgenda = new PacienteAgenda();
            pacienteAgenda.setPaciente(savedPaciente);
            pacienteAgenda.setAgenda(savedAgenda);
            pacienteAgendaRepository.save(pacienteAgenda);

            AgendaTerapeuta agendaTerapeuta = new AgendaTerapeuta();
            agendaTerapeuta.setAgenda(savedAgenda);
            agendaTerapeuta.setTerapeuta(savedPaciente.getTerapeuta());
            agendaTerapeutaRepository.save(agendaTerapeuta);
        }

        return new PacienteCreatedDTO(usuario, passwordPlano);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteOutputDTO> getAllPacientes(int page, int size) {
        List<Paciente> pacientes = pacienteRepository.findAll(PageRequest.of(page, size)).getContent();
        return pacienteRepository.findAll(PageRequest.of(page, size))
                .getContent()
                .stream()
                .map(pacienteMapper::PacienteToPacienteOutputDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteOutputDTO getPacienteById(Integer id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("El paciente con id " + id + " no existe"));
        return pacienteMapper.PacienteToPacienteOutputDTO(paciente);
    }

    @Override
    @Transactional
    public void deletePaciente(Integer id) {
        if (!pacienteRepository.existsById(id)) {
            throw new NoSuchElementException("No existe un paciente con ese id y no se puede eliminar");
        }

        List<PacienteAgenda> pacienteAgendas = pacienteAgendaRepository.findByPacienteId(id);
        for (PacienteAgenda pa : pacienteAgendas) {
            Agenda agenda = pa.getAgenda();
            if (agenda != null) {
                juegoAgendaRepository.deleteByAgendaId(agenda.getId());
                agendaTerapeutaRepository.deleteByAgendaId(agenda.getId());
                pacienteAgendaRepository.delete(pa);
                agendaRepository.delete(agenda);
            }
        }

        pacienteRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PacienteOutputDTO updatePaciente(Integer id, PacienteInputDTO pacienteDTO) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("El paciente con id " + id + " no existe"));

        if (pacienteDTO.getNombre() != null)
            paciente.setNombre(pacienteDTO.getNombre());
        if (pacienteDTO.getEnfermedad() != null)
            paciente.setEnfermedad(pacienteDTO.getEnfermedad());
        if (pacienteDTO.getApellido() != null)
            paciente.setApellido(pacienteDTO.getApellido());
        if (pacienteDTO.getEdad() != null)
            paciente.setEdad(pacienteDTO.getEdad());
        if (pacienteDTO.getTelefono() != null)
            paciente.setTelefono(pacienteDTO.getTelefono());
        if (pacienteDTO.getFechaNacimiento() != null)
            paciente.setFechaNacimiento(pacienteDTO.getFechaNacimiento());
        if (pacienteDTO.getContrasenia() != null) {
            paciente.setContrasenia(passwordEncoder.encode(pacienteDTO.getContrasenia()));
        }

        if (pacienteDTO.getTerapeuta_id() != null) {
            Terapeuta terapeuta = terapeutaRepository.findById(pacienteDTO.getTerapeuta_id())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Terapeuta with ID " + pacienteDTO.getTerapeuta_id() + " not found."));
            paciente.setTerapeuta(terapeuta);
        }

        return pacienteMapper.PacienteToPacienteOutputDTO(pacienteRepository.save(paciente));
    }

    @Override
    @Transactional
    public PasswordResetDTO resetPassword(Integer id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("El paciente con id " + id + " no existe"));

        String newPasswordPlano = utils.generateRandomPassword(paciente.getNombre(), paciente.getApellido());
        String hashedPassword = passwordEncoder.encode(newPasswordPlano);

        paciente.setContrasenia(hashedPassword);
        pacienteRepository.save(paciente);

        return new PasswordResetDTO(newPasswordPlano);
    }

}
