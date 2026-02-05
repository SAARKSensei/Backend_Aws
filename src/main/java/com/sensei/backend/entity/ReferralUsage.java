package com.sensei.backend.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "referral_usage",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"referral_code_id", "referred_parent_id"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferralUsage {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "referral_code_id", nullable = false)
    private UUID referralCodeId;

    @Column(name = "referrer_parent_id", nullable = false)
    private UUID referrerParentId;

    @Column(name = "referred_parent_id", nullable = false)
    private UUID referredParentId;

    @Column(name = "reward_amount", nullable = false)
    private Integer rewardAmount;

    @Column(name = "used_at", nullable = false)
    private LocalDateTime usedAt;
}
