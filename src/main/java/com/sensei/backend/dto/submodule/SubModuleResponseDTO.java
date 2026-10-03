package com.sensei.backend.dto.submodule;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor   // ← THIS is missing
@AllArgsConstructor
@Builder
public class SubModuleResponseDTO {

    private UUID id;
    private String name;
    private String description;
    private Integer orderIndex;
    private Boolean isActive;
    private UUID moduleId;
    private com.sensei.backend.dto.progress.HierarchicalProgressDTO progress;
    private Boolean isLocked;
}
