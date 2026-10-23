package com.sensei.backend.controller;
import jakarta.validation.Valid;


import com.sensei.backend.dto.ChildUserDTO;
import com.sensei.backend.dto.progress.*;
import com.sensei.backend.service.ChildProgressService;
import lombok.RequiredArgsConstructor;
import java.util.UUID;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ChildProgressController {

    private final ChildProgressService childProgressService;

    // Child starts an Interactive Activity
    @PostMapping("/activity/start")
    public ResponseEntity<String> startActivity(@Valid @RequestBody StartActivityDTO dto) {
        childProgressService.startInteractiveActivity(dto);
        return ResponseEntity.ok("Activity started");
    }

    // Child completes an Interactive Activity
    @PostMapping("/activity/complete")
    public ResponseEntity<String> completeActivity(@Valid @RequestBody CompleteActivityDTO dto) {
        childProgressService.completeInteractiveActivity(dto);
        return ResponseEntity.ok("Activity completed");
    }

    // Child answers a Question
    @PostMapping("/question/attempt")
    public ResponseEntity<String> attempt(@Valid @RequestBody QuestionAttemptDTO dto) {
        childProgressService.recordQuestionAttempt(dto);
        return ResponseEntity.ok("Answer recorded");
    }

    // START DIGITAL ACTIVITY
    @PostMapping("/digital/start")
    public ResponseEntity<String> startDigital(@Valid @RequestBody StartDigitalActivityDTO dto) {
        childProgressService.startDigitalActivity(dto);
            return ResponseEntity.ok("Digital activity started");
}

// COMPLETE DIGITAL ACTIVITY
    @PostMapping("/digital/complete")
    public ResponseEntity<String> completeDigital(@Valid @RequestBody CompleteDigitalActivityDTO dto) {
        childProgressService.completeDigitalActivity(dto);
        return ResponseEntity.ok("Digital activity completed");
    }

    @GetMapping("/child/{childId}/summary")
public ResponseEntity<ChildProgressSummaryDTO> getSummary(
        @PathVariable UUID childId) {

    return ResponseEntity.ok(
            childProgressService.getChildProgressSummary(childId)
    );
}
@GetMapping("/child/{childId}/submodules")
public ResponseEntity<List<SubModuleProgressDTO>> getSubModules(
        @PathVariable UUID childId) {

    return ResponseEntity.ok(
            childProgressService.getSubModuleProgress(childId)
    );
}

@GetMapping("/child/{childId}/activities")
public ResponseEntity<List<ActivityProgressDTO>> getActivities(
        @PathVariable UUID childId) {

    return ResponseEntity.ok(
            childProgressService.getActivityProgress(childId)
    );
}
@GetMapping("/child/{childId}/digitals")
public ResponseEntity<List<DigitalProgressDTO>> getDigitals(
        @PathVariable UUID childId) {

    return ResponseEntity.ok(
            childProgressService.getDigitalProgress(childId)
    );
}

@GetMapping("/school")
public ResponseEntity<SchoolProgressDTO> getSchoolProgress(
        @RequestParam String schoolName) {

    return ResponseEntity.ok(
            childProgressService.getSchoolProgress(schoolName)
    );
}

@GetMapping("/children")
public ResponseEntity<List<ChildUserDTO>> getChildren(
        @RequestParam(required = false) String school,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String location) {

    String targetCity = city != null ? city : location;
    return ResponseEntity.ok(
            childProgressService.getChildrenByFilter(school, targetCity)
    );
}

@GetMapping({"/city", "/location"})
public ResponseEntity<SchoolProgressDTO> getCityProgress(
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String location) {

    String targetCity = city != null ? city : location;
    return ResponseEntity.ok(
            childProgressService.getCityProgress(targetCity)
    );
}

}
