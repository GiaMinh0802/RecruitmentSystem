package com.fpt.recruitmentsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "interview_question")
public class InterviewQuestion {
    @EmbeddedId
    private InterviewQuestionKey id;
    private Float score;
    private String note;

    @ManyToOne
    @MapsId("interviewId")
    @JoinColumn(name = "interview_id", referencedColumnName = "id")
    private Interview interview;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;
}
