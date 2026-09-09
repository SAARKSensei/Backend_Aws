package com.sensei.backend.dto.parentquiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sensei.backend.enums.LifeSkillType;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentQuizOptionDto {
    private UUID optionId;
    private String optionText;
    private List<LifeSkillType> associatedLifeSkills;
}
