package com.sensei.backend.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "child_subject_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"child_id", "subject_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildSubjectProgress {

    @Id
    @GeneratedValue
    @Column(updatable = false)
    private UUID id;

    @Column(name = "child_id", nullable = false)
    private UUID childId;

    @Column(name = "subject_id", nullable = false)
    private UUID subjectId;

    @Column(name = "completed_modules")
    private Integer completedModules;

    @Column(name = "total_modules")
    private Integer totalModules;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
