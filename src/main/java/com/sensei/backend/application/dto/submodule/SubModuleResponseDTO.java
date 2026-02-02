package com.sensei.backend.application.dto.submodule;

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
}
