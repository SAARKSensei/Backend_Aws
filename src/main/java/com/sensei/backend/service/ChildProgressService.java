package com.sensei.backend.service;

import com.sensei.backend.dto.progress.*;

public interface ChildProgressService {

    void startInteractiveActivity(StartActivityDTO dto);

    void completeInteractiveActivity(CompleteActivityDTO dto);

    void recordQuestionAttempt(QuestionAttemptDTO dto);
}
