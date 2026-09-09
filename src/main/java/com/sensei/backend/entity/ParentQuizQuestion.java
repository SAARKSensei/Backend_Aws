package com.sensei.backend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "parent_quiz_question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentQuizQuestion {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "question_text", nullable = false, columnDefinition = "text")
    private String questionText;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
