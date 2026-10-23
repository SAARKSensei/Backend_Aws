package com.sensei.backend.service;

import java.util.UUID;
import java.util.List;

import com.sensei.backend.dto.ChildUserDTO;
import com.sensei.backend.dto.progress.*;

public interface ChildProgressService {

    void startInteractiveActivity(StartActivityDTO dto);

    void completeInteractiveActivity(CompleteActivityDTO dto);

    void recordQuestionAttempt(QuestionAttemptDTO dto);

    void startDigitalActivity(StartDigitalActivityDTO dto);

    void completeDigitalActivity(CompleteDigitalActivityDTO dto);

    ChildProgressSummaryDTO getChildProgressSummary(UUID childId);

    List<SubModuleProgressDTO> getSubModuleProgress(UUID childId);

    List<ActivityProgressDTO> getActivityProgress(UUID childId);

    List<DigitalProgressDTO> getDigitalProgress(UUID childId);

    SchoolProgressDTO getSchoolProgress(String schoolName);

    List<ChildUserDTO> getChildrenByFilter(String school, String city);

    SchoolProgressDTO getCityProgress(String city);

    default SchoolProgressDTO getLocationProgress(String location) {
        return getCityProgress(location);
    }
    
}
