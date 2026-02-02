package com.sensei.backend.application.dto;

import java.util.Map;
import java.util.UUID;

public class QuizResultDTO {
    private String childId;
    private Map<UUID, UUID> answerSelections;

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public Map<UUID, UUID> getAnswerSelections() {
        return answerSelections;
    }

    public void setAnswerSelections(Map<UUID, UUID> answerSelections) {
        this.answerSelections = answerSelections;
    }
}