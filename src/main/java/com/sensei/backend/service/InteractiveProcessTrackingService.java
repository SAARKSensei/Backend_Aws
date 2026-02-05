package com.sensei.backend.service;

import com.sensei.backend.dto.interactiveprocesstracking.InteractiveProcessTrackingRequestDTO;
import com.sensei.backend.dto.interactiveprocesstracking.InteractiveProcessTrackingResponseDTO;

import java.util.List;
import java.util.UUID;

public interface InteractiveProcessTrackingService {

    // Start a process for a child
    InteractiveProcessTrackingResponseDTO start(
            InteractiveProcessTrackingRequestDTO dto
    );

    // Mark process completed
    InteractiveProcessTrackingResponseDTO complete(
            UUID childId,
            UUID processId
    );

    // Get tracking for a child + process
    InteractiveProcessTrackingResponseDTO get(
            UUID childId,
            UUID processId
    );

    // Get all trackings for a child
    List<InteractiveProcessTrackingResponseDTO> getByChild(UUID childId);

    // Delete tracking
    void delete(UUID trackingId);
}
