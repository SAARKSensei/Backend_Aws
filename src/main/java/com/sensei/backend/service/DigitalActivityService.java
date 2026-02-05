// package com.sensei.backend.service;

// import com.sensei.backend.dto.DigitalActivityDTO;
// import com.sensei.backend.dto.QuestionsDTO;
// import com.sensei.backend.entity.DigitalActivity;
// import com.sensei.backend.entity.Questions;
// import com.sensei.backend.repository.DigitalActivityRepository;
// import com.sensei.backend.repository.QuestionsRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.*;
// import java.util.stream.Collectors;

// @Service
// public class DigitalActivityService {

//     @Autowired
//     private DigitalActivityRepository digitalActivityRepository;

//     @Autowired
//     private QuestionsService questionsService;

//     @Autowired
//     private QuestionsRepository questionsRepository;

//     // Create DigitalActivity with questions
//     public DigitalActivityDTO createDigitalActivity(DigitalActivityDTO dto) {
//         DigitalActivity entity = new DigitalActivity();
//         entity.setDigitalActivityName(dto.getDigitalActivityName());
//         entity.setKeyOutcomes(dto.getKeyOutcomes());
//         entity.setImage(dto.getImage());
//         entity.setRatings(dto.getRatings());
//         entity.setFeedback(dto.getFeedback());
//         entity.setProgress(dto.getProgress());
//         entity.setTags(dto.getTags());
//         entity.setSubmoduleIdRef(dto.getSubmoduleIdRef());
//         entity.setFirstQuestionIdRef(dto.getFirstQuestionIdRef());

//         DigitalActivity saved = digitalActivityRepository.save(entity);

//         if (dto.getQuestions() != null && !dto.getQuestions().isEmpty()) {
//             List<Questions> savedQuestions = new ArrayList<>();
//             for (QuestionsDTO qdto : dto.getQuestions()) {
//                 Questions q = new Questions();
//                 q.setQuestion(qdto.getQuestionName());
//                 q.setSenseiQuestion(qdto.getSenseiQuestion());
//                 q.setSenseiAnswer(qdto.getSenseiAnswer());
//                 q.setCorrectAnswerDescription(qdto.getCorrectAnswerDescription());
//                 q.setIncorrectAnswerDescription(qdto.getIncorrectAnswerDescription());
//                 q.setQuestionImage(qdto.getQuestionImage());
//                 q.setQuestionNumber(qdto.getQuestionNumber());
//                 q.setOption1(qdto.getOption1());
//                 q.setOption1CounsellorNote(qdto.getOption1CounsellorNote());
//                 q.setOption1Hint(null);  // ❌ HINTS DISABLED
//                 q.setOption2(qdto.getOption2());
//                 q.setOption2CounsellorNote(qdto.getOption2CounsellorNote());
//                 q.setOption2Hint(null);  // ❌ HINTS DISABLED
//                 q.setOption3(qdto.getOption3());
//                 q.setOption3CounsellorNote(qdto.getOption3CounsellorNote());
//                 q.setOption3Hint(null);  // ❌ HINTS DISABLED
//                 q.setDigitalActivity(saved);

//                 questionsService.autoSetOptionStatuses(q);
//                 savedQuestions.add(q);
//             }
//             saved.setQuestions(savedQuestions);
//             saved = digitalActivityRepository.save(saved);
//         }

//         return mapToDTO(saved);
//     }

//     // Get all DigitalActivities
//     public List<DigitalActivityDTO> getAllDigitalActivities() {
//         List<DigitalActivity> activities = digitalActivityRepository.findAll();

//         return activities.stream().map(activity -> {
//             DigitalActivityDTO dto = mapToDTO(activity);

//             List<QuestionsDTO> relatedQuestions = questionsRepository
//                     .findByDigitalActivity_DigitalActivityId(activity.getDigitalActivityId())
//                     .stream()
//                     .map(questionsService::convertToDTOWithoutHints) // ❌ NO HINTS
//                     .collect(Collectors.toList());

//             dto.setQuestions(relatedQuestions);
//             return dto;
//         }).collect(Collectors.toList());
//     }

//     // Get DigitalActivity by ID
//     public Optional<DigitalActivityDTO> getDigitalActivityById(String id) {
//         return digitalActivityRepository.findById(id).map(activity -> {
//             DigitalActivityDTO dto = mapToDTO(activity);

//             List<QuestionsDTO> relatedQuestions = questionsRepository
//                     .findByDigitalActivity_DigitalActivityId(activity.getDigitalActivityId())
//                     .stream()
//                     .map(questionsService::convertToDTOWithoutHints) // ❌ NO HINTS
//                     .collect(Collectors.toList());

//             dto.setQuestions(relatedQuestions);
//             return dto;
//         });
//     }

