package com.sensei.backend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import com.sensei.backend.enums.LifeSkillType;

@Entity
@Table(name = "child_life_skill")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildLifeSkill {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildUser childUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "life_skill", nullable = false)
    private LifeSkillType lifeSkill;

    @Column(name = "score", nullable = false)
    @Builder.Default
    private Integer score = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
