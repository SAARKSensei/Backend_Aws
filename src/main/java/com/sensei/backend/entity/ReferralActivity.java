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

    @Column(name = "referral_code_id", nullable = false)
    private String referralCodeId;  // Which referral code was used

    @Column(name = "referred_user_id", nullable = false)
    private String referredUserId;  // Who used the code

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getReferralCodeId() { return referralCodeId; }
    public void setReferralCodeId(String referralCodeId) { this.referralCodeId = referralCodeId; }

    public String getReferredUserId() { return referredUserId; }
    public void setReferredUserId(String referredUserId) { this.referredUserId = referredUserId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
