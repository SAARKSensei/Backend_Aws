package com.sensei.backend.adapter.persistence.jpa.repository;

import com.sensei.backend.adapter.persistence.jpa.entity.InteractiveProcess;
import com.sensei.backend.application.dto.interactiveactivityprocess.*;
import com.sensei.backend.domain.port.repository.InteractiveProcessRepository;
import com.sensei.backend.domain.service.InteractiveProcessService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InteractiveProcessServiceImpl implements InteractiveProcessService {

    private final InteractiveProcessRepository repository;

    private InteractiveProcessResponseDTO map(InteractiveProcess p) {
        InteractiveProcessResponseDTO dto = new InteractiveProcessResponseDTO();
        dto.setId(p.getId());
        dto.setInteractiveActivityId(p.getInteractiveActivityId());
        dto.setSubModuleId(p.getSubModuleId());
        dto.setStepOrder(p.getStepOrder());
        dto.setSenseiMessage(p.getSenseiMessage());
        dto.setHint(p.getHint());
        dto.setChildTask(p.getChildTask());
        dto.setMediaUrl(p.getMediaUrl());
        dto.setStatus(p.getStatus());
        dto.setStartedAt(p.getStartedAt());
        dto.setCompletedAt(p.getCompletedAt());
        return dto;
    }

    @Override
    public InteractiveProcessResponseDTO create(InteractiveProcessRequestDTO dto) {
        InteractiveProcess p = InteractiveProcess.builder()
                .interactiveActivityId(dto.getInteractiveActivityId())
                .subModuleId(dto.getSubModuleId())
                .stepOrder(dto.getStepOrder())
                .senseiMessage(dto.getSenseiMessage())
                .hint(dto.getHint())
                .childTask(dto.getChildTask())
                .mediaUrl(dto.getMediaUrl())
                .status("NOT_STARTED")
                .build();

        return map(repository.save(p));
    }

    @Override
    public InteractiveProcessResponseDTO update(UUID id, InteractiveProcessRequestDTO dto) {
        InteractiveProcess p = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Process not found"));

        p.setStepOrder(dto.getStepOrder());
        p.setSenseiMessage(dto.getSenseiMessage());
        p.setHint(dto.getHint());
        p.setChildTask(dto.getChildTask());
        p.setMediaUrl(dto.getMediaUrl());

        return map(repository.save(p));
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public InteractiveProcessResponseDTO getById(UUID id) {
        return map(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Process not found")));
    }

    @Override
    public List<InteractiveProcessResponseDTO> getByActivity(UUID activityId) {
        return repository.findByInteractiveActivityIdOrderByStepOrderAsc(activityId)
                .stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public List<InteractiveProcessResponseDTO> getBySubModule(UUID subModuleId) {
        return repository.findBySubModuleIdOrderByStepOrderAsc(subModuleId)
                .stream().map(this::map).collect(Collectors.toList());
    }
}
