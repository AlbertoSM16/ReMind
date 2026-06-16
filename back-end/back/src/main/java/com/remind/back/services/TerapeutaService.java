package com.remind.back.services;

import com.remind.back.dto.AgendaOutputDTO;
import com.remind.back.dto.PasswordResetDTO;
import com.remind.back.dto.TerapeutaCreatedDTO;
import com.remind.back.dto.TerapeutaInputDTO;
import com.remind.back.dto.TerapeutaOutputDTO;
import com.remind.back.dto.TerapeutaSeguimientoDTO;

import java.util.List;

public interface TerapeutaService {

  TerapeutaCreatedDTO createTerapeuta(TerapeutaInputDTO terapeutaInputDTO);

  TerapeutaOutputDTO getTerapeutaById(Integer id);

  List<TerapeutaOutputDTO> getAllTerapeutas(int page, int size);

  void deleteTerapeuta(int id);

  TerapeutaOutputDTO updateTerapeuta(Integer id, TerapeutaInputDTO terapeutaInputDTO);

  List<AgendaOutputDTO> getAgendasByTerapeutaId(Integer id);

  TerapeutaSeguimientoDTO getSeguimientoByTerapeutaId(Integer terapeutaId);

  PasswordResetDTO resetPassword(Integer id);
}
