package com.remind.back.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.remind.back.dto.AgendaOutputDTO;
import com.remind.back.dto.JuegoAsignadoDTO;
import com.remind.back.dto.PacienteSeguimientoDTO;
import com.remind.back.dto.TerapeutaInputDTO;
import com.remind.back.dto.TerapeutaOutputDTO;
import com.remind.back.dto.TerapeutaSeguimientoDTO;
import com.remind.back.entities.Agenda;
import com.remind.back.entities.JuegoAgenda;
import com.remind.back.entities.Paciente;
import com.remind.back.entities.PacienteAgenda;
import com.remind.back.entities.Terapeuta;
import com.remind.back.Mapper.TerapeutaMapper;
import com.remind.back.repositories.JuegoAgendaRepository;
import com.remind.back.repositories.PacienteAgendaRepository;
import com.remind.back.repositories.PacienteTerapeutaRepository;
import com.remind.back.repositories.TerapeutaRepository;
import com.remind.back.utils.Utils;

@Service
public class TerapeutaServiceImpl implements TerapeutaService {

    @Autowired
    private TerapeutaRepository terapeutaRepository;

    @Autowired
    private PacienteTerapeutaRepository pacienteTerapeutaRepository;

    @Autowired
    private JuegoAgendaRepository juegoAgendaRepository;

    @Autowired
    private PacienteAgendaRepository pacienteAgendaRepository;

    @Autowired
    private TerapeutaMapper terapeutaMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Utils utils;

    @Override
    @Transactional
    public TerapeutaOutputDTO createTerapeuta(TerapeutaInputDTO terapeutaInputDTO) {

        String hashedPassword = passwordEncoder.encode(terapeutaInputDTO.getContrasenia());
        terapeutaInputDTO.setContrasenia(hashedPassword);

        String usuario = utils.generateRandomUsername(terapeutaInputDTO.getNombre(), terapeutaInputDTO.getApellido());
        terapeutaInputDTO.setUsuario(usuario);

        Terapeuta terapeuta = terapeutaMapper.TerapeutaInputDTOToTerapeuta(terapeutaInputDTO);

        return terapeutaMapper.TerapeutaToTerapeutaOutputDTO(terapeutaRepository.save(terapeuta));
    }

