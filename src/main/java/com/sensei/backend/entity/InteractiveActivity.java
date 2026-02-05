// package com.sensei.backend.entity;

// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import org.hibernate.annotations.GenericGenerator;

// import javax.persistence.*;
// import javax.validation.constraints.NotBlank;
// import java.util.List;

// @Entity
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// public class InteractiveActivity {
//     @Id
//     @GeneratedValue(generator = "system-uuid")
//     @GenericGenerator(name="system-uuid",strategy = "uuid")
//     private String interactiveActivityId;

//     @NotBlank
//     private String interactiveActivityName;

//     private int interactiveActivityNumber;

//     @Column(columnDefinition = "TEXT")
//     private String intro;

//     @Column(columnDefinition = "TEXT")
//     private String materialsRequired;

//     @Column(columnDefinition = "TEXT")
//     private String keyOutcomes;

//     @Column(columnDefinition = "TEXT")
//     private String howToDoIt;

//     @Column(columnDefinition = "TEXT")
//     private String learningObjective;

//     private String duration;

//     @Column(columnDefinition = "TEXT")
//     private String image;

//     private Boolean submission;

//     private String ratings;

//     private String feedback;

//     private String tags;

//     @Column(columnDefinition = "TEXT")
//     private String bookRef;

//     @Column(columnDefinition = "TEXT")
//     private String videoRef;

//     private float progress;

//     @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//     @JoinColumn(name = "interactive_activity_id")
//     private List<Processes> processes;

//     // New Column
//     @Column(columnDefinition = "TEXT")
//     private String submoduleIdRef;

//     // New Column
//     @Column(columnDefinition = "TEXT")
//     private String firstProcessIdRef;
    
//  // 🆕 Newly Added Fields
//     @Column(columnDefinition = "TEXT")
//     private String submissionType;

//     @Column(columnDefinition = "TEXT")
//     private String submissionLink;

//     @Column(columnDefinition = "TEXT")
//     private String typeOfInteractiveActivity;
// }

package com.sensei.backend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "interactive_activity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteractiveActivity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_module_id", nullable = false)
    private SubModule subModule;

    @Column(name = "title", nullable = false, columnDefinition = "text")
    private String title;

    @Column(name = "learning_outcome", columnDefinition = "text")
    private String learningOutcome;

    @Column(name = "key_objectives", columnDefinition = "text")
    private String keyObjectives;

    @Column(name = "reference_video", columnDefinition = "text")
    private String referenceVideo;

    @Column(name = "cover_image", columnDefinition = "text")
    private String coverImage;

    @Column(name = "objective", columnDefinition = "text")
    private String objective;

    @Column(name = "activity_type", columnDefinition = "text")
    private String activityType;    

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
