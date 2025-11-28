package com.sensei.backend.repository;

import com.sensei.backend.entity.Campaign;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CampaignRepository extends JpaRepository<Campaign, String> {
	Optional<Campaign> findByName(String name);
}
