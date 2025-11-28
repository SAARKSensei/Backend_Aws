package com.sensei.backend.repository;

import com.sensei.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {

    // ✅ Add this method
    List<Question> findByDigitalActivity_DigitalActivityId(String digitalActivityId);
}
