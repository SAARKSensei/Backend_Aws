package com.sensei.backend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pricing_plan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricingPlan {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private String name;

    /**
     * Price in paise (₹499 = 49900)
     */
    @Column(nullable = false)
    private Integer price;

    /**
     * Duration of plan in months
     */
    @Column(name = "duration_in_months", nullable = false)
    private Integer durationMonths;

    private String grade;

    /**
     * ACTIVE / INACTIVE
     */
    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "text")
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
