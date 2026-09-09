package com.sensei.backend.dto.parentquiz;

import com.sensei.backend.enums.LifeSkillType;
import lombok.Data;

import java.util.List;

@Data
public class CreateQuizOptionDto {
    private String optionText;
    private List<LifeSkillType> lifeSkills;
}
