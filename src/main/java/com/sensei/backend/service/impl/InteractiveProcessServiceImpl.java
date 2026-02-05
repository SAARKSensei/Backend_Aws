package com.sensei.backend.service.impl;

import com.sensei.backend.dto.interactiveprocess.InteractiveProcessRequestDTO;
import com.sensei.backend.dto.interactiveprocess.InteractiveProcessResponseDTO;
import com.sensei.backend.entity.InteractiveActivity;
import com.sensei.backend.entity.InteractiveProcess;
import com.sensei.backend.repository.InteractiveActivityRepository;
import com.sensei.backend.repository.InteractiveProcessRepository;
import com.sensei.backend.service.InteractiveProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InteractiveProcessServiceImpl implements InteractiveProcessService {

    private final InteractiveProcessRepository processRepository;
    private final InteractiveActivityRepository activityRepository;

    // -------------------------
    // CREATE
    // -------------------------
    @Override
    public InteractiveProcessResponseDTO create(InteractiveProcessRequestDTO dto) {

        InteractiveActivity activity = activityRepository.findById(dto.getInteractiveActivityId())
                .orElseThrow(() -> new RuntimeException("InteractiveActivity not found"));

        InteractiveProcess process = InteractiveProcess.builder()
                .interactiveActivity(activity)
                .stepOrder(dto.getStepOrder())
                .senseiMessage(dto.getSenseiMessage())
                .hint(dto.getHint())
                .childTask(dto.getChildTask())
                .mediaUrl(dto.getMediaUrl())
                .build();

        return map(processRepository.save(process));
    }

    // -------------------------
    // GET BY ID
    // -------------------------
    @Override
    public InteractiveProcessResponseDTO getById(UUID id) {
        InteractiveProcess process = processRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("InteractiveProcess not found"));
        return map(process);
    }

    // -------------------------
    // GET BY ACTIVITY
    // -------------------------
    @Override
    public List<InteractiveProcessResponseDTO> getByActivity(UUID activityId) {
        return processRepository
                .findByInteractiveActivity_IdOrderByStepOrderAsc(activityId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    // -------------------------
    // UPDATE
    // -------------------------
    @Override
    public InteractiveProcessResponseDTO update(UUID id, InteractiveProcessRequestDTO dto) {

        InteractiveProcess process = processRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("InteractiveProcess not found"));

        process.setStepOrder(dto.getStepOrder());
        process.setSenseiMessage(dto.getSenseiMessage());
        process.setHint(dto.getHint());
        process.setChildTask(dto.getChildTask());
        process.setMediaUrl(dto.getMediaUrl());

        return map(processRepository.save(process));
    }

    // -------------------------
    // DELETE
    // -------------------------
    @Override
    public void delete(UUID id) {
        if (!processRepository.existsById(id)) {
            throw new RuntimeException("InteractiveProcess not found");
        }
        processRepository.deleteById(id);
    }

    // -------------------------
    // MAPPER
    // -------------------------
    private InteractiveProcessResponseDTO map(InteractiveProcess process) {
        InteractiveProcessResponseDTO dto = new InteractiveProcessResponseDTO();
        dto.setId(process.getId());
        dto.setInteractiveActivityId(process.getInteractiveActivity().getId());
        dto.setStepOrder(process.getStepOrder());
        dto.setSenseiMessage(process.getSenseiMessage());
        dto.setHint(process.getHint());
        dto.setChildTask(process.getChildTask());
        dto.setMediaUrl(process.getMediaUrl());
        dto.setCreatedAt(process.getCreatedAt());
        dto.setUpdatedAt(process.getUpdatedAt());
        return dto;
    }
}
