package com.sensei.backend.adapter.controller;
import com.sensei.backend.application.dto.processChildStep.ProcessChildStepRequestDTO;
import com.sensei.backend.application.dto.processChildStep.ProcessChildStepResponseDTO;
import com.sensei.backend.domain.service.ProcessChildStepService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/process-child-steps")
@RequiredArgsConstructor
public class ProcessChildStepController {

    

    private final ProcessChildStepService service;

    @PostMapping
    public ProcessChildStepResponseDTO start(@RequestBody ProcessChildStepRequestDTO dto) {
        return service.start(dto);
    }

    @PutMapping("/{id}/status")
    public ProcessChildStepResponseDTO updateStatus(@PathVariable UUID id, @RequestParam String status) {
        return service.updateStatus(id, status);
    }

    @GetMapping("/process/{processId}")
    public List<ProcessChildStepResponseDTO> getByProcess(@PathVariable UUID processId) {
        return service.getByProcess(processId);
    }

    @GetMapping("/child/{childId}")
    public List<ProcessChildStepResponseDTO> getByChild(@PathVariable UUID childId) {
        return service.getByChild(childId);
    }

    @PutMapping("/complete/{id}")
    public ProcessChildStepResponseDTO complete(@PathVariable UUID id) {
        return service.complete(id);
    }
}

