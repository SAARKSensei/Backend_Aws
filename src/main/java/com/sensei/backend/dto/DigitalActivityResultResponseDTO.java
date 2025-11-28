package com.sensei.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DigitalActivityResultResponseDTO {
    private String childId;
    private String digitalActivityId;
    private Double result;
    private Integer attemptCount;
    private Double totalTimeTaken;
}
