package com.sensei.backend.dto.report;

import com.sensei.backend.enums.LifeSkillType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterReportCardResponse {

    private ChildProfileDto childProfile;
    private ParentProfileDto parentProfile;
    private List<LifeSkillType> showingLifeSkills;
    private List<LifeSkillType> developFurtherLifeSkills;
    private List<String> completedTasks;
    private List<String> wrongQuestionsHistory;
    private Double overallScore;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChildProfileDto {
        private String name;
        private String grade;
        private String planStatus;
        private LocalDate planStartDate;
        private LocalDate planExpiryDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParentProfileDto {
        private String name;
        private String email;
        private String phone;
        private String location;
    }
}
