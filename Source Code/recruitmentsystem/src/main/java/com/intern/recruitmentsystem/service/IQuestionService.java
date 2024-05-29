package com.fpt.recruitmentsystem.service;

import java.util.List;

import com.fpt.recruitmentsystem.dto.QuestionDTO;

public interface IQuestionService {
    List<QuestionDTO> getAllQuestion();
    QuestionDTO createQuestion(QuestionDTO questionDTO);
    QuestionDTO updateQuestion(Integer questionId, QuestionDTO questionDTO);
    void deleteQuestion(Integer id);
    List<QuestionDTO> filterQuestion(Integer skillId);
}
