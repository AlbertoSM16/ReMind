package com.remind.back.services;

import com.remind.back.Mapper.AdministradorMapper;
import com.remind.back.dto.AdministradorInputDTO;
import com.remind.back.dto.AdministradorOutputDTO;
import com.remind.back.entities.Administrador;
import com.remind.back.repositories.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Asegúrate de que esté importado
import org.springframework.stereotype.Service;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private AdministradorMapper administradorMapper;

    @Autowired
    private PasswordEncoder passwordEncoder; 
    @Override
    public AdministradorOutputDTO createAdministrador(AdministradorInputDTO administradorInputDTO) {
        Administrador administrador = administradorMapper.toAdministrador(administradorInputDTO);
        
        administrador.setContrasena(passwordEncoder.encode(administradorInputDTO.getContrasena()));
        
        Administrador savedAdministrador = administradorRepository.save(administrador);
        return administradorMapper.toDTO(savedAdministrador);
    }

   
}