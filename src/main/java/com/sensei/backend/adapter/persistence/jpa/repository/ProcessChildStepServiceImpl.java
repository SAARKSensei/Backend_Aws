package com.sensei.backend.adapter.persistence.jpa.repository;

import com.sensei.backend.adapter.persistence.jpa.entity.InteractiveProcess;
import com.sensei.backend.adapter.persistence.jpa.entity.ProcessChildStep;
import com.sensei.backend.application.dto.processChildStep.ProcessChildStepRequestDTO;
import com.sensei.backend.application.dto.processChildStep.ProcessChildStepResponseDTO;
import com.sensei.backend.domain.port.repository.InteractiveProcessRepository;
import com.sensei.backend.domain.port.repository.ProcessChildStepRepository;
import com.sensei.backend.domain.service.ProcessChildStepService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcessChildStepServiceImpl implements ProcessChildStepService {

    private final ProcessChildStepRepository repository;
    private final InteractiveProcessRepository processRepository;

    // ---------- Mapper ----------
    private ProcessChildStepResponseDTO map(ProcessChildStep s) {
        ProcessChildStepResponseDTO dto = new ProcessChildStepResponseDTO();
        dto.setId(s.getId());
        dto.setProcessId(s.getProcess().getId());
        dto.setStepOrder(s.getStepOrder());
        dto.setStepText(s.getStepText());
        dto.setActivityId(s.getActivityId());
        dto.setActivityType(s.getActivityType());
        dto.setStatus(s.getStatus());
        dto.setStartedAt(s.getStartedAt());
        dto.setCompletedAt(s.getCompletedAt());
        return dto;
    }

    // ---------- START STEP ----------
    @Override
    public ProcessChildStepResponseDTO start(ProcessChildStepRequestDTO dto) {

        InteractiveProcess process = processRepository.findById(dto.getProcessId())
                .orElseThrow(() -> new RuntimeException("Interactive process not found"));

        ProcessChildStep step = ProcessChildStep.builder()
                .process(process)
                .stepOrder(dto.getStepOrder())
                .stepText(dto.getStepText())
                .activityId(dto.getActivityId())
                .activityType(dto.getActivityType())
                .status("IN_PROGRESS")
                .startedAt(LocalDateTime.now())
                .build();

        return map(repository.save(step));
    }

    // ---------- UPDATE STATUS ----------
    @Override
    public ProcessChildStepResponseDTO updateStatus(UUID id, String status) {
        ProcessChildStep step = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Step not found"));

        step.setStatus(status);

        if ("COMPLETED".equalsIgnoreCase(status)) {
            step.setCompletedAt(LocalDateTime.now());
        }

        return map(repository.save(step));
    }

    // ---------- COMPLETE STEP ----------
    @Override
    public ProcessChildStepResponseDTO complete(UUID id) {
        return updateStatus(id, "COMPLETED");
    }

    // ---------- GET BY PROCESS ----------
    @Override
    public List<ProcessChildStepResponseDTO> getByProcess(UUID processId) {
        return repository.findByProcessIdOrderByStepOrderAsc(processId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    // ---------- GET BY CHILD ----------
    @Override
    public List<ProcessChildStepResponseDTO> getByChild(UUID childId) {
        return repository.findByProcess_ChildIdOrderByStartedAtAsc(childId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
