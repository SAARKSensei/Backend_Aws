package com.sensei.backend.dto.pricingplan;

import lombok.Data;

import java.util.UUID;

@Data
public class PricingPlanResponseDTO {
    private UUID id;
    private String name;
    private Integer price;
    private Integer durationInMonths;
    private String grade;
    private String status;
    private String description;
}
