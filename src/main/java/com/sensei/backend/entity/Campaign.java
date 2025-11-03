package com.sensei.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String campaignId;  // Auto-generated unique ID
    private String name; 
    private String schoolName;  // School name (text)
    private String email;       // Email (text)
    private String phone;       // Phone (text)

    private LocalDateTime time; // Timestamp (date + time)
}
