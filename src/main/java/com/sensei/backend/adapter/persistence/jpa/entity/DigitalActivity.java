// package com.sensei.backend.entity;

// import javax.persistence.*;
// import org.hibernate.annotations.GenericGenerator;
// import java.util.List;

// @Entity
// public class DigitalActivity {

//     @Id
//     @GeneratedValue(generator = "uuid2")
//     @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
//     @Column(length = 36)
//     private String digitalActivityId;

//     private String digitalActivityName;
//     private String keyOutcomes;
//     private String image;
//     private Double ratings;
//     private String feedback;
//     private String tags;
//     private Double progress;
//     private String submoduleIdRef;
//     private String firstQuestionIdRef;
//     private Integer noOfQuestions;

//     // 🔹 ManyToOne reference to SubModule to fix mappedBy issue
//     @ManyToOne
//     @JoinColumn(name = "sub_module_id")
//     private SubModule subModule;

//     // 🔹 OneToMany to Questions
//     @OneToMany(mappedBy = "digitalActivity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//     private List<Questions> questions;

//     // --- Getters and Setters ---
//     public String getDigitalActivityId() { return digitalActivityId; }
//     public void setDigitalActivityId(String digitalActivityId) { this.digitalActivityId = digitalActivityId; }

//     public String getDigitalActivityName() { return digitalActivityName; }
//     public void setDigitalActivityName(String digitalActivityName) { this.digitalActivityName = digitalActivityName; }

//     public String getKeyOutcomes() { return keyOutcomes; }
//     public void setKeyOutcomes(String keyOutcomes) { this.keyOutcomes = keyOutcomes; }

//     public String getImage() { return image; }
//     public void setImage(String image) { this.image = image; }

//     public Double getRatings() { return ratings; }
//     public void setRatings(Double ratings) { this.ratings = ratings; }

//     public String getFeedback() { return feedback; }
//     public void setFeedback(String feedback) { this.feedback = feedback; }

//     public String getTags() { return tags; }
//     public void setTags(String tags) { this.tags = tags; }

//     public Double getProgress() { return progress; }
//     public void setProgress(Double progress) { this.progress = progress; }

//     public String getSubmoduleIdRef() { return submoduleIdRef; }
//     public void setSubmoduleIdRef(String submoduleIdRef) { this.submoduleIdRef = submoduleIdRef; }

//     public String getFirstQuestionIdRef() { return firstQuestionIdRef; }
//     public void setFirstQuestionIdRef(String firstQuestionIdRef) { this.firstQuestionIdRef = firstQuestionIdRef; }

//     public Integer getNoOfQuestions() { return noOfQuestions; }
//     public void setNoOfQuestions(Integer noOfQuestions) { this.noOfQuestions = noOfQuestions; }

//     public List<Questions> getQuestions() { return questions; }
//     public void setQuestions(List<Questions> questions) { this.questions = questions; }

//     public SubModule getSubModule() { return subModule; }
//     public void setSubModule(SubModule subModule) { this.subModule = subModule; }
// }
package com.sensei.backend.adapter.persistence.jpa.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "digital_activity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DigitalActivity {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "uuid", updatable = false)
    private UUID id;

    // FK → sub_module.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_module_id", nullable = false)
    private SubModule subModule;

    @Column(name = "title", nullable = false, columnDefinition = "text")
    private String title;

    @Column(name = "game_type", columnDefinition = "text")
    private String gameType;

    @Column(name = "instructions", columnDefinition = "text")
    private String instructions;

    @Column(name = "instruction", columnDefinition = "text")
    private String instruction;

    @Column(name = "difficulty", columnDefinition = "text")
    private String difficulty;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
