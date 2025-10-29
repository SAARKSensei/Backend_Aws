package com.sensei.backend.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "referral_codes")
public class ReferralCode {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 36, updatable = false, nullable = false)
	private String id;

    private String referrerUserId;
    private String referredUserId;
    private String code;
    private BigDecimal bonusAmount;

    // ✅ Add this field
    @Column(name = "is_school", nullable = false)
    private Boolean isSchool;
    

    
    // Getters
    public String getId() { return id; }
    public String getReferrerUserId() { return referrerUserId; }
    public String getReferredUserId() { return referredUserId; }
    public String getCode() { return code; }
    public BigDecimal getBonusAmount() { return bonusAmount; }
    public Boolean getIsSchool() { return isSchool; }

    // Setters
    public void setReferrerUserId(String referrerUserId) { this.referrerUserId = referrerUserId; }
    public void setReferredUserId(String referredUserId) { this.referredUserId = referredUserId; }
    public void setCode(String code) { this.code = code; }
    public void setBonusAmount(BigDecimal bonusAmount) { this.bonusAmount = bonusAmount; }
    public void setIsSchool(Boolean isSchool) { this.isSchool = isSchool; }
}
