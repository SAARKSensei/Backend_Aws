package com.sensei.backend.repository;

import com.sensei.backend.entity.Processes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessesRepository extends JpaRepository<Processes, String> {
}
