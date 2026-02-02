// package com.sensei.backend.entity;

// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import org.hibernate.annotations.GenericGenerator;

// import javax.persistence.*;
// import java.time.LocalDateTime;

// @Entity
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// public class Campaign {

//     @Id
//     @GeneratedValue(generator = "system-uuid")
//     @GenericGenerator(name="system-uuid", strategy = "uuid")
//     private String campaignId;  // Auto-generated unique ID
//     private String name; 
//     private String schoolName;  // School name (text)
//     private String email;       // Email (text)
//     private String phone;       // Phone (text)

//     private LocalDateTime time; // Timestamp (date + time)
// }
package com.sensei.backend.adapter.persistence.jpa.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "campaign")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campaign {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
