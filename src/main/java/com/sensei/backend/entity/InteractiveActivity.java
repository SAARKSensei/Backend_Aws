package com.sensei.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InteractiveActivity {
    // @Id
    // @GeneratedValue(generator = "system-uuid")
    // @GenericGenerator(name="system-uuid",strategy = "uuid")
    // private String interactiveActivityId;
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    @NotBlank
    private String interactiveActivityName;

    private int interactiveActivityNumber;

    @Column(columnDefinition = "TEXT")
    private String intro;

    @Column(columnDefinition = "TEXT")
    private String materialsRequired;

    @Column(columnDefinition = "TEXT")
    private String keyOutcomes;

    @Column(columnDefinition = "TEXT")
    private String howToDoIt;

    @Column(columnDefinition = "TEXT")
    private String learningObjective;

    private String duration;

    @Column(columnDefinition = "TEXT")
    private String image;

    private Boolean submission;

    private String ratings;

    private String feedback;

    private String tags;

    @Column(columnDefinition = "TEXT")
    private String bookRef;

    @Column(columnDefinition = "TEXT")
    private String videoRef;

    private float progress;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "interactive_activity_id")
    private List<Processes> processes;

    // New Column
    @Column(columnDefinition = "TEXT")
    private String submoduleIdRef;

    // New Column
    @Column(columnDefinition = "TEXT")
    private String firstProcessIdRef;
    
 // 🆕 Newly Added Fields
    @Column(columnDefinition = "TEXT")
    private String submissionType;

    @Column(columnDefinition = "TEXT")
    private String submissionLink;

    @Column(columnDefinition = "TEXT")
    private String typeOfInteractiveActivity;
}