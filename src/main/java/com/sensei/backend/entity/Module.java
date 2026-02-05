// package com.sensei.backend.entity;

// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import org.hibernate.annotations.GenericGenerator;

// import javax.persistence.*;
// import javax.validation.constraints.NotBlank;
// import java.util.List;

// @Entity
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// public class Module {
//     @Id
//     @GeneratedValue(generator = "system-uuid")
//     @GenericGenerator(name="system-uuid",strategy = "uuid")
//     private String moduleId;

//     @NotBlank
//     private String moduleName;

//     private int moduleNumber;

//     @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//     @JoinColumn(name = "module_id")
//     private List<SubModule> subModules;

//     // New Column
//     @Column(columnDefinition = "TEXT")
//     private String subjectIdRef;

//     // New Column
// //    @Column(columnDefinition = "TEXT")
// //    private String submoduleIdRef;

//     @ElementCollection
//     @Column(columnDefinition = "TEXT")
//     private List<String> submoduleIdRef; // Store multiple submodule IDs
// }

package com.sensei.backend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "module")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Module {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    // FK → subject.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
