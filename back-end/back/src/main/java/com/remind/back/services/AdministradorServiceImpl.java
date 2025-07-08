package com.remind.back.services;

import com.remind.back.Mapper.AdministradorMapper;
import com.remind.back.dto.AdministradorInputDTO;
import com.remind.back.dto.AdministradorOutputDTO;
import com.remind.back.entities.Administrador;
import com.remind.back.entities.TipoUsuario;
import com.remind.back.repositories.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private AdministradorMapper administradorMapper;

    @Override
    @Transactional
    public AdministradorOutputDTO createAdministrador(AdministradorInputDTO administradorInputDTO) {
        Administrador administrador = administradorMapper.AdministradorInputDTOToAdministrador(administradorInputDTO);
        administrador.setTipo(TipoUsuario.ADMINISTRADOR);
        administrador.setRol("ADMINISTRADOR");
        return administradorMapper.AdministradorToAdministradorOutputDTO(administradorRepository.save(administrador));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdministradorOutputDTO> getAllAdministradores() {
        return administradorRepository.findAll().stream()
            .map(administradorMapper::AdministradorToAdministradorOutputDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdministradorOutputDTO getAdministradorById(Integer id) {
        Administrador administrador = administradorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe administrador con el id " + id));
        return administradorMapper.AdministradorToAdministradorOutputDTO(administrador);
    }

    @Override
    @Transactional
    public AdministradorOutputDTO updateAdministrador(Integer id, AdministradorInputDTO administradorInputDTO) {
        Administrador administrador = administradorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe administrador con el id " + id));

        if (administradorInputDTO.getNombre() != null) {
            administrador.setNombre(administradorInputDTO.getNombre());
        }
        if (administradorInputDTO.getApellido() != null) {
            administrador.setApellido(administradorInputDTO.getApellido());
        }
        if (administradorInputDTO.getEmail() != null) {
            administrador.setEmail(administradorInputDTO.getEmail());
        }
        if (administradorInputDTO.getTelefono() != null) {
            administrador.setTelefono(administradorInputDTO.getTelefono());
        }
        if (administradorInputDTO.getFechaNacimiento() != null) {
            administrador.setFechaNacimiento(administradorInputDTO.getFechaNacimiento());
        }

        return administradorMapper.AdministradorToAdministradorOutputDTO(administradorRepository.save(administrador));
    }

    @Override
    @Transactional
    public void deleteAdministrador(Integer id) {
        if (!administradorRepository.existsById(id)) {
            throw new NoSuchElementException("No existe administrador con el id " + id + " y no se puede eliminar");
        }
        administradorRepository.deleteById(id);
    }
}