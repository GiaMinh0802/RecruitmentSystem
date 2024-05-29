package com.fpt.recruitmentsystem.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.recruitmentsystem.service.IInterviewService;
import com.fpt.recruitmentsystem.dto.CandidateVacancyDTO;
import com.fpt.recruitmentsystem.dto.InterviewDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController("AdminInterviewController")
@RequestMapping("/admins/interviews")
@Tag(name = "Admin", description = "Interview Controller")
@SecurityRequirement(name="bearerAuth")
@RequiredArgsConstructor
public class InterviewController {
    private final IInterviewService interviewService;

    @PutMapping("/approve/{interviewId}")
    public ResponseEntity<CandidateVacancyDTO> approve(@PathVariable Integer interviewId) {
        return new ResponseEntity<>(interviewService.approve(interviewId), HttpStatus.OK);
    }

    @PutMapping("/reject/{interviewId}")
    public ResponseEntity<CandidateVacancyDTO> reject(@PathVariable Integer interviewId) {
        return new ResponseEntity<>(interviewService.reject(interviewId), HttpStatus.OK);
    }
    @GetMapping("/search")
    public ResponseEntity<List<InterviewDTO>> search(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) Integer vacancyId
    ) {
        List<InterviewDTO> interviews = interviewService.getAllInterview(page, limit, vacancyId);
        return new ResponseEntity<>(interviews, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<InterviewDTO> getById(@PathVariable Integer id) {
        InterviewDTO interview = interviewService.getInterviewById(id);
        return new ResponseEntity<>(interview, HttpStatus.OK);
    }
}
