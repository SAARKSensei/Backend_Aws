package com.sensei.backend.dto.option;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OptionResponseDTO {
    private UUID id;
    private UUID questionId;
    private String optionText;
    private String hint;
    private String counsellorNote;
    private Boolean isCorrect;
}
