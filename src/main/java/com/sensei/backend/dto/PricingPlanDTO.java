package com.sensei.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class PricingPlanDTO {
    private String id;
    private String name;
    private int price;
    private int durationInMonths;
    private String grade;
    private String status;
    private String description;
    private List<SubjectDTO> subjects;
}
