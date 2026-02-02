package com.sensei.backend.adapter.persistence.jpa.repository;

import com.sensei.backend.adapter.persistence.jpa.entity.ModuleEntity;
import com.sensei.backend.adapter.persistence.jpa.entity.SubModule;
import com.sensei.backend.adapter.persistence.jpa.repository.Module.ModuleJpaRepository;
import com.sensei.backend.application.dto.submodule.SubModuleRequestDTO;
import com.sensei.backend.application.dto.submodule.SubModuleResponseDTO;
import com.sensei.backend.domain.port.repository.SubModuleRepository;
import com.sensei.backend.domain.service.SubModuleService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubModuleServiceImpl implements SubModuleService {

    private final SubModuleRepository subModuleRepository;
    private final ModuleJpaRepository moduleRepository;

    // ---- mapper ----
    private SubModuleResponseDTO map(SubModule s) {
        SubModuleResponseDTO dto = new SubModuleResponseDTO();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setDescription(s.getDescription());
        dto.setOrderIndex(s.getOrderIndex());
        dto.setIsActive(s.getIsActive());
        dto.setModuleId(s.getModule().getId());
        return dto;
    }

    // ---- create ----
    @Override
    public SubModuleResponseDTO create(SubModuleRequestDTO dto) {
        ModuleEntity module = moduleRepository.findById(dto.getModuleId())
                .orElseThrow(() -> new EntityNotFoundException("Module not found"));

        SubModule s = SubModule.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .orderIndex(dto.getOrderIndex())
                .module(module)
                .isActive(true)
                .build();

        return map(subModuleRepository.save(s));
    }

    // ---- get all (active only) ----
    @Override
    public List<SubModuleResponseDTO> getAll() {
        return subModuleRepository.findByIsActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    // ---- get by module ----
    @Override
    public List<SubModuleResponseDTO> getByModule(UUID moduleId) {
        return subModuleRepository
                .findByModule_IdAndIsActiveTrueOrderByOrderIndexAsc(moduleId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    // ---- get by id ----
    @Override
    public SubModuleResponseDTO getById(UUID id) {
        return map(subModuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SubModule not found")));
    }

    // ---- update ----
    @Override
    public SubModuleResponseDTO update(UUID id, SubModuleRequestDTO dto) {
        SubModule s = subModuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SubModule not found"));

        s.setName(dto.getName());
        s.setDescription(dto.getDescription());
        s.setOrderIndex(dto.getOrderIndex());

        return map(subModuleRepository.save(s));
    }

    // ---- soft delete ----
    @Override
    public void delete(UUID id) {
        SubModule s = subModuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SubModule not found"));

        s.setIsActive(false);
        subModuleRepository.save(s);
    }
}
