package com.fpt.recruitmentsystem.controller.interviewer;

import com.fpt.recruitmentsystem.dto.QuestionDTO;
import com.fpt.recruitmentsystem.service.implement.QuestionService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interviewers/questions")
@SecurityRequirement(name="bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Interviewer",description = "Interviewer Controller")
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getAllQuestion() {
        List<QuestionDTO> questions = questionService.getAllQuestion();
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<QuestionDTO>> filterQuestion(
            @RequestParam(value = "skillId", required = false) Integer skillId
    ) {
        List<QuestionDTO> filteredQuestions = questionService.filterQuestion(skillId);
        return new ResponseEntity<>(filteredQuestions, HttpStatus.OK);
    }


    @PutMapping("{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Integer id, @RequestBody QuestionDTO questionDTO) {
        QuestionDTO updatedQuestion = questionService.updateQuestion(id, questionDTO);
        return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Integer id) {
        questionService.deleteQuestion(id);
        return new ResponseEntity<>("Successfully deleted question with ID " + id, HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO) {
        QuestionDTO createdQuestion = questionService.createQuestion(questionDTO);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }
}
