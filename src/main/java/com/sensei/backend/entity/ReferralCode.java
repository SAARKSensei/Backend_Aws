package com.sensei.backend.entity;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "referral_codes")
public class ReferralCode {

    // @Id
    // @GeneratedValue(generator = "system-uuid")
    // @GenericGenerator(name = "system-uuid", strategy = "uuid")
    // @Column(length = 36, updatable = false, nullable = false)
    // private String id;
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;


    @Column(name = "referrer_user_id", nullable = false)
    private String referrerUserId;  // User who owns this code

    @Column(name = "code", unique = true, nullable = false, length = 20)
    private String code;  // Auto-generated referral code (e.g., "SHAAN8472")

    @Column(name = "usage_count", nullable = false)
    private Integer usageCount = 0;  // How many times this code has been used

    @Column(name = "max_usage_limit", nullable = false)
    private Integer maxUsageLimit = 5;  // Maximum 5 uses

    @Column(name = "is_school", nullable = false)
    private Boolean isSchool = false;  // Special flag for school codes

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getReferrerUserId() { return referrerUserId; }
    public void setReferrerUserId(String referrerUserId) { this.referrerUserId = referrerUserId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Integer getUsageCount() { return usageCount; }
    public void setUsageCount(Integer usageCount) { this.usageCount = usageCount; }

    public Integer getMaxUsageLimit() { return maxUsageLimit; }
    public void setMaxUsageLimit(Integer maxUsageLimit) { this.maxUsageLimit = maxUsageLimit; }

    public Boolean getIsSchool() { return isSchool; }
    public void setIsSchool(Boolean isSchool) { this.isSchool = isSchool; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
