package com.sensei.backend.entity;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.UUID;

import javax.persistence.*;

@Entity
@Table(name = "quiz_answer")
public class QuizAnswer {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "answer_id", updatable = false, nullable = false)
    private String answerId;  // ❌ remove = UUID.randomUUID().toString()

    @Column(name = "answer_text", nullable = false)
    private String answerText;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonBackReference
    private Questiontable question;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "lifeskill_id", referencedColumnName = "lifeskill_id")
    private LifeSkill lifeSkill;

    // Getters and setters
    public String getAnswerId() { return answerId; }
    public void setAnswerId(String answerId) { this.answerId = answerId; }

    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }

    public Questiontable getQuestion() { return question; }
    public void setQuestion(Questiontable question) { this.question = question; }

    public LifeSkill getLifeSkill() { return lifeSkill; }
    public void setLifeSkill(LifeSkill lifeSkill) { this.lifeSkill = lifeSkill; }
}
