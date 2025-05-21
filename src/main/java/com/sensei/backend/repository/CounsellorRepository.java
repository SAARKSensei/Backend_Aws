package com.sensei.backend.repository;

import com.sensei.backend.entity.Counsellor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounsellorRepository extends JpaRepository<Counsellor, String> {
}
