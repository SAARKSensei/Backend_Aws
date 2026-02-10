package com.sensei.backend.dto.referral;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferralUsageResponseDTO {

    private UUID referralCodeId;
    private UUID referrerParentId;
    private UUID referredParentId;
    private Integer rewardAmount;
    private LocalDateTime usedAt;
}
