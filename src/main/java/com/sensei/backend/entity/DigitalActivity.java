package com.sensei.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DigitalActivity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid",strategy = "uuid")
    private String digitalActivityId;

    @NotBlank
    private String digitalActivityName;

    @Column(columnDefinition = "TEXT")
    private String keyOutcomes;

    @NotBlank
    private String numberOfQuestion;

    @Column(columnDefinition = "TEXT")
    private String image;

    private String ratings;

    private String feedback;

    private String tags;

    private float progress;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "digital_activity_id")
    private List<Questions> questions;

    // New Column
    @Column(columnDefinition = "TEXT")
    private String submoduleIdRef;

    // New Column
    @Column(columnDefinition = "TEXT")
    private String firstQuestionIdRef;
}