//     // Update DigitalActivity
//     public DigitalActivityDTO updateDigitalActivity(String id, DigitalActivityDTO dto) {
//         DigitalActivity existing = digitalActivityRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("DigitalActivity not found with id: " + id));

//         existing.setDigitalActivityName(dto.getDigitalActivityName());
//         existing.setKeyOutcomes(dto.getKeyOutcomes());
//         existing.setImage(dto.getImage());
//         existing.setRatings(dto.getRatings());
//         existing.setFeedback(dto.getFeedback());
//         existing.setProgress(dto.getProgress());
//         existing.setTags(dto.getTags());
//         existing.setSubmoduleIdRef(dto.getSubmoduleIdRef());
//         existing.setFirstQuestionIdRef(dto.getFirstQuestionIdRef());

//         if (dto.getQuestions() != null && !dto.getQuestions().isEmpty()) {
//             List<Questions> updatedQuestions = new ArrayList<>();

//             for (QuestionsDTO qdto : dto.getQuestions()) {
//                 Questions question = questionsRepository.findById(qdto.getQuestionId())
//                         .orElse(new Questions());

//                 question.setQuestion(qdto.getQuestionName());
//                 question.setSenseiQuestion(qdto.getSenseiQuestion());
//                 question.setSenseiAnswer(qdto.getSenseiAnswer());
//                 question.setCorrectAnswerDescription(qdto.getCorrectAnswerDescription());
//                 question.setIncorrectAnswerDescription(qdto.getIncorrectAnswerDescription());
//                 question.setQuestionImage(qdto.getQuestionImage());
//                 question.setQuestionNumber(qdto.getQuestionNumber());
//                 question.setOption1(qdto.getOption1());
//                 question.setOption1CounsellorNote(qdto.getOption1CounsellorNote());
//                 question.setOption1Hint(null);   // ❌ NO HINTS
//                 question.setOption2(qdto.getOption2());
//                 question.setOption2CounsellorNote(qdto.getOption2CounsellorNote());
//                 question.setOption2Hint(null);   // ❌ NO HINTS
//                 question.setOption3(qdto.getOption3());
//                 question.setOption3CounsellorNote(qdto.getOption3CounsellorNote());
//                 question.setOption3Hint(null);   // ❌ NO HINTS
//                 question.setDigitalActivity(existing);

//                 questionsService.autoSetOptionStatuses(question);
//                 updatedQuestions.add(question);
//             }

//             existing.setQuestions(updatedQuestions);
//         }

//         DigitalActivity saved = digitalActivityRepository.save(existing);
//         return mapToDTO(saved);
//     }

//     // Delete DigitalActivity
//     public void deleteDigitalActivity(String id) {
//         if (!digitalActivityRepository.existsById(id)) {
//             throw new RuntimeException("DigitalActivity not found with id: " + id);
//         }

//         questionsRepository.deleteAll(
//                 questionsRepository.findByDigitalActivity_DigitalActivityId(id)
//         );

//         digitalActivityRepository.deleteById(id);
//     }

//     // Mapper
//     private DigitalActivityDTO mapToDTO(DigitalActivity activity) {
//         DigitalActivityDTO dto = new DigitalActivityDTO();
//         dto.setDigitalActivityId(activity.getDigitalActivityId());
//         dto.setDigitalActivityName(activity.getDigitalActivityName());
//         dto.setKeyOutcomes(activity.getKeyOutcomes());
//         dto.setImage(activity.getImage());
//         dto.setRatings(activity.getRatings());
//         dto.setFeedback(activity.getFeedback());
//         dto.setProgress(activity.getProgress());
//         dto.setTags(activity.getTags());
//         dto.setSubmoduleIdRef(activity.getSubmoduleIdRef());
//         dto.setFirstQuestionIdRef(activity.getFirstQuestionIdRef());

//         List<QuestionsDTO> questions = activity.getQuestions() != null
//                 ? activity.getQuestions().stream()
//                         .map(questionsService::convertToDTOWithoutHints)  // ❌ NO HINTS
//                         .collect(Collectors.toList())
//                 : new ArrayList<>();

//         dto.setQuestions(questions);
//         dto.setNoOfQuestions(questions.size()); // dynamic no of questions

//         return dto;
//     }
// }

package com.sensei.backend.service;

import com.sensei.backend.dto.digitalactivity.*;

import java.util.List;
import java.util.UUID;

public interface DigitalActivityService {

    DigitalActivityResponseDTO create(DigitalActivityRequestDTO dto);

    DigitalActivityResponseDTO update(UUID id, DigitalActivityRequestDTO dto);

    void delete(UUID id);

    DigitalActivityResponseDTO getById(UUID id);

    List<DigitalActivityResponseDTO> getBySubModule(UUID subModuleId);

    UUID getSubjectIdBySubModule(UUID subModuleId);

}

