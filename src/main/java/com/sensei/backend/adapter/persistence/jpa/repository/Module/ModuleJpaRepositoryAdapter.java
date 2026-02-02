package com.sensei.backend.adapter.persistence.jpa.repository.Module;

import com.sensei.backend.adapter.persistence.jpa.entity.ModuleEntity;
import com.sensei.backend.adapter.persistence.jpa.entity.SubjectEntity;
import com.sensei.backend.application.dto.module.*;
import com.sensei.backend.domain.port.repository.Subject.SubjectRepositoryPort;
import com.sensei.backend.domain.service.ModuleService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleJpaRepositoryAdapter implements ModuleService {

    private final ModuleJpaRepository moduleRepository;
    private final SubjectRepositoryPort subjectRepository;

    // @Override
    // public ModuleResponseDTO createModule(ModuleRequestDTO dto) {
    //     Subject subject = subjectRepository.findById(dto.getSubjectId())
    //             .orElseThrow(() -> new RuntimeException("Subject not found"));

    //     Module module = Module.builder()
    //             .subject(subject)
    //             .name(dto.getName())
    //             .description(dto.getDescription())
    //             .orderIndex(dto.getOrderIndex())
    //             .isActive(true)
    //             .build();

    //     return map(moduleRepository.save(module));
    // }

    @Override
    public List<ModuleResponseDTO> getModulesBySubject(UUID subjectId) {
        return moduleRepository
                .findBySubjectIdAndIsActiveTrueOrderByOrderIndexAsc(subjectId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public ModuleResponseDTO getById(UUID id) {
        return map(moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found")));
    }

    @Override
    public ModuleResponseDTO update(UUID id, ModuleRequestDTO dto) {
        ModuleEntity module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        module.setName(dto.getName());
        module.setDescription(dto.getDescription());
        module.setOrderIndex(dto.getOrderIndex());

        return map(moduleRepository.save(module));
    }

    @Override
    public void delete(UUID id) {
        ModuleEntity module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        module.setIsActive(false);
        moduleRepository.save(module);
    }

    private ModuleResponseDTO map(ModuleEntity m) {
        return ModuleResponseDTO.builder()
                .id(m.getId())
                .name(m.getName())
                .description(m.getDescription())
                .orderIndex(m.getOrderIndex())
                .isActive(m.getIsActive())
                .subjectId(m.getSubject().getId())
                .subjectName(m.getSubject().getName())
                .build();
    }
    @Override
public List<ModuleResponseDTO> getAllModules() {
    return moduleRepository.findByIsActiveTrueOrderByCreatedAtDesc()
            .stream()
            .map(this::map)
            .collect(Collectors.toList());
}

    @Override
    public ModuleResponseDTO createModule(ModuleRequestDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createModule'");
    }
}

