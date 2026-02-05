package com.sensei.backend.service;

import com.sensei.backend.dto.interactiveProcessSubStep.*;

import java.util.List;
import java.util.UUID;

public interface InteractiveProcessSubStepService {

    InteractiveProcessSubStepResponseDTO create(
            InteractiveProcessSubStepRequestDTO dto
    );

    InteractiveProcessSubStepResponseDTO update(
            UUID subStepId,
            InteractiveProcessSubStepRequestDTO dto
    );

    void delete(UUID subStepId);

    InteractiveProcessSubStepResponseDTO getById(UUID subStepId);

    List<InteractiveProcessSubStepResponseDTO> getByProcess(UUID processId);
}
