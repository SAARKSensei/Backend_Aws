package com.sensei.backend.service.impl;

import com.sensei.backend.dto.interactiveProcessSubStep.*;
import com.sensei.backend.entity.InteractiveProcess;
import com.sensei.backend.entity.InteractiveProcessSubStep;
import com.sensei.backend.repository.InteractiveProcessRepository;
import com.sensei.backend.repository.InteractiveProcessSubStepRepository;
import com.sensei.backend.service.InteractiveProcessSubStepService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InteractiveProcessSubStepServiceImpl
        implements InteractiveProcessSubStepService {

    private final InteractiveProcessSubStepRepository subStepRepository;
    private final InteractiveProcessRepository processRepository;

    private InteractiveProcessSubStepResponseDTO map(
            InteractiveProcessSubStep s
    ) {
        return InteractiveProcessSubStepResponseDTO.builder()
                .id(s.getId())
                .processId(s.getProcess().getId())
                .stepOrder(s.getStepOrder())
                .stepText(s.getStepText())
                .status(s.getStatus())
                .startedAt(s.getStartedAt())
                .completedAt(s.getCompletedAt())
                .build();
    }

    @Override
    public InteractiveProcessSubStepResponseDTO create(
            InteractiveProcessSubStepRequestDTO dto
    ) {

        InteractiveProcess process = processRepository.findById(dto.getProcessId())
                .orElseThrow(() -> new RuntimeException("Process not found"));

        InteractiveProcessSubStep subStep =
                InteractiveProcessSubStep.builder()
                        .process(process)
                        .stepOrder(dto.getStepOrder())
                        .stepText(dto.getStepText())
                        .status("NOT_STARTED")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

        return map(subStepRepository.save(subStep));
    }

    @Override
    public InteractiveProcessSubStepResponseDTO update(
            UUID subStepId,
            InteractiveProcessSubStepRequestDTO dto
    ) {

        InteractiveProcessSubStep subStep =
                subStepRepository.findById(subStepId)
                        .orElseThrow(() -> new RuntimeException("SubStep not found"));

        subStep.setStepOrder(dto.getStepOrder());
        subStep.setStepText(dto.getStepText());
        subStep.setUpdatedAt(LocalDateTime.now());

        return map(subStepRepository.save(subStep));
    }

    @Override
    public void delete(UUID subStepId) {
        subStepRepository.deleteById(subStepId);
    }

    @Override
    public InteractiveProcessSubStepResponseDTO getById(UUID subStepId) {
        return map(
                subStepRepository.findById(subStepId)
                        .orElseThrow(() -> new RuntimeException("SubStep not found"))
        );
    }

    @Override
    public List<InteractiveProcessSubStepResponseDTO> getByProcess(
            UUID processId
    ) {
        return subStepRepository
                .findByProcess_IdOrderByStepOrderAsc(processId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
