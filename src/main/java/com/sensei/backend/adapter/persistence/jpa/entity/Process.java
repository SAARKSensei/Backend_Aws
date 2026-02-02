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
package com.sensei.backend.adapter.persistence.jpa.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "interactive_process")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Process {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    // FK → interactive_activity.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interactive_activity_id", nullable = false)
    private InteractiveActivity interactiveActivity;

    @Column(name = "sensei_message", nullable = false, columnDefinition = "text")
    private String senseiMessage;

    @Column(name = "hint", columnDefinition = "text")
    private String hint;

    @ElementCollection
    @CollectionTable(name = "process_child_steps", joinColumns = @JoinColumn(name = "process_id"))
    @Column(name = "child_step", columnDefinition = "text")
    private List<String> childSteps;

    @Column(name = "media_url", columnDefinition = "text")
    private String mediaUrl;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
