package com.fpt.recruitmentsystem.mapper;

import com.fpt.recruitmentsystem.model.InterviewQuestion;
import com.fpt.recruitmentsystem.dto.InterviewQuestionDTO;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InterviewQuestionMapper {
    private final ModelMapper modelMapper;

    public InterviewQuestionDTO mapToDTO(InterviewQuestion interviewQuestion) {
        return modelMapper.map(interviewQuestion, InterviewQuestionDTO.class);
    }

    public InterviewQuestion mapToEntity(InterviewQuestionDTO interviewQuestionDTO) {
        return modelMapper.map(interviewQuestionDTO, InterviewQuestion.class);
    }
}
