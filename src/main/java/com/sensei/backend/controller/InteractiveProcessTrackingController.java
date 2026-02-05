package com.sensei.backend.controller;

import com.sensei.backend.dto.interactiveprocesstracking.InteractiveProcessTrackingRequestDTO;
import com.sensei.backend.dto.interactiveprocesstracking.InteractiveProcessTrackingResponseDTO;
import com.sensei.backend.service.AccessControlService;
import com.sensei.backend.service.InteractiveProcessTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interactive-process-tracking")
@RequiredArgsConstructor
public class InteractiveProcessTrackingController {

    private final InteractiveProcessTrackingService trackingService;
    private final AccessControlService accessControlService;

    // --------------------------------------------------
    // START / UPSERT PROCESS TRACKING
    // --------------------------------------------------
    @PostMapping("/start")
    public ResponseEntity<InteractiveProcessTrackingResponseDTO> start(
            @RequestBody InteractiveProcessTrackingRequestDTO dto
    ) {
        // 🔐 Validate subject access
        UUID subjectId =
                accessControlService.getSubjectIdByProcess(dto.getInteractiveProcessId());
        accessControlService.validateSubjectAccess(dto.getChildId(), subjectId);

        return ResponseEntity.ok(trackingService.start(dto));
    }

    // --------------------------------------------------
    // GET TRACKING (child + process)
    // --------------------------------------------------
    @GetMapping("/process/{processId}")
    public ResponseEntity<InteractiveProcessTrackingResponseDTO> get(
            @PathVariable UUID processId,
            @RequestParam UUID childId
    ) {
        UUID subjectId =
                accessControlService.getSubjectIdByProcess(processId);
        accessControlService.validateSubjectAccess(childId, subjectId);

        return ResponseEntity.ok(trackingService.get(childId, processId));
    }

    // --------------------------------------------------
    // COMPLETE PROCESS
    // --------------------------------------------------
    @PutMapping("/complete/{processId}")
    public ResponseEntity<InteractiveProcessTrackingResponseDTO> complete(
            @PathVariable UUID processId,
            @RequestParam UUID childId
    ) {
        UUID subjectId =
                accessControlService.getSubjectIdByProcess(processId);
        accessControlService.validateSubjectAccess(childId, subjectId);

        return ResponseEntity.ok(trackingService.complete(childId, processId));
    }

    // --------------------------------------------------
    // GET ALL TRACKING FOR CHILD
    // --------------------------------------------------
    @GetMapping("/child/{childId}")
    public ResponseEntity<List<InteractiveProcessTrackingResponseDTO>> getByChild(
            @PathVariable UUID childId
    ) {
        return ResponseEntity.ok(trackingService.getByChild(childId));
    }
}
