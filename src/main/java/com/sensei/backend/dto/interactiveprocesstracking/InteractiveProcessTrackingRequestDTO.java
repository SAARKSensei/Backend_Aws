package com.sensei.backend.dto.interactiveprocesstracking;

import lombok.Data;
import java.util.UUID;

@Data
public class InteractiveProcessTrackingRequestDTO {
    private UUID childId;
    private UUID interactiveProcessId;
}
