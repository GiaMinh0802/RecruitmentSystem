package com.fpt.recruitmentsystem.mapper;

import com.fpt.recruitmentsystem.model.Question;
import com.fpt.recruitmentsystem.model.Skill;
import com.fpt.recruitmentsystem.dto.QuestionDTO;
import com.fpt.recruitmentsystem.repository.SkillRepository;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionMapper {
    private final ModelMapper modelMapper;
    private final SkillRepository skillRepository;
    
    public Question mapToEntity(QuestionDTO questionDTO){
        Question question = modelMapper.map(questionDTO,Question.class);
        question.setContent(questionDTO.getContent());
        question.setAnswer(questionDTO.getAnswer());
        Skill skill = skillRepository.findSkillById(questionDTO.getSkill().getId());
        question.setSkill(skill);
        return question;
    }

    public QuestionDTO mapToDTO(Question question){
        return modelMapper.map(question, QuestionDTO.class);
    }
}
