package com.fpt.recruitmentsystem.controller.interviewer;

import com.fpt.recruitmentsystem.service.ICvService;
import com.fpt.recruitmentsystem.service.IInterviewService;
import com.fpt.recruitmentsystem.dto.*;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("InterviewerInterviewController")
@RequestMapping("/interviewers/interviews")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Interviewer", description = "Interviewer Controller")
public class InterviewController {
    private final IInterviewService interviewService;
    private final ICvService cvService;

    // Get Recruiter information
    @GetMapping("/recruiters/{id}")
    public ResponseEntity<RecruiterDTO> getRecruiterById(@PathVariable Integer id) {
        return new ResponseEntity<>(interviewService.getRecruiterById(id), HttpStatus.OK);
    }
    @GetMapping("/cv/{interviewId}")
    public ResponseEntity<CvDTO> getCv(@PathVariable Integer interviewId, @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return new ResponseEntity<>(cvService.getCv(interviewId,authorization), HttpStatus.OK);
    }

    // Give the score for the interview
    @PutMapping("/score")
    public ResponseEntity<InterviewQuestionDTO> scoreQuestion(
        @RequestParam Integer interviewId,
        @RequestParam Integer questionId,
        @RequestBody InterviewQuestionDTO resultAnswer
    ) {
        return new ResponseEntity<>(interviewService.giveScoreByInterviewer(interviewId, questionId, resultAnswer), HttpStatus.OK);
    }

    // Add question to interview
    @PostMapping("/question")
    public ResponseEntity<InterviewQuestionDTO> addInterviewQuestion(
        @RequestParam Integer interviewId,
        @RequestParam Integer questionId
    ) {
        return new ResponseEntity<>(interviewService.addQuestionToInterview(interviewId, questionId), HttpStatus.CREATED);
    }

    // Delete question from interview
    @DeleteMapping("/question")
    public ResponseEntity<String> deleteInterviewQuestion(
        @RequestParam Integer interviewId,
        @RequestParam Integer questionId
    ) {
        interviewService.deleteQuestionFromInterview(interviewId, questionId);
        return new ResponseEntity<>("Successfully deleted question with id " + questionId + " from interview id " + interviewId, HttpStatus.NO_CONTENT);
    }

    // Update Question from interview Question
    @PutMapping("/question")
    public ResponseEntity<InterviewQuestionDTO> updateInterviewQuestion(
        @RequestParam Integer interviewId,
        @RequestParam Integer currentQuestionId,
        @RequestParam Integer updatedQuestionId
    ) {
        return new ResponseEntity<>(interviewService.updateQuestionToInterview(interviewId, currentQuestionId, updatedQuestionId), HttpStatus.OK);
    }

    @PutMapping("/complete/{interviewId}")
    public ResponseEntity<InterviewDTO> updateCompleteInterviewer(@PathVariable int interviewId, @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return new ResponseEntity<>(interviewService.updateInterviewerStatus(interviewId, authorization), HttpStatus.OK);
    }
}