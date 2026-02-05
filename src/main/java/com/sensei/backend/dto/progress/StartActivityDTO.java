package com.sensei.backend.dto.progress;

import lombok.Data;
import java.util.UUID;

@Data
public class StartActivityDTO {
    private UUID childId;
    private UUID interactiveActivityId;
}
