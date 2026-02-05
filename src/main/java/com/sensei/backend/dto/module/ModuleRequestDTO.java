package com.sensei.backend.dto.module;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleRequestDTO {

    private UUID subjectId;
    private String name;
    private String description;
    private Integer orderIndex;
}
