// package com.sensei.backend.dto;

// import lombok.Data;
// import java.util.List;

// @Data
// public class PricingPlanDTO {
//     private String id;
//     private String name;
//     private int price;
//     private int durationInMonths;
//     private String grade;
//     private String status;
//     private String description;
//     private List<SubjectDTO> subjects;
// }
package com.sensei.backend.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class PricingPlanDTO {
    private UUID id;
    private String name;
    private Integer price;
    private Integer durationInMonths;
    private String grade;
    private String status;
    private String description;

    // Subject IDs only
    private List<UUID> subjectIds;
}
