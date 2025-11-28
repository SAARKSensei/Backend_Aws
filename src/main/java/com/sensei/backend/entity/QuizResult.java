package com.sensei.backend.entity;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "quiz_result")
public class QuizResult {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "result_id", updatable = false, nullable = false)
    private String resultId;

    @Column(name = "child_id", nullable = false)
    private String childId;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "quiz_result_answers",
        joinColumns = @JoinColumn(name = "result_id"),
        inverseJoinColumns = @JoinColumn(name = "answer_id")
    )
    private List<QuizAnswer> selectedAnswers;

    // Getters and setters
    public String getResultId() { return resultId; }
    public void setResultId(String resultId) { this.resultId = resultId; }

    public String getChildId() { return childId; }
    public void setChildId(String childId) { this.childId = childId; }

    public List<QuizAnswer> getSelectedAnswers() { return selectedAnswers; }
    public void setSelectedAnswers(List<QuizAnswer> selectedAnswers) { this.selectedAnswers = selectedAnswers; }
}
