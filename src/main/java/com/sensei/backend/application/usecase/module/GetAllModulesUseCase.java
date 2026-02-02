package com.sensei.backend.application.usecase.module;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sensei.backend.application.dto.module.ModuleResponseDTO;
import com.sensei.backend.domain.port.repository.Module.ModuleRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetAllModulesUseCase {
    private final ModuleRepositoryPort repository;

    public List<ModuleResponseDTO> execute() {
        return repository.findActive()
                .stream()
                .map(ModuleResponseDTO::fromDomain)
                .collect(Collectors.toList());
    }
}
