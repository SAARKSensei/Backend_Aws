package com.sensei.backend.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "child_module_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"child_id", "module_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildModuleProgress {

    @Id
    @GeneratedValue
    @Column(updatable = false)
    private UUID id;

    @Column(name = "child_id", nullable = false)
    private UUID childId;

    @Column(name = "module_id", nullable = false)
    private UUID moduleId;

    @Column(name = "completed_submodules")
    private Integer completedSubmodules;

    @Column(name = "total_submodules")
    private Integer totalSubmodules;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
