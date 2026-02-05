// package com.sensei.backend.service;




// import com.sensei.backend.entity.UserFeedback;
// import com.sensei.backend.repository.UserFeedbackRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;

// import javax.persistence.EntityNotFoundException;
// import java.util.List;

// @Service
// @RequiredArgsConstructor
// public class UserFeedbackService {

//     private final UserFeedbackRepository userFeedbackRepository;



//     // Create Feedback
//     public UserFeedback createFeedback(UserFeedback feedback) {
//         return userFeedbackRepository.save(feedback);
//     }

//     // Get All Feedback
//     public List<UserFeedback> getAllFeedback() {
//         return userFeedbackRepository.findAll();
//     }

//     // Get Feedback by ID
//     public UserFeedback getFeedbackById(Long id) {
//         return userFeedbackRepository.findById(id)
//                 .orElseThrow(() -> new EntityNotFoundException("Feedback not found with id: " + id));
//     }

//     // Update Feedback
//     public UserFeedback updateFeedback(Long id, UserFeedback updatedFeedback) {
//         UserFeedback existingFeedback = getFeedbackById(id);

//         existingFeedback.setActivityId(updatedFeedback.getActivityId());
//         existingFeedback.setActivityName(updatedFeedback.getActivityName());
//         existingFeedback.setRating(updatedFeedback.getRating());
//         existingFeedback.setMessage(updatedFeedback.getMessage());
//         existingFeedback.setParentName(updatedFeedback.getParentName());
//         existingFeedback.setChildName(updatedFeedback.getChildName());
//         existingFeedback.setEmailId(updatedFeedback.getEmailId());

//         return userFeedbackRepository.save(existingFeedback);
//     }

//     // Delete Feedback
//     public void deleteFeedback(Long id) {
//         if (!userFeedbackRepository.existsById(id)) {
//             throw new EntityNotFoundException("Feedback not found with id: " + id);
//         }
//         userFeedbackRepository.deleteById(id);
//     }
// }