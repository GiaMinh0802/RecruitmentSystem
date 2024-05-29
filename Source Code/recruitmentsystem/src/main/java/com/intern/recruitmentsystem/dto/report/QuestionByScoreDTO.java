package com.fpt.recruitmentsystem.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionByScoreDTO implements IQuestionByScoreDTO {
    private Float scores;
    private String contents;
}
