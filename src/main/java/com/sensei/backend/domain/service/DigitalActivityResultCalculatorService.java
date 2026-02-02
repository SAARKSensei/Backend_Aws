package com.sensei.backend.domain.service;
// package com.sensei.backend.service;

// import com.sensei.backend.dto.DigitalActivityResultResponseDTO;
// import com.sensei.backend.entity.DigitalActivityResultCalculator;
// import com.sensei.backend.repository.DigitalActivityResultCalculatorRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.time.Duration;
// import java.time.LocalDateTime;
// import java.util.concurrent.ThreadLocalRandom;

// @Service
// public class DigitalActivityResultCalculatorService {

//     @Autowired
//     private DigitalActivityResultCalculatorRepository repository;

//     public DigitalActivityResultResponseDTO calculateResult(String childId, String digitalActivityId) {

//         // Fetch previous record if exists
//         DigitalActivityResultCalculator record = repository.findByChildIdAndDigitalActivityId(childId, digitalActivityId)
//                 .orElseGet(() -> {
//                     DigitalActivityResultCalculator newRecord = new DigitalActivityResultCalculator();
//                     newRecord.setChildId(childId);
//                     newRecord.setDigitalActivityId(digitalActivityId);
//                     newRecord.setNumberOfAttempts(0);
//                     newRecord.setResult(0.0);
//                     newRecord.setStartTime(LocalDateTime.now());
//                     return newRecord;
//                 });

//         // Increment attempt count
//         int attempts = record.getNumberOfAttempts() + 1;
//         record.setNumberOfAttempts(attempts);

//         // Calculate score based on attempt number
//         double additionalScore = 0.0;
//         if (attempts == 1) additionalScore = 1.0;
//         else if (attempts == 2) additionalScore = 0.5;
//         else if (attempts == 3) additionalScore = 0.3;

//         record.setResult(record.getResult() + additionalScore);

//         // Track end time and total time
//         LocalDateTime endTime = LocalDateTime.now();
//         record.setEndTime(endTime);
//         record.setTotalTimeTaken(Duration.between(record.getStartTime(), endTime).toSeconds());

//         repository.save(record);

//         // Prepare response DTO
//         return new DigitalActivityResultResponseDTO(
//                 record.getChildId(),
//                 record.getDigitalActivityId(),
//                 record.getResult(),
//                 record.getNumberOfAttempts(),
//                 record.getTotalTimeTaken()
//         );
//     }
// }
