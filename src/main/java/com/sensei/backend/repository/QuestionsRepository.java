package com.sensei.backend.repository;

import com.sensei.backend.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionsRepository extends JpaRepository<Questions, String> {

    // ✅ Add this method
    List<Questions> findByDigitalActivity_DigitalActivityId(String digitalActivityId);
}
