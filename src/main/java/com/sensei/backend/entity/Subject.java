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
// public class Subject {
//     @Id
//     @GeneratedValue(generator = "system-uuid")
//     @GenericGenerator(name="system-uuid",strategy = "uuid")
//     private String subjectId;

//     @NotBlank
//     private String subjectName;

//     @NotBlank
//     private String ageGroup;

//     private int duration;

//     private int progress;

//     @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//     @JoinColumn(name = "subject_id")
//     private List<Module> modules;

//     // New Column
//     @Column(columnDefinition = "TEXT")
//     private String moduleIdRef;

//     public String getName() {
//         return subjectName;
//     }  // Added by Vaishnav Kale
// }
package com.sensei.backend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "subject")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "icon_url", columnDefinition = "text")
    private String iconUrl;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
