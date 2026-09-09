package com.sensei.backend.dto.parentquiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentQuizResponse {
    private UUID questionId;
    private String questionText;
    private Integer orderIndex;
    private List<ParentQuizOptionDto> options;
}
