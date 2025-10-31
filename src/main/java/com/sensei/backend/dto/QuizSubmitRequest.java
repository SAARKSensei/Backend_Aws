package com.sensei.backend.dto;

import java.util.List;
import java.util.UUID;

public class QuizSubmitRequest {
    private UUID childId;
    private List<UUID> selectedAnswers;

    public UUID getChildId() { return childId; }
    public void setChildId(UUID childId) { this.childId = childId; }

    public List<UUID> getSelectedAnswers() { return selectedAnswers; }
    public void setSelectedAnswers(List<UUID> selectedAnswers) { this.selectedAnswers = selectedAnswers; }
}
