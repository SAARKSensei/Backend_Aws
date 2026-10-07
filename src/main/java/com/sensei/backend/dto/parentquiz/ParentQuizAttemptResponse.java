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
public class ParentQuizAttemptResponse {
    private UUID parentId;
    private UUID childId;
    private List<UUID> selectedOptionIds;
}
