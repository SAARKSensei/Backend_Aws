package com.sensei.backend.adapter.persistence.jpa.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "child_interactive_activity_progress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildInteractiveActivityProgress {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(name = "child_id", nullable = false, columnDefinition = "uuid")
    private UUID childId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interactive_activity_id", nullable = false)
    private InteractiveActivity interactiveActivity;

    @Column(nullable = false)
    private String status;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
