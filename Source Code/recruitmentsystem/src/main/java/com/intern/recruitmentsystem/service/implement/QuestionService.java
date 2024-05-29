package com.fpt.recruitmentsystem.service.implement;

import com.fpt.recruitmentsystem.model.Question;
import com.fpt.recruitmentsystem.model.Skill;
import com.fpt.recruitmentsystem.service.IQuestionService;
import com.fpt.recruitmentsystem.util.Message;
import com.fpt.recruitmentsystem.dto.*;
import com.fpt.recruitmentsystem.exception.BadRequestException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.QuestionMapper;
import com.fpt.recruitmentsystem.repository.QuestionRepository;
import com.fpt.recruitmentsystem.repository.SkillRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService {
    private final QuestionRepository questionRepository;
    private final SkillRepository skillRepository;
    private final QuestionMapper questionMapper;

    public List<QuestionDTO> getAllQuestion(){
        List<Question> questionLists = questionRepository.findAll();
        if (questionLists.isEmpty()) {
            throw new NotFoundException(Message.QUESTION_NOT_FOUND);
        }
        return questionLists.stream()
                .map(questionMapper::mapToDTO)
                .toList();
    }

    public List<QuestionDTO> filterQuestion(Integer skillId) {
        List<Question> questionList = questionRepository.findAll();

        if (skillId != null) {
            Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new NotFoundException(Message.SKILL_NOT_FOUND));
            questionList = questionList.stream()
                    .filter(question -> question.getSkill().equals(skill))
                    .toList();
        }

        if (questionList.isEmpty()) {
            throw new NotFoundException(Message.QUESTION_NOT_FOUND);
        }

        return questionList.stream()
            .map(questionMapper::mapToDTO)
            .toList();
    }

    public void deleteQuestion(Integer id){
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Message.QUESTION_NOT_FOUND));
        try {
            questionRepository.delete(question);
        } catch (Exception e) {
            throw new BadRequestException("Can't delete question");
        }
    }

    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        Question question = questionMapper.mapToEntity(questionDTO);
        Question newquestion = questionRepository.save(question);
        return questionMapper.mapToDTO(newquestion);
    }

    public QuestionDTO updateQuestion(Integer questionId, QuestionDTO questionDTO) {
        // Kiểm tra xem câu hỏi có tồn tại trong cơ sở dữ liệu không
        Question optionalQuestion = questionRepository.findById(questionId)
            .orElseThrow(() -> new NotFoundException(Message.QUESTION_NOT_FOUND));
        Question questionToEntity = questionMapper.mapToEntity(questionDTO);
        // Cập nhật nội dung câu hỏi
        optionalQuestion.setContent(questionToEntity.getContent());
        optionalQuestion.setAnswer(questionToEntity.getAnswer());

        // cap nhat skill
        Skill optionalSkill = skillRepository.findById(questionToEntity.getSkill().getId())
            .orElseThrow(() -> new NotFoundException(Message.SKILL_NOT_FOUND));
        optionalQuestion.setSkill(optionalSkill);

        Question updatedQuestion = questionRepository.save(optionalQuestion);
        return questionMapper.mapToDTO(updatedQuestion);
    }
}
