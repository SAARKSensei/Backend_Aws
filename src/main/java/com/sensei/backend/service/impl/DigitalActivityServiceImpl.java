package com.sensei.backend.service.impl;

import com.sensei.backend.dto.digitalactivity.*;
import com.sensei.backend.entity.DigitalActivity;
import com.sensei.backend.entity.SubModule;
import com.sensei.backend.repository.DigitalActivityRepository;
import com.sensei.backend.repository.SubModuleRepository;
import com.sensei.backend.service.DigitalActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DigitalActivityServiceImpl implements DigitalActivityService {

    private final DigitalActivityRepository repository;
    private final SubModuleRepository subModuleRepository;

    private DigitalActivityResponseDTO map(DigitalActivity d) {
        DigitalActivityResponseDTO dto = new DigitalActivityResponseDTO();
        dto.setId(d.getId());
        dto.setSubModuleId(d.getSubModule().getId());
        dto.setTitle(d.getTitle());
        dto.setGameType(d.getGameType());
        dto.setInstructions(d.getInstructions());
        // dto.setInstruction(d.getInstruction());
        dto.setDifficulty(d.getDifficulty());
        dto.setOrderIndex(d.getOrderIndex());
        dto.setIsActive(d.getIsActive());
        return dto;
    }

    @Override
    public DigitalActivityResponseDTO create(DigitalActivityRequestDTO dto) {
        SubModule sm = subModuleRepository.findById(dto.getSubModuleId())
                .orElseThrow(() -> new RuntimeException("SubModule not found"));

        DigitalActivity d = DigitalActivity.builder()
                .subModule(sm)
                .title(dto.getTitle())
                .gameType(dto.getGameType())
                .instructions(dto.getInstructions())
                // .instruction(dto.getInstruction())
                .difficulty(dto.getDifficulty())
                .orderIndex(dto.getOrderIndex())
                .isActive(true)
                .build();

        return map(repository.save(d));
    }

    @Override
    public DigitalActivityResponseDTO update(UUID id, DigitalActivityRequestDTO dto) {
        DigitalActivity d = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Digital Activity not found"));

        d.setTitle(dto.getTitle());
        d.setGameType(dto.getGameType());
        d.setInstructions(dto.getInstructions());
        // d.setInstruction(dto.getInstruction());
        d.setDifficulty(dto.getDifficulty());
        d.setOrderIndex(dto.getOrderIndex());

        return map(repository.save(d));
    }

    // @Override
    // public void delete(UUID id) {
    //     repository.deleteById(id);
    // }
    @Override
    public void delete(UUID id) {
    DigitalActivity d = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Digital Activity not found"));
    d.setIsActive(false);
    repository.save(d);
}


    @Override
    public DigitalActivityResponseDTO getById(UUID id) {
        return map(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Digital Activity not found")));
    }

    @Override
public List<DigitalActivityResponseDTO> getBySubModule(UUID subModuleId) {
    return repository
            .findBySubModule_IdAndIsActiveTrueOrderByOrderIndexAsc(subModuleId)
            .stream()
            .map(this::map)
            .collect(Collectors.toList());
}

@Override
public UUID getSubjectIdBySubModule(UUID subModuleId) {
    return subModuleRepository.findById(subModuleId)
            .orElseThrow(() -> new RuntimeException("SubModule not found"))
            .getModule()
            .getSubject()
            .getId();
}

}
