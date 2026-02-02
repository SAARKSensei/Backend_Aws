package com.sensei.backend.application.dto.submodule;

import lombok.Data;
import java.util.UUID;
@Data
public class SubModuleRequestDTO {
    private UUID moduleId;
    private String name;
    private String description;
    private Integer orderIndex;
}
