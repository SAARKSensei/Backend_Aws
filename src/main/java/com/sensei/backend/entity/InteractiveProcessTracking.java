package com.sensei.backend.entity;

import com.sensei.backend.enums.ProcessTrackingStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "interactive_process_tracking",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"child_id", "interactive_process_id"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteractiveProcessTracking {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildUser child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interactive_process_id", nullable = false)
    private InteractiveProcess interactiveProcess;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcessTrackingStatus status;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
