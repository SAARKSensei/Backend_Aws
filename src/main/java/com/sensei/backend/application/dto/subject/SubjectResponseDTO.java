package com.sensei.backend.application.dto.subject;

import com.sensei.backend.domain.model.SubjectModel;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class SubjectResponseDTO {

    private UUID id;
    private String name;
    private String description;
    private String iconUrl;
    private Boolean isActive;
    private Instant createdAt;

    /**
     * DOMAIN → DTO mapping
     * This is an application-layer responsibility
     */
    public static SubjectResponseDTO fromDomain(SubjectModel subject) {
        return SubjectResponseDTO.builder()
                .id(subject.getId())
                .name(subject.getName())
                .description(subject.getDescription())
                .iconUrl(subject.getIconUrl())
                .isActive(subject.isActive())
                .createdAt(subject.getCreatedAt())
                .build();
    }
}
