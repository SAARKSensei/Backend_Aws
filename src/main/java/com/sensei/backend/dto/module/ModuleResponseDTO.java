// package com.sensei.backend.dto.module;

// import lombok.Builder;
// import lombok.Data;

// import java.time.LocalDateTime;
// import java.util.UUID;

// @Data
// @Builder
// public class ModuleResponseDTO {
//     private UUID id;
//     private UUID subjectId;
//     private String name;
//     private String description;
//     private Integer orderIndex;
//     private Boolean isActive;
//     private LocalDateTime createdAt;
// }
package com.sensei.backend.dto.module;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleResponseDTO {

    private UUID id;
    private UUID subjectId;
    private String subjectName;
    private String name;
    private String description;
    private Integer orderIndex;
    private Boolean isActive;
}

