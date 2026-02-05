package com.sensei.backend.repository;

import com.sensei.backend.entity.ReferralCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReferralCodeRepository
        extends JpaRepository<ReferralCode, UUID> {

    Optional<ReferralCode> findByParentId(UUID parentId);

    Optional<ReferralCode> findByCodeAndActiveTrue(String code);
}
