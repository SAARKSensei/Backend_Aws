package com.sensei.backend.application.dto;

import java.util.List;
import java.util.UUID;

public class QuizSubmitRequest {
    private String childId;  //UUID stored as String
    private List<UUID> selectedAnswers;

    public String getChildId() { 
        return childId; 
    }

    public void setChildId(String childId) {   // ✅ changed UUID → String
        this.childId = childId; 
    }

    public List<UUID> getSelectedAnswers() { 
        return selectedAnswers; 
    }

    public void setSelectedAnswers(List<UUID> selectedAnswers) { 
        this.selectedAnswers = selectedAnswers; 
    }
}
