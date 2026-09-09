package com.sensei.backend.dto.lifeskills;

import com.sensei.backend.enums.LifeSkillType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildLifeSkillResponse {
    private LifeSkillType lifeSkill;
    private Boolean isAchieved;
}
