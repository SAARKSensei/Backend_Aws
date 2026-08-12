// package com.sensei.backend.service;

// import com.sensei.backend.dto.QuizResultDTO;
// import com.sensei.backend.entity.LifeSkill;
// import com.sensei.backend.entity.QuizAnswer;
// import com.sensei.backend.entity.QuizResult;
// import com.sensei.backend.repository.LifeSkillRepository;
// import com.sensei.backend.repository.QuizAnswerRepository;
// import com.sensei.backend.repository.QuizResultRepository;
// import com.sensei.backend.repository.QuestiontableRepository;
// import org.springframework.stereotype.Service;

// import jakarta.transaction.Transactional;
// import java.util.*;

// @Transactional
// @Service
// public class QuizResultService {

//     private final QuizResultRepository quizResultRepository;
//     private final QuizAnswerRepository quizAnswerRepository;
//     private final LifeSkillRepository lifeSkillRepository;
//     private final QuestiontableRepository questiontableRepository;

//     public QuizResultService(QuizResultRepository quizResultRepository,
//                              QuizAnswerRepository quizAnswerRepository,
//                              LifeSkillRepository lifeSkillRepository,
//                              QuestiontableRepository questiontableRepository) {
//         this.quizResultRepository = quizResultRepository;
//         this.quizAnswerRepository = quizAnswerRepository;
//         this.lifeSkillRepository = lifeSkillRepository;
//         this.questiontableRepository = questiontableRepository;
//     }

//     // ✅ Save quiz and return summary
//     public Map<String, Object> saveQuizResultAndGetSummary(QuizResultDTO quizResultDTO) {
//         System.out.println("✅ Quiz submitted for childId: " + quizResultDTO.getChildId());

//         // 🧹 Step 1: Delete old results for the same child
//         quizResultRepository.deleteByChildId(quizResultDTO.getChildId());

//         // 🧩 Step 2: Create new QuizResult entity
//         QuizResult quizResult = new QuizResult();
//         quizResult.setChildId(quizResultDTO.getChildId());

//         // 🧠 Step 3: Collect selected answers from map (questionId -> answerId)
//         Map<UUID, UUID> answerSelections = quizResultDTO.getAnswerSelections();
//         if (answerSelections == null || answerSelections.isEmpty()) {
//             throw new RuntimeException("No answers submitted!");
//         }

//         List<QuizAnswer> selectedAnswers = new ArrayList<>();

//         for (Map.Entry<UUID, UUID> entry : answerSelections.entrySet()) {
//             UUID answerId = entry.getValue();
//             quizAnswerRepository.findById(answerId.toString()).ifPresent(selectedAnswers::add);
//         }

//         // 🗂️ Step 4: Save quiz result with selected answers
//         quizResult.setSelectedAnswers(selectedAnswers);
//         quizResultRepository.save(quizResult);

//         // ✅ Step 5: Generate summary (strong / needAttention)
//         return getQuizSummaryBasedOnSelections(selectedAnswers);
//     }

//     // ✅ Generate summary directly from selected answers
//     private Map<String, Object> getQuizSummaryBasedOnSelections(List<QuizAnswer> selectedAnswers) {
//         Map<String, Object> summary = new HashMap<>();

//         Set<String> strong = new LinkedHashSet<>();
//         Set<String> needAttention = new LinkedHashSet<>();

//         // ✅ Step 1: Collect life skills from selected answers
//         for (QuizAnswer answer : selectedAnswers) {
//             if (answer.getLifeSkill() != null) {
//                 strong.add(answer.getLifeSkill().getLifeskillName());
//             }
//         }

//         // ✅ Step 2: Get all life skills from DB and find which are missing
//         List<LifeSkill> allSkills = lifeSkillRepository.findAll();
//         for (LifeSkill skill : allSkills) {
//             if (!strong.contains(skill.getLifeskillName())) {
//                 needAttention.add(skill.getLifeskillName());
//             }
//         }

//         summary.put("strong", new ArrayList<>(strong));
//         summary.put("needAttention", new ArrayList<>(needAttention));

//         return summary;
//     }
// }

