package com.sensei.backend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import com.sensei.backend.enums.LifeSkillType;

@Entity
@Table(name = "parent_quiz_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentQuizOption {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private ParentQuizQuestion question;

    @Column(name = "option_text", nullable = false, columnDefinition = "text")
    private String optionText;

    @ElementCollection(targetClass = LifeSkillType.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "parent_quiz_option_life_skills", joinColumns = @JoinColumn(name = "option_id"))
    @Column(name = "life_skill")
    private java.util.List<LifeSkillType> associatedLifeSkills;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
