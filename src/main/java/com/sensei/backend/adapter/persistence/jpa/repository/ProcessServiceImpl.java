package com.sensei.backend.adapter.persistence.jpa.repository;

import com.sensei.backend.adapter.persistence.jpa.entity.InteractiveActivity;
import com.sensei.backend.adapter.persistence.jpa.entity.Process;
import com.sensei.backend.application.dto.process.ProcessRequestDTO;
import com.sensei.backend.application.dto.process.ProcessResponseDTO;
import com.sensei.backend.domain.port.repository.InteractiveActivityRepository;
import com.sensei.backend.domain.port.repository.ProcessRepository;
import com.sensei.backend.domain.service.ProcessService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {

    private final ProcessRepository processRepository;
    private final InteractiveActivityRepository activityRepository;

    @Override
    public ProcessResponseDTO createProcess(ProcessRequestDTO dto) {

        InteractiveActivity activity = activityRepository.findById(dto.getInteractiveActivityId())
                .orElseThrow(() -> new RuntimeException("InteractiveActivity not found"));

        Process process = Process.builder()
                .interactiveActivity(activity)
                .senseiMessage(dto.getSenseiMessage())
                .hint(dto.getHint())
                .childSteps(dto.getChildSteps())
                .mediaUrl(dto.getMediaUrl())
                .stepOrder(dto.getStepOrder())
                .build();

        Process saved = processRepository.save(process);
        return map(saved);
    }

    @Override
    public List<ProcessResponseDTO> getByInteractiveActivity(UUID interactiveActivityId) {

        InteractiveActivity activity = activityRepository.findById(interactiveActivityId)
                .orElseThrow(() -> new RuntimeException("InteractiveActivity not found"));

        return processRepository.findByInteractiveActivityOrderByStepOrder(activity)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private ProcessResponseDTO map(Process process) {
        return ProcessResponseDTO.builder()
                .id(process.getId())
                .interactiveActivityId(process.getInteractiveActivity().getId())
                .senseiMessage(process.getSenseiMessage())
                .hint(process.getHint())
                .childSteps(process.getChildSteps())
                .mediaUrl(process.getMediaUrl())
                .stepOrder(process.getStepOrder())
                .createdAt(process.getCreatedAt())
                .build();
    }
}
