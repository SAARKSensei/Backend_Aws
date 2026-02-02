package com.sensei.backend.application.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class InteractiveActivityDTO {
    private String interactiveActivityId;

    @NotBlank
    private String interactiveActivityName;

    private int interactiveActivityNumber;

    private String intro;
    private String materialsRequired;
    private String keyOutcomes;
    private String howToDoIt;
    private String learningObjective;
    private String duration;
    private String image;
    private Boolean submission;
    private String ratings;
    private String feedback;
    private String tags;
    private String bookRef;
    private String videoRef;
    private float progress;

    private List<ProcessesDTO> processes;

    private String submoduleIdRef;
    private String firstProcessIdRef;

    // 🆕 Newly Added Fields
    private String submissionType;
    private String submissionLink;
    private String typeOfInteractiveActivity;

    // Getter for interactiveActivityNumber
    public int getInteractiveActivityNumber() {
        return this.interactiveActivityNumber;
    }
}
