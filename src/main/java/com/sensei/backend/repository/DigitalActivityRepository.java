package com.sensei.backend.repository;

import com.sensei.backend.entity.DigitalActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DigitalActivityRepository extends JpaRepository<DigitalActivity, String> {
}
