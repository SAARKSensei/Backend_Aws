package com.sensei.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.auto.value.AutoValue.Builder;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Builder
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "answer_id", updatable = false, nullable = false)
    private UUID answerId;

    @Column(name = "answer_text", nullable = false)
    private String answerText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @JsonIgnore
    private Question question;

    @ManyToOne(fetch = FetchType.EAGER, optional = true) 
    @JoinColumn(name = "lifeskill_id" ,referencedColumnName = "lifeskill_id")  // corrected here
    private LifeSkill lifeSkills;

    // Getters and Setters
    public UUID getAnswerId() {
        return answerId;
    }

    public void setAnswerId(UUID answerId) {
        this.answerId = answerId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public LifeSkill getLifeSkill() {
        return lifeSkills;
    }

    public void setLifeSkill(LifeSkill lifeSkill) {
        this.lifeSkills = lifeSkill;
    }
}
