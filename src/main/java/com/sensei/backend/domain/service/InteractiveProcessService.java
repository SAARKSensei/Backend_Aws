package com.sensei.backend.domain.service;

import java.util.List;
import java.util.UUID;

import com.sensei.backend.application.dto.interactiveactivityprocess.InteractiveProcessRequestDTO;
import com.sensei.backend.application.dto.interactiveactivityprocess.InteractiveProcessResponseDTO;

public interface InteractiveProcessService {

    InteractiveProcessResponseDTO create(InteractiveProcessRequestDTO dto);

    InteractiveProcessResponseDTO update(UUID id, InteractiveProcessRequestDTO dto);

    void delete(UUID id);

    InteractiveProcessResponseDTO getById(UUID id);

    List<InteractiveProcessResponseDTO> getByActivity(UUID activityId);

    List<InteractiveProcessResponseDTO> getBySubModule(UUID subModuleId);
}
