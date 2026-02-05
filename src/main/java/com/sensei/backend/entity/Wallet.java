package com.sensei.backend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    // 🔗 Parent owns wallet (1–1)
    @Column(name = "parent_id", nullable = false, unique = true)
    private UUID parentId;

    // Stored in RUPEES (int, no decimals)
    private Integer balance;

    @Column(length = 20)
    private String status; // ACTIVE / BLOCKED

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
