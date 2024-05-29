package com.fpt.recruitmentsystem.service;

import com.fpt.recruitmentsystem.dto.QuestionDTO;
import com.fpt.recruitmentsystem.dto.SkillDTO;
import com.fpt.recruitmentsystem.mapper.QuestionMapper;
import com.fpt.recruitmentsystem.mapper.SkillMapper;
import com.fpt.recruitmentsystem.model.Question;
import com.fpt.recruitmentsystem.model.Skill;
import com.fpt.recruitmentsystem.repository.QuestionRepository;
import com.fpt.recruitmentsystem.repository.SkillRepository;
import com.fpt.recruitmentsystem.service.implement.QuestionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {
    @InjectMocks
    private QuestionService questionService;

    @Mock
    private QuestionMapper questionMapper;
    @Mock
    private SkillMapper skillMapper;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private SkillDTO skillDTO;
    private Question createExistingQuestion() {
        Skill existingSkill = createExistingSkill();


        return Question.builder()
                .id(1)
                .content("Sample question content")
                .answer("WHAT")
                .skill(existingSkill)
                .build();
    }

    private Skill createExistingSkill() {
        return Skill.builder().id(1).name("Sample Skill").build();
    }


    private QuestionDTO createIncomingQuestionDTO() {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setContent("Sample question content");
        questionDTO.setAnswer("What");

        // Assuming you have functions for creating existing SkillDTO, PositionDTO, and LevelDTO
        SkillDTO existingSkillDTO = createExistingSkillDTO();
        questionDTO.setSkill(existingSkillDTO);
        return questionDTO;
    }

    private SkillDTO createExistingSkillDTO() {
        SkillDTO skillDTO = new SkillDTO();
        skillDTO.setId(1);
        skillDTO.setName("C++");
        return skillDTO;
    }





    private void configMapperToDTO() {
        when(questionMapper.mapToDTO(any(Question.class))).thenAnswer(invocation -> {
            Question question = invocation.getArgument(0);
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setContent(question.getContent());
            questionDTO.setAnswer(question.getAnswer());
            questionDTO.setSkill(skillDTO);

            return questionDTO;
        });
    }


    private void configMapperToEntity() {
        // Configure the map method to return a Question based on the QuestionDTO input argument
        when(questionMapper.mapToEntity(any(QuestionDTO.class))).thenAnswer(invocation -> {
            QuestionDTO sourceQuestionDTO = invocation.getArgument(0);
            Question question = new Question();
            question.setContent(sourceQuestionDTO.getContent());
            question.setAnswer(sourceQuestionDTO.getAnswer());
            Skill skill = skillRepository.findSkillById(sourceQuestionDTO.getSkill().getId());
            question.setSkill(skill);
         return question;
        });
    }
    @Test
    public void testGetAllQuestionNotEmpty() {
        // Create an existing Question
        Question existingQuestion = createExistingQuestion();

        // Create a list of questions containing the existingQuestion
        List<Question> questionList = Collections.singletonList(existingQuestion);

        // Mock the behavior of questionRepository.findAll() to return the list of questions
        when(questionRepository.findAll()).thenReturn(questionList);

        // Create a QuestionDTO representing the existingQuestion
        QuestionDTO existingQuestionDTO = createIncomingQuestionDTO();

        // Mock the behavior of questionMapper.maptoDTO() to return the existingQuestionDTO
        when(questionMapper.mapToDTO(existingQuestion)).thenReturn(existingQuestionDTO);

        // Call the method to be tested
        List<QuestionDTO> questions = questionService.getAllQuestion();

        // Verify that questionRepository.findAll() is called once
        verify(questionRepository, times(1)).findAll();

        // Verify that questionMapper.maptoDTO() is called once with the existingQuestion as an argument
        verify(questionMapper, times(1)).mapToDTO(existingQuestion);

        // Verify the returned list
        assertEquals(1, questions.size());
        QuestionDTO questionDTO = questions.get(0);
        assertEquals(existingQuestionDTO.getContent(), questionDTO.getContent());
        assertEquals(existingQuestionDTO.getAnswer(),questionDTO.getAnswer());
        assertEquals(existingQuestionDTO.getSkill(), questionDTO.getSkill());

    }
    @Test
    public void testDeleteQuestion() {
        // Create an existing Question
        Question existingQuestion = createExistingQuestion();

        // Mock the behavior of questionRepository.findById() to return the existingQuestion
        when(questionRepository.findById(existingQuestion.getId())).thenReturn(Optional.of(existingQuestion));

        // Call the method to be tested
        questionService.deleteQuestion(existingQuestion.getId());

        // Verify that questionRepository.findById() is called once with the correct ID
        verify(questionRepository, times(1)).findById(existingQuestion.getId());

        // Verify that questionRepository.delete() is called once with the existingQuestion
        verify(questionRepository, times(1)).delete(existingQuestion);
    }
    @Test
    public void testCreateQuestion_Success() {
        // Create an incoming QuestionDTO
        QuestionDTO incomingQuestionDTO = createIncomingQuestionDTO();

        // Create the corresponding Question object from incomingQuestionDTO
        Question createdQuestion = createExistingQuestion();

        // Create a QuestionDTO representing the createdQuestion
        QuestionDTO createdQuestionDTO = createIncomingQuestionDTO();

        // Mock the behavior of questionMapper.maptoEntity() to return the createdQuestion
        when(questionMapper.mapToEntity(incomingQuestionDTO)).thenReturn(createdQuestion);

        // Mock the behavior of questionRepository.save() to return the createdQuestion
        when(questionRepository.save(createdQuestion)).thenReturn(createdQuestion);

        // Mock the behavior of questionMapper.maptoDTO() to return the createdQuestionDTO
        when(questionMapper.mapToDTO(createdQuestion)).thenReturn(createdQuestionDTO);

        // Call the method to be tested
        QuestionDTO resultQuestionDTO = questionService.createQuestion(incomingQuestionDTO);

        // Verify that questionMapper.maptoEntity() is called once with the correct QuestionDTO
        verify(questionMapper, times(1)).mapToEntity(incomingQuestionDTO);

        // Verify that questionRepository.save() is called once with the correct Question
        verify(questionRepository, times(1)).save(createdQuestion);

        // Verify that questionMapper.maptoDTO() is called once with the createdQuestion
        verify(questionMapper, times(1)).mapToDTO(createdQuestion);

        // Verify the returned QuestionDTO
        assertEquals(createdQuestionDTO.getContent(), resultQuestionDTO.getContent());
        assertEquals(createdQuestionDTO.getAnswer(),resultQuestionDTO.getAnswer());
        assertEquals(createdQuestionDTO.getSkill(), resultQuestionDTO.getSkill());

    }
    @Test
    public void testUpdateExistingQuestion() {
        // Create an existing Question
        Question existingQuestion = createExistingQuestion();

        // Create an incoming QuestionDTO with updated content
        QuestionDTO incomingQuestionDTO = createIncomingQuestionDTO();
        incomingQuestionDTO.setContent("Updated question content");

        // Mock the behavior of questionRepository.findById() to return the existingQuestion
        when(questionRepository.findById(existingQuestion.getId())).thenReturn(Optional.of(existingQuestion));

        // Mock the behavior of positionRepository.findById() to return an existing Position

        when(skillRepository.findById(anyInt())).thenReturn(Optional.of(createExistingSkill()));


        // Mock the behavior of questionMapper.maptoEntity() to return the incomingQuestionDTO
        when(questionMapper.mapToEntity(incomingQuestionDTO)).thenReturn(existingQuestion);

        // Mock the behavior of questionRepository.save() to return the updatedQuestion
        when(questionRepository.save(existingQuestion)).thenReturn(existingQuestion);

        // Mock the behavior of questionMapper.maptoDTO() to return the updatedQuestionDTO
        when(questionMapper.mapToDTO(existingQuestion)).thenReturn(incomingQuestionDTO);

        // Call the method to be tested
        QuestionDTO updatedQuestionDTO = questionService.updateQuestion(existingQuestion.getId(), incomingQuestionDTO);

        // Verify that questionRepository.findById() is called once with the correct ID
        verify(questionRepository, times(1)).findById(existingQuestion.getId());

        // Verify that questionRepository.save() is called once with the existingQuestion
        verify(questionRepository, times(1)).save(existingQuestion);

        // Verify that questionMapper.maptoEntity() is called once with the incomingQuestionDTO
        verify(questionMapper, times(1)).mapToEntity(incomingQuestionDTO);

        // Verify that questionMapper.maptoDTO() is called once with the existingQuestion
        verify(questionMapper, times(1)).mapToDTO(existingQuestion);

        // Verify the returned QuestionDTO
        assertEquals(incomingQuestionDTO.getContent(), updatedQuestionDTO.getContent());
        assertEquals(incomingQuestionDTO.getAnswer(),updatedQuestionDTO.getAnswer());
        assertEquals(incomingQuestionDTO.getSkill(), updatedQuestionDTO.getSkill());

    }
}
