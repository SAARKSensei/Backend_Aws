package com.sensei.backend.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "referral_activity")
public class ReferralActivity {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 36, updatable = false, nullable = false)
	private String id;

    // Use referralCodeId (Long) instead of ReferralCode entity
    @Column(name = "referral_code_id", nullable = false)
    private String referralCodeId;

    // Use userId (Long) instead of User entity
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getReferralCodeId() { return referralCodeId; }
    public void setReferralCodeId(String referralCodeId) { this.referralCodeId = referralCodeId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }



}