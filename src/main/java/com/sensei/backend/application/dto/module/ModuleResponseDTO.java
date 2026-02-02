package com.sensei.backend.application.dto.module;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import com.sensei.backend.domain.model.ModuleModel;

@Data
@Builder
public class ModuleResponseDTO {

    private UUID id;
    private UUID subjectId;
    private String subjectName;
    private String name;
    private String description;
    private Integer orderIndex;
    private Boolean isActive;

    /**
     * DOMAIN → DTO mapping
     * This is an application-layer responsibility
    */

    public static ModuleResponseDTO fromDomain(ModuleModel module){
        return ModuleResponseDTO.builder()
        .id(module.getId())
        .subjectId(module.getSubId())
        .name(module.getName())
        .description(module.getDescription())
        .subjectName(module.getSubName())
        .orderIndex(module.getOrderIndex())
        .isActive(module.isActive())
        .build();
    }
}