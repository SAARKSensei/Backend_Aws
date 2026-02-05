// package com.sensei.backend.entity;

// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import org.hibernate.annotations.GenericGenerator;

// import javax.persistence.*;

// @Entity
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// public class QuestionOption {

//     @Id
//     @GeneratedValue(generator = "system-uuid")
//     @GenericGenerator(name="system-uuid", strategy = "uuid")
//     private String id;

//     private String optionText;
//     private String status;
//     private String counsellorNote;
// }
package com.sensei.backend.entity;

import lombok.*;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "question_option")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOption {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "option_text", nullable = false, columnDefinition = "text")
    private String optionText;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "status")
    private String status; // CORRECT / WRONG (optional, UI friendly)

    @Column(name = "hint", columnDefinition = "text")
    private String hint;

    @Column(name = "counsellor_note", columnDefinition = "text")
    private String counsellorNote;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "is_active")
    private Boolean isActive = true;

}