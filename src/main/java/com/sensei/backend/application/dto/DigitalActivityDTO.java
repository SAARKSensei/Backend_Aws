package com.sensei.backend.application.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for DigitalActivity entity.
 * Contains metadata about digital activities and their related questions.
 */
public class DigitalActivityDTO {

    private String digitalActivityId;
    private String digitalActivityName;
    private String keyOutcomes;
    private String image;
    private Double ratings;
    private String feedback;
    private String tags; // Comma-separated list of tags
    private Double progress;
    private String submoduleIdRef;
    private String firstQuestionIdRef;
    private Integer noOfQuestions; // ✅ Added field for number of questions

    private List<QuestionsDTO> questions = new ArrayList<>();

    // ---------------- Getters & Setters ----------------

    public String getDigitalActivityId() {
        return digitalActivityId;
    }

    public void setDigitalActivityId(String digitalActivityId) {
        this.digitalActivityId = digitalActivityId;
    }

    public String getDigitalActivityName() {
        return digitalActivityName;
    }

    public void setDigitalActivityName(String digitalActivityName) {
        this.digitalActivityName = digitalActivityName;
    }

    public String getKeyOutcomes() {
        return keyOutcomes;
    }

    public void setKeyOutcomes(String keyOutcomes) {
        this.keyOutcomes = keyOutcomes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getRatings() {
        return ratings;
    }

    public void setRatings(Double ratings) {
        this.ratings = ratings;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public String getSubmoduleIdRef() {
        return submoduleIdRef;
    }

    public void setSubmoduleIdRef(String submoduleIdRef) {
        this.submoduleIdRef = submoduleIdRef;
    }

    public String getFirstQuestionIdRef() {
        return firstQuestionIdRef;
    }

    public void setFirstQuestionIdRef(String firstQuestionIdRef) {
        this.firstQuestionIdRef = firstQuestionIdRef;
    }

    public Integer getNoOfQuestions() {
        return noOfQuestions;
    }

    public void setNoOfQuestions(Integer noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
    }

    public List<QuestionsDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionsDTO> questions) {
        this.questions = questions != null ? questions : new ArrayList<>();
    }
}
