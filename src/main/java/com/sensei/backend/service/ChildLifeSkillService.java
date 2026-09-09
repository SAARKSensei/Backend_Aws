package com.sensei.backend.service;

import com.sensei.backend.dto.lifeskills.ChildLifeSkillReportResponse;
import com.sensei.backend.enums.LifeSkillType;

import java.util.UUID;

public interface ChildLifeSkillService {
    ChildLifeSkillReportResponse getChildLifeSkills(UUID childId);
    void addLifeSkillPoints(UUID childId, LifeSkillType lifeSkill, int points);
}
