package com.sensei.backend.domain.service;

import com.sensei.backend.application.dto.progress.*;

public interface ChildProgressService {

    void startInteractiveActivity(StartActivityDTO dto);

    void completeInteractiveActivity(CompleteActivityDTO dto);

    void recordQuestionAttempt(QuestionAttemptDTO dto);
}
