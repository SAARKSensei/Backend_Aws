package com.sensei.backend.dto.referral;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferralCodeResponseDTO {

    private UUID id;
    private UUID parentId;
    private String code;
    private Integer usedCount;
    private Integer maxUsage;
    private Boolean active;
}
