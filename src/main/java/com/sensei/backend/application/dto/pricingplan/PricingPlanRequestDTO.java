package com.sensei.backend.application.dto.pricingplan;

import lombok.Data;

@Data
public class PricingPlanRequestDTO {
    private String name;
    private Integer price;
    private Integer durationInMonths;
    private String grade;
    private String status;      // ACTIVE / INACTIVE
    private String description;
}
