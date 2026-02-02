package com.sensei.backend.adapter.controller;

import com.sensei.backend.application.dto.interactiveactivityprocess.*;
import com.sensei.backend.domain.service.InteractiveProcessService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interactive-process")
@RequiredArgsConstructor
public class InteractiveProcessController {

    private final InteractiveProcessService service;

    @PostMapping
    public InteractiveProcessResponseDTO create(@RequestBody InteractiveProcessRequestDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public InteractiveProcessResponseDTO update(@PathVariable UUID id, @RequestBody InteractiveProcessRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public InteractiveProcessResponseDTO getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping("/activity/{activityId}")
    public List<InteractiveProcessResponseDTO> getByActivity(@PathVariable UUID activityId) {
        return service.getByActivity(activityId);
    }

    @GetMapping("/submodule/{subModuleId}")
    public List<InteractiveProcessResponseDTO> getBySubModule(@PathVariable UUID subModuleId) {
        return service.getBySubModule(subModuleId);
    }
}
