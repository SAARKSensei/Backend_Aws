package com.sensei.backend.application.dto;
import java.util.ArrayList;
import java.util.List;

public class LifeSkillResultDTO {
	    private List<String> strong;
	    private List<String> needAttention;

	    // Constructors
	    public LifeSkillResultDTO() {
	        this.strong = new ArrayList<>();
	        this.needAttention = new ArrayList<>();
	    }

	    // Getters and setters
	    public List<String> getStrong() { return strong; }
	    public void setStrong(List<String> strong) { this.strong = strong; }

	    public List<String> getNeedAttention() { return needAttention; }
	    public void setNeedAttention(List<String> needAttention) { this.needAttention = needAttention; }
}
