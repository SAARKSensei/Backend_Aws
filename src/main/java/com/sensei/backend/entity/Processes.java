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
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String processId;

    @NotBlank
    private String processName;

    private int processNumber;

    @Column(columnDefinition = "TEXT")
    private String senseiMessage;

    // Renamed from parentMessage → childMessage
    @Column(name = "child_message", columnDefinition = "TEXT")
    private String childMessage;

    @Column(columnDefinition = "TEXT")
    private String image;

    private int interactiveActivityRef;

    @Column(columnDefinition = "TEXT")
    private String interactiveActivityIdRef;

    @Column(columnDefinition = "TEXT")
    private String nextProcessIdRef;

    // NEW COLUMN
    @Column(columnDefinition = "TEXT")
    private String hint;

}