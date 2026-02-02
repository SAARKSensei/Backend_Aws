// package com.sensei.backend.entity;

// import lombok.*;
// import javax.persistence.*;
// import java.util.UUID;
// import java.time.LocalDateTime;

// @Entity
// @Table(name = "process_child_step")
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class ProcessChildStep {

//     @Id
//     @GeneratedValue
//     @Column(columnDefinition = "uuid")
//     private UUID id;

//     @Column(name = "process_id", nullable = false)
//     private UUID processId;

//     @Column(name = "activity_id", nullable = false)
//     private UUID activityId;

//     @Column(name = "activity_type", nullable = false)
//     private String activityType; // INTERACTIVE / DIGITAL / QUIZ

//     @Column(name = "status")
//     private String status; // STARTED / COMPLETED

//     @Column(name = "started_at")
//     private LocalDateTime startedAt;

//     @Column(name = "completed_at")
//     private LocalDateTime completedAt;
// }
package com.sensei.backend.adapter.persistence.jpa.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "process_child_step")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessChildStep {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id", nullable = false)
    private InteractiveProcess process;

    @Column(name = "step_order")
    private Integer stepOrder;

    @Column(name = "step_text", columnDefinition = "text")
    private String stepText;

    @Column(name = "activity_id")
    private UUID activityId;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "status")
    private String status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
