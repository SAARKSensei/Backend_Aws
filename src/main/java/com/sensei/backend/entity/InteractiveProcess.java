// package com.sensei.backend.entity;

// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import org.hibernate.annotations.GenericGenerator;

// import javax.persistence.*;
// import javax.validation.constraints.NotBlank;

// @Entity
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// public class Processes {

//     @Id
//     @GeneratedValue(generator = "system-uuid")
//     @GenericGenerator(name="system-uuid", strategy = "uuid")
//     private String processId;

//     @NotBlank
//     private String processName;

//     private int processNumber;

//     @Column(columnDefinition = "TEXT")
//     private String senseiMessage;

//     // Renamed from parentMessage → childMessage
//     @Column(name = "child_message", columnDefinition = "TEXT")
//     private String childMessage;

//     @Column(columnDefinition = "TEXT")
//     private String image;

//     private int interactiveActivityRef;

//     @Column(columnDefinition = "TEXT")
//     private String interactiveActivityIdRef;

//     @Column(columnDefinition = "TEXT")
//     private String nextProcessIdRef;

//     // NEW COLUMN
//     @Column(columnDefinition = "TEXT")
//     private String hint;

// }
package com.sensei.backend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    // FK → interactive_activity.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interactive_activity_id", nullable = false)
    private InteractiveActivity interactiveActivity;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @Column(name = "sensei_message", columnDefinition = "text")
    private String senseiMessage;

    @Column(name = "hint", columnDefinition = "text")
    private String hint;

    // Free text for now (can be JSON later if needed)
    @Column(name = "child_task", columnDefinition = "text")
    private String childTask;

    @Column(name = "media_url", columnDefinition = "text")
    private String mediaUrl;

    // Optional denormalized FK (kept for performance)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_module_id")
    private SubModule subModule;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
