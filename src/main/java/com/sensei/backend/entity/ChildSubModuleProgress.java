package com.sensei.backend.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "child_submodule_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"child_id", "sub_module_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildSubModuleProgress {

    @Id
    @GeneratedValue
    @Column(updatable = false)
    private UUID id;

    @Column(name = "child_id", nullable = false)
    private UUID childId;

    @Column(name = "sub_module_id", nullable = false)
    private UUID subModuleId;

    @Column(name = "completed_activities")
    private Integer completedActivities;

    @Column(name = "total_activities")
    private Integer totalActivities;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
