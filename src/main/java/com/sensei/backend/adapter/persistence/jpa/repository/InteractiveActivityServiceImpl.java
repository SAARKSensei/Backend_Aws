package com.sensei.backend.adapter.persistence.jpa.repository;

import com.sensei.backend.adapter.persistence.jpa.entity.InteractiveActivity;
import com.sensei.backend.adapter.persistence.jpa.entity.SubModule;
import com.sensei.backend.application.dto.interactiveactivity.InteractiveActivityRequestDTO;
import com.sensei.backend.application.dto.interactiveactivity.InteractiveActivityResponseDTO;
import com.sensei.backend.domain.port.repository.InteractiveActivityRepository;
import com.sensei.backend.domain.port.repository.SubModuleRepository;
import com.sensei.backend.domain.service.InteractiveActivityService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InteractiveActivityServiceImpl implements InteractiveActivityService {

    private final InteractiveActivityRepository repository;
    private final SubModuleRepository subModuleRepository;

    private InteractiveActivityResponseDTO map(InteractiveActivity a) {
        InteractiveActivityResponseDTO dto = new InteractiveActivityResponseDTO();
        dto.setId(a.getId());
        dto.setSubModuleId(a.getSubModule().getId());
        dto.setTitle(a.getTitle());
        dto.setLearningOutcome(a.getLearningOutcome());
        dto.setKeyObjectives(a.getKeyObjectives());
        dto.setReferenceVideo(a.getReferenceVideo());
        dto.setCoverImage(a.getCoverImage());
        dto.setObjective(a.getObjective());
        dto.setActivityType(a.getActivityType());
        dto.setOrderIndex(a.getOrderIndex());
        dto.setIsActive(a.getIsActive());
        return dto;
    }

    @Override
    public InteractiveActivityResponseDTO create(InteractiveActivityRequestDTO dto) {

        SubModule subModule = subModuleRepository.findById(dto.getSubModuleId())
                .orElseThrow(() -> new RuntimeException("SubModule not found"));

        InteractiveActivity activity = InteractiveActivity.builder()
                .subModule(subModule)
                .title(dto.getTitle())
                .learningOutcome(dto.getLearningOutcome())
                .keyObjectives(dto.getKeyObjectives())
                .referenceVideo(dto.getReferenceVideo())
                .coverImage(dto.getCoverImage())
                .objective(dto.getObjective())
                .activityType(dto.getActivityType())
                .orderIndex(dto.getOrderIndex())
                .isActive(true)
                .build();

        return map(repository.save(activity));
    }

    @Override
    public List<InteractiveActivityResponseDTO> getBySubModule(UUID subModuleId) {
        return repository.findBySubModule_IdAndIsActiveTrueOrderByOrderIndexAsc(subModuleId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public InteractiveActivityResponseDTO getById(UUID id) {
        return map(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found")));
    }

    @Override
    public InteractiveActivityResponseDTO update(UUID id, InteractiveActivityRequestDTO dto) {
        InteractiveActivity a = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        a.setTitle(dto.getTitle());
        a.setLearningOutcome(dto.getLearningOutcome());
        a.setKeyObjectives(dto.getKeyObjectives());
        a.setReferenceVideo(dto.getReferenceVideo());
        a.setCoverImage(dto.getCoverImage());
        a.setObjective(dto.getObjective());
        a.setActivityType(dto.getActivityType());
        a.setOrderIndex(dto.getOrderIndex());

        return map(repository.save(a));
    }

    @Override
    public void delete(UUID id) {
        InteractiveActivity a = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        a.setIsActive(false);
        repository.save(a);
    }
}
