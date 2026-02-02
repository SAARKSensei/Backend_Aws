package com.sensei.backend.domain.service;

import java.util.List;
import java.util.UUID;

import com.sensei.backend.application.dto.processChildStep.ProcessChildStepRequestDTO;
import com.sensei.backend.application.dto.processChildStep.ProcessChildStepResponseDTO;

public interface ProcessChildStepService {

    ProcessChildStepResponseDTO start(ProcessChildStepRequestDTO dto);

    ProcessChildStepResponseDTO updateStatus(UUID id, String status);

    List<ProcessChildStepResponseDTO> getByProcess(UUID processId);

    List<ProcessChildStepResponseDTO> getByChild(UUID childId);

    ProcessChildStepResponseDTO complete(UUID id);

}
