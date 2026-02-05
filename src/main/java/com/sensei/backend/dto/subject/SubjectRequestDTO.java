// package com.sensei.backend.dto;

// import lombok.Data;
// import javax.validation.constraints.NotBlank;
// import java.util.List;

// @Data
// public class SubjectDTO {
//     private String subjectId;

//     @NotBlank
//     private String subjectName;

//     @NotBlank
//     private String ageGroup;

//     private int duration;

//     private int progress;

//     private List<ModuleDTO> modules;

//     // New Field
//     private String moduleIdRef;
// }
package com.sensei.backend.dto.subject;

import lombok.Data;

@Data
public class SubjectRequestDTO {
    private String name;
    private String description;
    private String iconUrl;
}
