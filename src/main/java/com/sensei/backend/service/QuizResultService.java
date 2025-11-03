package com.sensei.backend.service;

import com.sensei.backend.dto.QuizResultDTO;
import com.sensei.backend.entity.LifeSkill;
import com.sensei.backend.entity.QuizAnswer;
import com.sensei.backend.entity.QuizResult;
import com.sensei.backend.repository.LifeSkillRepository;
import com.sensei.backend.repository.QuizAnswerRepository;
import com.sensei.backend.repository.QuizResultRepository;
import com.sensei.backend.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizResultService {

    private final QuizResultRepository quizResultRepository;
    private final QuizAnswerRepository quizAnswerRepository;
    private final LifeSkillRepository lifeSkillRepository;
    private final QuestionRepository questionRepository;

    public QuizResultService(QuizResultRepository quizResultRepository,
                             QuizAnswerRepository quizAnswerRepository,
                             LifeSkillRepository lifeSkillRepository,
                             QuestionRepository questionRepository) {
        this.quizResultRepository = quizResultRepository;
        this.quizAnswerRepository = quizAnswerRepository;
        this.lifeSkillRepository = lifeSkillRepository;
        this.questionRepository = questionRepository;
    }

    // ✅ Save quiz and return summary
    public Map<String, Object> saveQuizResultAndGetSummary(QuizResultDTO quizResultDTO) {
        System.out.println("✅ Quiz submitted for childId: " + quizResultDTO.getChildId());

        // 🧹 Step 1: Delete old results for the same child (to avoid duplication)
        quizResultRepository.deleteByChildId(quizResultDTO.getChildId());

        // 🧩 Step 2: Create new QuizResult entity
        QuizResult quizResult = new QuizResult();
        quizResult.setChildId(quizResultDTO.getChildId());

        // 🧠 Step 3: Collect selected answers from the map (questionId -> answerId)
        Map<UUID, UUID> answerSelections = quizResultDTO.getAnswerSelections();
        if (answerSelections == null || answerSelections.isEmpty()) {
            throw new RuntimeException("No answers submitted!");
        }

        List<QuizAnswer> selectedAnswers = new ArrayList<>();

        for (Map.Entry<UUID, UUID> entry : answerSelections.entrySet()) {
            UUID questionId = entry.getKey();
            UUID answerId = entry.getValue();

            quizAnswerRepository.findById(answerId.toString()).ifPresent(selectedAnswers::add);
        }

        // 🗂️ Step 4: Save quiz result with selected answers
        quizResult.setSelectedAnswers(selectedAnswers);
        quizResultRepository.save(quizResult);

        // ✅ Step 5: Return summary (strong / needAttention)
        return getQuizSummary(quizResultDTO.getChildId());
    }


    // ✅ Generate summary of Life Skills
    public Map<String, Object> getQuizSummary(String childId) {
        Map<String, Object> summary = new HashMap<>();

        Map<String, Integer> skillScores = getLifeSkillScores(childId);

        Set<String> strong = new LinkedHashSet<>();
        Set<String> needAttention = new LinkedHashSet<>();

        // Fetch all life skills from DB
        List<LifeSkill> allSkills = lifeSkillRepository.findAll();

        for (LifeSkill skill : allSkills) {
            String skillName = skill.getLifeskillName();
            int score = skillScores.getOrDefault(skillName, 0);

            if (score >= 1) {
                strong.add(skillName);
            } else {
                needAttention.add(skillName);
            }
        }

        summary.put("strong", new ArrayList<>(strong));
        summary.put("needAttention", new ArrayList<>(needAttention));

        return summary;
    }

    // ✅ Calculate how many times each life skill appears
    public Map<String, Integer> getLifeSkillScores(String childId) {
        List<QuizResult> results = quizResultRepository.findByChildId(childId);
        Map<String, Integer> scoreMap = new HashMap<>();

        for (QuizResult result : results) {
            for (QuizAnswer answer : result.getSelectedAnswers()) {
                if (answer.getLifeSkill() != null) {
                    String skillName = answer.getLifeSkill().getLifeskillName();
                    scoreMap.put(skillName, scoreMap.getOrDefault(skillName, 0) + 1);
                }
            }
        }

        return scoreMap;
    }
}
