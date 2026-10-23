package com.sensei.backend.entity;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.sensei.backend.enums.PlanStatus;

@Entity
@Table(name = "child_user")
@org.hibernate.annotations.SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildUser {

    @Id
    @GeneratedValue
    @Column(
        name = "child_id",
        updatable = false,
        nullable = false
    )
    
    private UUID childId;

    @Column(name = "child_name")
    private String childName;

    private String gender;
    private String grade;

    @Column(name = "age_group")
    private String ageGroup;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "phone_number")
    private String phoneNumber;

    @org.hibernate.annotations.JdbcTypeCode(java.sql.Types.DATE)
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    // ✅ NEW FIELD
    @Column(name = "school_id")
    private String schoolName;

    @Column(name = "school_phone")
    private String schoolPhone;

    @Column(name = "school_email")
    private String schoolEmail;

    @Column(name = "school_address")
    private String schoolAddress;

    @Column(name = "medical_history")
    private String medicalHistory;

    private String pfp;
    
    // 🔗 Parent
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private ParentUser parentUser;

    // 🔐 Active plan reference
    @Column(name = "active_plan_id")
    private UUID activePlanId;

    @Column(name = "plan_start_date")
    private LocalDate planStartDate;

    @Column(name = "plan_expiry_date")
    private LocalDate planExpiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_status")
    private PlanStatus planStatus;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Cascading for quiz attempts if a child is deleted
    @OneToMany(mappedBy = "childUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private java.util.List<ParentQuizAttempt> quizAttempts = new java.util.ArrayList<>();

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
