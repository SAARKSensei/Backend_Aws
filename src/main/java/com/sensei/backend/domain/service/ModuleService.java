package com.sensei.backend.domain.service;

import java.util.List;
import java.util.UUID;

import com.sensei.backend.application.dto.module.ModuleRequestDTO;
import com.sensei.backend.application.dto.module.ModuleResponseDTO;

public interface ModuleService {

    ModuleResponseDTO createModule(ModuleRequestDTO dto);

    List<ModuleResponseDTO> getModulesBySubject(UUID subjectId);

    ModuleResponseDTO getById(UUID id);

    ModuleResponseDTO update(UUID id, ModuleRequestDTO dto);

    void delete(UUID id);

    List<ModuleResponseDTO> getAllModules();
}