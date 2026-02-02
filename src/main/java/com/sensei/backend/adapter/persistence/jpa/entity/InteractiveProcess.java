package com.sensei.backend.adapter.persistence.jpa.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "interactive_process")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteractiveProcess {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "interactive_activity_id", nullable = false)
    private UUID interactiveActivityId;

    @Column(name = "sub_module_id", nullable = false)
    private UUID subModuleId;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @Column(name = "sensei_message", columnDefinition = "text")
    private String senseiMessage;

    @Column(name = "hint", columnDefinition = "text")
    private String hint;

    @Column(name = "child_task", columnDefinition = "text")
    private String childTask;

    @Column(name = "media_url", columnDefinition = "text")
    private String mediaUrl;

    @Column(name = "child_id")
    private UUID childId;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "status")
    private String status; // IN_PROGRESS / COMPLETED
}
