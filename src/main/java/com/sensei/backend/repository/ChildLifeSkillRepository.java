package com.sensei.backend.repository;

import com.sensei.backend.entity.ChildLifeSkill;
import com.sensei.backend.enums.LifeSkillType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChildLifeSkillRepository extends JpaRepository<ChildLifeSkill, UUID> {
    List<ChildLifeSkill> findByChildUser_ChildId(UUID childId);
    Optional<ChildLifeSkill> findByChildUser_ChildIdAndLifeSkill(UUID childId, LifeSkillType lifeSkill);
}
