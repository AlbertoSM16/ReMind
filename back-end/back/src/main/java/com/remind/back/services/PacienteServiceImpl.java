package com.remind.back.services;

import java.util.List;

import com.remind.back.Mapper.PacienteMapper;
import com.remind.back.Mapper.PacienteTerapeutaMapper;
import com.remind.back.dto.PacienteInputDTO;
import com.remind.back.dto.PacienteOutputDTO;
import com.remind.back.dto.PacienteTerapeutaInputDTO;
import com.remind.back.entities.Paciente;
import com.remind.back.entities.PacienteTerapeuta;
import com.remind.back.entities.Terapeuta;
import com.remind.back.entities.TipoUsuario;
import com.remind.back.repositories.PacienteRepository;
import com.remind.back.repositories.PacienteTerapeutaRepository;
import com.remind.back.repositories.TerapeutaRepository;

import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;

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
    private PacienteTerapeutaMapper pacienteTerapeutaMapper;

    @Autowired
    private TerapeutaRepository terapeutaRepository;

    @Autowired
    private PacienteTerapeutaRepository pacienteTerapeutaRepository;

    @Override
    @Transactional
    public PacienteOutputDTO createPaciente(PacienteInputDTO pacienteInputDTO) {
    
        Paciente paciente = pacienteMapper.PacienteInputDTOToPaciente(pacienteInputDTO);
         
        Paciente savedPaciente = pacienteRepository.save(paciente);

        Integer terapeutaId = pacienteInputDTO.getTerapeutaId();
        if (terapeutaId != null) {
            Terapeuta terapeuta = terapeutaRepository.findById(terapeutaId)
                .orElseThrow(() -> new NoSuchElementException("Terapeuta with ID " + terapeutaId + " not found."));
            PacienteTerapeutaInputDTO pacienteTerapeutaInputDTO = new PacienteTerapeutaInputDTO(paciente.getId(),terapeuta.getId());
            PacienteTerapeuta pacienteTerapeuta = pacienteTerapeutaMapper.PacienteTerapeutaInputDTOToPacienteTerapeuta(pacienteTerapeutaInputDTO);
            pacienteTerapeutaRepository.save(pacienteTerapeuta);
        }

        return pacienteMapper.PacienteToPacienteOutputDTO(savedPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteOutputDTO> getAllPacientes(int page,int size) {
        List<Paciente> pacientes = pacienteRepository.findAll(PageRequest.of(page,size)).getContent();
        return pacienteRepository.findAll(PageRequest.of(page,size))
            .getContent()
            .stream()
            .map(pacienteMapper::PacienteToPacienteOutputDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteOutputDTO getPacienteById(Integer id) {
        Paciente paciente = pacienteRepository.findById(id)
            .orElseThrow(() ->  new NoSuchElementException("El paciente con id " + id + " no existe"));
        return pacienteMapper.PacienteToPacienteOutputDTO(paciente);
    }

    @Override
    @Transactional
    public void deletePaciente(Integer id) {
        if(!pacienteRepository.existsById(id)){
            throw new NoSuchElementException("No existe un paciente con ese id y no se puede eliminar");
        }
        pacienteRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PacienteOutputDTO updatePaciente(Integer id, PacienteInputDTO pacienteDTO) {
        
        Paciente paciente = pacienteRepository.findById(id)
            .orElseThrow(() ->  new NoSuchElementException("El paciente con id " + id + " no existe"));
        if(pacienteDTO.getNombre() != null){
            paciente.setNombre(pacienteDTO.getNombre());
        }
        if(pacienteDTO.getEnfermedad() !=null){
            paciente.setEnfermedad(pacienteDTO.getEnfermedad());
        }
        if(pacienteDTO.getApellido() != null){
            paciente.setApellido(pacienteDTO.getApellido());
        }
        if(pacienteDTO.getEdad() != null){
            paciente.setEdad(pacienteDTO.getEdad());
        }    
        if(pacienteDTO.getTelefono() != null){
            paciente.setTelefono(pacienteDTO.getTelefono());
        }
        if(pacienteDTO.getEmail() != null){
            paciente.setEmail(pacienteDTO.getEmail());
        }
        if(pacienteDTO.getFechaNacimiento() != null){
            paciente.setFechaNacimiento(pacienteDTO.getFechaNacimiento());
        }
        if (pacienteDTO.getTerapeutaId() != null) {
            Terapeuta terapeuta = terapeutaRepository.findById(pacienteDTO.getTerapeutaId())
                .orElseThrow(() -> new NoSuchElementException("Terapeuta with ID " + pacienteDTO.getTerapeutaId() + " not found."));

            PacienteTerapeuta pacienteTerapeuta = pacienteTerapeutaRepository.findByPacienteId(id)
                .orElse(new PacienteTerapeuta(id, paciente, null));
            
            pacienteTerapeuta.setTerapeuta(terapeuta);
            pacienteTerapeutaRepository.save(pacienteTerapeuta);
        }
        
        return pacienteMapper.PacienteToPacienteOutputDTO(pacienteRepository.save(paciente));
      
    }

}
