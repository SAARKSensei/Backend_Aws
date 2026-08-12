package com.sensei.backend.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "child_digital_activity_progress",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"child_id", "digital_activity_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildDigitalActivityProgress {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "child_id", nullable = false)
    private UUID childId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "digital_activity_id", nullable = false)
    private DigitalActivity digitalActivity;

    @Column(name = "status", nullable = false)
    private String status; // STARTED / COMPLETED

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
