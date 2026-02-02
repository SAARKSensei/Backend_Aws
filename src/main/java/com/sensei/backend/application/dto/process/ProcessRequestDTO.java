package com.sensei.backend.application.dto.process;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ProcessRequestDTO {
    private UUID interactiveActivityId;
    private String senseiMessage;
    private String hint;
    private List<String> childSteps;
    private String mediaUrl;
    private Integer stepOrder;
}
