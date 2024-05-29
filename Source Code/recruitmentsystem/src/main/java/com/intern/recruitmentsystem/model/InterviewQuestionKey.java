package com.fpt.recruitmentsystem.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class InterviewQuestionKey implements Serializable {
    private Integer interviewId;
    private Integer questionId;
}
