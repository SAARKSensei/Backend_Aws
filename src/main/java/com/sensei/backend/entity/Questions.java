package com.sensei.backend.entity;

import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Questions{

    // @Id
	// @GeneratedValue(generator = "UUID")
	// @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	// @Column(name = "questionId", updatable = false, nullable = false)
	// private String questionId;
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    private String question;
    private String senseiQuestion;
    private String senseiAnswer;
    private String correctAnswerDescription;
    private String incorrectAnswerDescription;
    private String questionImage;
    private Integer questionNumber;

    private String option1;
    private String option1Status;
    private String option1CounsellorNote;
    private String option1Hint; // ✅ Added

    private String option2;
    private String option2Status;
    private String option2CounsellorNote;
    private String option2Hint; // ✅ Added

    private String option3;
    private String option3Status;
    private String option3CounsellorNote;
    private String option3Hint; // ✅ Added

    @Column(name = "digitalActivityIdRef")
    private String digitalActivityIdRef;

    // ✅ Back-reference to DigitalActivity
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "digital_activity_id", referencedColumnName = "digitalActivityId")
    private DigitalActivity digitalActivity;

    // --- Getters and Setters ---
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSenseiQuestion() {
        return senseiQuestion;
    }

    public void setSenseiQuestion(String senseiQuestion) {
        this.senseiQuestion = senseiQuestion;
    }

    public String getSenseiAnswer() {
        return senseiAnswer;
    }

    public void setSenseiAnswer(String senseiAnswer) {
        this.senseiAnswer = senseiAnswer;
    }

    public String getCorrectAnswerDescription() {
        return correctAnswerDescription;
    }

    public void setCorrectAnswerDescription(String correctAnswerDescription) {
        this.correctAnswerDescription = correctAnswerDescription;
    }

    public String getIncorrectAnswerDescription() {
        return incorrectAnswerDescription;
    }

    public void setIncorrectAnswerDescription(String incorrectAnswerDescription) {
        this.incorrectAnswerDescription = incorrectAnswerDescription;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption1Status() {
        return option1Status;
    }

    public void setOption1Status(String option1Status) {
        this.option1Status = option1Status;
    }

    public String getOption1CounsellorNote() {
        return option1CounsellorNote;
    }

    public void setOption1CounsellorNote(String option1CounsellorNote) {
        this.option1CounsellorNote = option1CounsellorNote;
    }

    public String getOption1Hint() {
        return option1Hint;
    }

    public void setOption1Hint(String option1Hint) {
        this.option1Hint = option1Hint;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption2Status() {
        return option2Status;
    }

    public void setOption2Status(String option2Status) {
        this.option2Status = option2Status;
    }

    public String getOption2CounsellorNote() {
        return option2CounsellorNote;
    }

    public void setOption2CounsellorNote(String option2CounsellorNote) {
        this.option2CounsellorNote = option2CounsellorNote;
    }

    public String getOption2Hint() {
        return option2Hint;
    }

    public void setOption2Hint(String option2Hint) {
        this.option2Hint = option2Hint;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption3Status() {
        return option3Status;
    }

    public void setOption3Status(String option3Status) {
        this.option3Status = option3Status;
    }

    public String getOption3CounsellorNote() {
        return option3CounsellorNote;
    }

    public void setOption3CounsellorNote(String option3CounsellorNote) {
        this.option3CounsellorNote = option3CounsellorNote;
    }

    public String getOption3Hint() {
        return option3Hint;
    }

    public void setOption3Hint(String option3Hint) {
        this.option3Hint = option3Hint;
    }

    public String getDigitalActivityIdRef() {
        return digitalActivityIdRef;
    }

    public void setDigitalActivityIdRef(String digitalActivityIdRef) {
        this.digitalActivityIdRef = digitalActivityIdRef;
    }

    public DigitalActivity getDigitalActivity() {
        return digitalActivity;
    }

    public void setDigitalActivity(DigitalActivity digitalActivity) {
        this.digitalActivity = digitalActivity;
    }
}
