package com.sensei.backend.dto.lifeskills;

import com.sensei.backend.enums.LifeSkillType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildLifeSkillReportResponse {
    private List<LifeSkillType> showing;
    private List<LifeSkillType> toDevelopFurther;
}
