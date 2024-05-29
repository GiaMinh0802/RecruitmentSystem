package com.fpt.recruitmentsystem.dto.report;

public interface IQuestionByScoreDTO {
    Float getScores();

    String getContents();

    void setScores(Float scores);

    void setContents(String contents);
}
