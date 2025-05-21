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
public class Questions {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid",strategy = "uuid")
    private String questionId;

    private int questionNumber;

    private String questionName;

    @Column(columnDefinition = "TEXT")
    private String senseiQuestion;

    @Column(columnDefinition = "TEXT")
    private String option1;

    @Column(columnDefinition = "TEXT")
    private String option2;

    @Column(columnDefinition = "TEXT")
    private String option3;

    @Column(columnDefinition = "TEXT")
    private String senseiAnswer;

    @Column(columnDefinition = "TEXT")
    private String correctAnswerDescription;

    @Column(columnDefinition = "TEXT")
    private String incorrectAnswerDescription;

    @Column(columnDefinition = "TEXT")
    private String questionImage;

    private int digitalActivityRef;

    // New Column
    @Column(columnDefinition = "TEXT")
    private String digitalActivityIdRef;

    // New Column
    @Column(columnDefinition = "TEXT")
    private String nextQuestionIdRef;
}
