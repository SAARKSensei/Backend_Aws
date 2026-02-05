package com.sensei.backend.dto.digitalactivity;

import lombok.Data;
import java.util.UUID;

@Data
public class DigitalActivityRequestDTO {
    private UUID subModuleId;
    private String title;
    private String gameType;
    private String instructions;
    // private String instruction;
    private String difficulty;
    private Integer orderIndex;
}