    @Override
    @Transactional(readOnly = true)
    public TerapeutaOutputDTO getTerapeutaById(Integer id) {
        Terapeuta terapeuta = terapeutaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe terapeuta con ese id" + id));

        return terapeutaMapper.TerapeutaToTerapeutaOutputDTO(terapeuta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TerapeutaOutputDTO> getAllTerapeutas(int page, int size) {
        List<Terapeuta> terapeutas = terapeutaRepository.findAll(PageRequest.of(page, size)).getContent();
        return terapeutaRepository.findAll(PageRequest.of(page, size))
                .getContent()
                .stream()
                .map(terapeutaMapper::TerapeutaToTerapeutaOutputDTO)
                .toList();
    }

    @Override
    @Transactional
    public void deleteTerapeuta(Integer id) {
        Terapeuta terapeuta = terapeutaRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("No existe un terapeuta con ese id y no se puede eliminar"));

        if (!terapeuta.getPacientes().isEmpty()) {
            throw new IllegalStateException(
                    "No se puede eliminar el terapeuta con id " + id + " porque tiene pacientes asociados.");
        }

        terapeutaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TerapeutaOutputDTO updateTerapeuta(Integer id, TerapeutaInputDTO terapeutaInputDTO) {
        Terapeuta terapeuta = terapeutaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("El terapeuta con id " + id + " no existe"));

        if (terapeutaInputDTO.getNombre() != null) {
            terapeuta.setNombre(terapeutaInputDTO.getNombre());
        }
        if (terapeutaInputDTO.getApellido() != null) {
            terapeuta.setApellido(terapeutaInputDTO.getApellido());
        }
        if (terapeutaInputDTO.getEmail() != null) {
            terapeuta.setEmail(terapeutaInputDTO.getEmail());
        }
        if (terapeutaInputDTO.getTelefono() != null) {
            terapeuta.setTelefono(terapeutaInputDTO.getTelefono());
        }
        if (terapeutaInputDTO.getEspecialidad() != null) {
            terapeuta.setEspecialidad(terapeutaInputDTO.getEspecialidad());
        }
        if (terapeutaInputDTO.getContrasenia() != null) {
            String hashedPassword = passwordEncoder.encode(terapeutaInputDTO.getContrasenia());
            terapeuta.setContrasenia(hashedPassword);
        }

        return terapeutaMapper.TerapeutaToTerapeutaOutputDTO(terapeutaRepository.save(terapeuta));

    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendaOutputDTO> getAgendasByTerapeutaId(Integer id) {
        Terapeuta terapeuta = terapeutaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe terapeuta con el id " + id));

        List<Agenda> agendas = terapeuta.getAgendaConexiones()
                .stream()
                .map(agendaConexion -> agendaConexion.getAgenda())
                .collect(Collectors.toList());

        return agendas.stream().map(agenda -> {
            PacienteAgenda pa = pacienteAgendaRepository.findByAgendaIdAndPacienteId(agenda.getId(), null);

            String nombrePaciente = "Paciente no asignado";
            if (pa != null && pa.getPaciente() != null) {
                nombrePaciente = pa.getPaciente().getNombre() + " " + pa.getPaciente().getApellido();
            }

            return new AgendaOutputDTO(
                    agenda.getId(),
                    agenda.getNombre(),
                    nombrePaciente,
                    terapeuta.getNombre() + " " + terapeuta.getApellido());
        }).collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public TerapeutaSeguimientoDTO getSeguimientoByTerapeutaId(Integer terapeutaId) {
        Terapeuta terapeuta = terapeutaRepository.findById(terapeutaId)
                .orElseThrow(() -> new NoSuchElementException("No existe terapeuta con el id " + terapeutaId));

        List<PacienteSeguimientoDTO> pacientesDelTerapeuta = terapeuta.getPacientes()
                .stream()
                .map(paciente -> {
                    Integer agendaId = pacienteAgendaRepository.findByPacienteId(paciente.getId())
                            .stream()
                            .map(pacienteAgenda -> pacienteAgenda.getAgenda().getId())
                            .findFirst()
                            .orElse(null);

                    long juegosCompletados = 0;
                    long juegosTotales = 0;

                    List<JuegoAsignadoDTO> juegosAsignadosDto = new java.util.ArrayList<>();

                    if (agendaId != null) {
                        List<JuegoAgenda> juegosAsignados = juegoAgendaRepository.findByAgendaId(agendaId);
                        juegosTotales = juegosAsignados.size();
                        juegosCompletados = juegosAsignados.stream()
                                .filter(juego -> juego.getRealizado() != null && juego.getRealizado())
                                .count();

                        juegosAsignadosDto = juegosAsignados.stream()
                                .map(juegoAgenda -> new JuegoAsignadoDTO(
                                        juegoAgenda.getJuego().getId(),
                                        juegoAgenda.getJuego().getNombre(),
                                        juegoAgenda.getJuego().getCodigo(),
                                        juegoAgenda.getDificultad(),
                                        juegoAgenda.getRealizado() != null && juegoAgenda.getRealizado()))
                                .collect(Collectors.toList());
                    }

                    return new PacienteSeguimientoDTO(
                            paciente.getId(),
                            paciente.getNombre() + " " + paciente.getApellido(),
                            juegosCompletados,
                            juegosTotales,
                            juegosAsignadosDto // Se pasa la lista
                    );
                }).collect(Collectors.toList());

        return new TerapeutaSeguimientoDTO(
                terapeuta.getId(),
                terapeuta.getNombre() + " " + terapeuta.getApellido(),
                pacientesDelTerapeuta);
    }

}
