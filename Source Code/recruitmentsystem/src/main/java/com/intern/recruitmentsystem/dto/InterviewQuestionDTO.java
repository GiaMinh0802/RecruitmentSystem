package com.fpt.recruitmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewQuestionDTO {
    private Integer interviewId;
    private QuestionDTO question;
    private String note;
    private Float score;
}
