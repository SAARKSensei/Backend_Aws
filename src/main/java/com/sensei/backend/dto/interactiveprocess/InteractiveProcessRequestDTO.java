package com.sensei.backend.dto.interactiveprocess;

import lombok.Data;
import java.util.UUID;

@Data
public class InteractiveProcessRequestDTO {

    private UUID interactiveActivityId;
    private Integer stepOrder;
    private String senseiMessage;
    private String hint;
    private String childTask;
    private String mediaUrl;
}
