package com.sensei.backend.controller;

import com.sensei.backend.dto.interactiveProcessSubStep.*;
import com.sensei.backend.service.AccessControlService;
import com.sensei.backend.service.InteractiveProcessSubStepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interactive-process-substeps")
@RequiredArgsConstructor
public class InteractiveProcessSubStepController {

    private final InteractiveProcessSubStepService service;
    private final AccessControlService accessControlService;

    @PostMapping
    public ResponseEntity<InteractiveProcessSubStepResponseDTO> create(
            @RequestBody InteractiveProcessSubStepRequestDTO dto,
            @RequestParam UUID childId
    ) {

        UUID subjectId = accessControlService.getSubjectIdByProcess(dto.getProcessId());
        accessControlService.validateSubjectAccess(childId, subjectId);

        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/by-process/{processId}")
    public ResponseEntity<List<InteractiveProcessSubStepResponseDTO>> getByProcess(
            @PathVariable UUID processId,
            @RequestParam UUID childId
    ) {

        UUID subjectId = accessControlService.getSubjectIdByProcess(processId);
        accessControlService.validateSubjectAccess(childId, subjectId);

        return ResponseEntity.ok(service.getByProcess(processId));
    }

    @GetMapping("/{subStepId}")
    public ResponseEntity<InteractiveProcessSubStepResponseDTO> getById(
            @PathVariable UUID subStepId,
            @RequestParam UUID childId
    ) {

        UUID subjectId = accessControlService.getSubjectIdBySubStep(subStepId);
        accessControlService.validateSubjectAccess(childId, subjectId);

        return ResponseEntity.ok(service.getById(subStepId));
    }

    @PutMapping("/{subStepId}")
    public ResponseEntity<InteractiveProcessSubStepResponseDTO> update(
            @PathVariable UUID subStepId,
            @RequestBody InteractiveProcessSubStepRequestDTO dto,
            @RequestParam UUID childId
    ) {

        UUID subjectId = accessControlService.getSubjectIdBySubStep(subStepId);
        accessControlService.validateSubjectAccess(childId, subjectId);

        return ResponseEntity.ok(service.update(subStepId, dto));
    }

    @DeleteMapping("/{subStepId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID subStepId,
            @RequestParam UUID childId
    ) {

        UUID subjectId = accessControlService.getSubjectIdBySubStep(subStepId);
        accessControlService.validateSubjectAccess(childId, subjectId);

        service.delete(subStepId);
        return ResponseEntity.noContent().build();
    }
}
