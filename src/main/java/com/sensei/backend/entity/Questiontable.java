package com.sensei.backend.entity;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Questiontable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "question_id", updatable = false, nullable = false)
    private String questionId = UUID.randomUUID().toString();

    @Column(name = "question_text", nullable = false)
    private String questionText;

    // ✅ Many questions belong to one life skill
    @ManyToOne
    @JoinColumn(name = "lifeskill_id") // FK column in question table
    private LifeSkill lifeSkill;

    // One question has many answers
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<QuizAnswer> answers;

    // Getters and setters
    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public List<QuizAnswer> getAnswers() { return answers; }
    public void setAnswers(List<QuizAnswer> answers) { this.answers = answers; }

    public LifeSkill getLifeSkill() { return lifeSkill; }
    public void setLifeSkill(LifeSkill lifeSkill) { this.lifeSkill = lifeSkill; }
}