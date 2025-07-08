package com.remind.back.services;

import com.remind.back.dto.AdministradorInputDTO;
import com.remind.back.dto.AdministradorOutputDTO;

import java.util.List;

public interface AdministradorService {

    AdministradorOutputDTO createAdministrador(AdministradorInputDTO administradorInputDTO);

    List<AdministradorOutputDTO> getAllAdministradores();

    AdministradorOutputDTO getAdministradorById(Integer id);

    AdministradorOutputDTO updateAdministrador(Integer id, AdministradorInputDTO administradorInputDTO);

    void deleteAdministrador(Integer id);
}