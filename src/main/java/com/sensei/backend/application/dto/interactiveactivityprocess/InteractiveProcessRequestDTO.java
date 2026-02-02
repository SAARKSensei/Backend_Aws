package com.sensei.backend.application.dto.interactiveactivityprocess;

import lombok.Data;
import java.util.UUID;

@Data
public class InteractiveProcessRequestDTO {

    private UUID interactiveActivityId;
    private UUID subModuleId;
    private Integer stepOrder;
    private String senseiMessage;
    private String hint;
    private String childTask;
    private String mediaUrl;
}
