package com.sensei.backend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "parent_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentUser {

    @Id
    @GeneratedValue
    @Column(name = "parent_id")
    private UUID parentId;

    private String name;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(unique = true)
    private String email;

    private String phone;
    private String password;
    private String maritalStatus;
    private String occupation;
    private LocalDate dateOfBirth;

    private String relationWithChildren;

    // Spouse Info
    private String spouseName;
    private String spouseGender;
    private String spouseEmail;
    private String spousePhone;
    private String spouseOccupation;
    private LocalDate spouseDateOfBirth;
    private String spouseRelationWithChild;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 🔗 FIXED mappedBy
    @OneToMany(mappedBy = "parentUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ChildUser> childUsers = new ArrayList<>();

    // ✅ NEW FIELD
    @Column(name = "location")
    private String location;
}

