package com.sensei.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Processes {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid",strategy = "uuid")
    private String processId;

    @NotBlank
    private String processName;

    private int processNumber;

    @Column(columnDefinition = "TEXT")
    private String senseiMessage;

    @Column(columnDefinition = "TEXT")
    private String parentMessage;

    @Column(columnDefinition = "TEXT")
    private String image;

    private int interactiveActivityRef;

    // New Column
    @Column(columnDefinition = "TEXT")
    private String interactiveActivityIdRef;

    // New Column
    @Column(columnDefinition = "TEXT")
    private String nextProcessIdRef;

}
