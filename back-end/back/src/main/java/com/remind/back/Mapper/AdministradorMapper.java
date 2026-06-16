package com.remind.back.Mapper;

import com.remind.back.dto.AdministradorInputDTO;
import com.remind.back.dto.AdministradorOutputDTO;
import com.remind.back.entities.Administrador;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdministradorMapper {

    Administrador toAdministrador(AdministradorInputDTO administradorInputDTO);

    AdministradorOutputDTO toDTO(Administrador administrador);
}