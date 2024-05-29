package com.fpt.recruitmentsystem.controller.recruiter;

import com.fpt.recruitmentsystem.dto.InterviewerDTO;
import com.fpt.recruitmentsystem.service.IInterviewerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("RecruiterInterviewerController")
@RequestMapping("/recruiters/interviewers")
@SecurityRequirement(name="bearerAuth")
@Tag(name = "Recruiter",description = "Recruiter Controller")
@RequiredArgsConstructor
public class InterviewerController {
    private final IInterviewerService interviewerService;

    @GetMapping("/search")
    public ResponseEntity<List<InterviewerDTO>> search(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer limit        
    ){
        return new ResponseEntity<>(interviewerService.search(page,limit), HttpStatus.OK);
    }

    @GetMapping("/{interviewerId}")
    public ResponseEntity<InterviewerDTO> getInterviewerDetailsByRecruiter(@PathVariable Integer interviewerId) {
        return new ResponseEntity<>(interviewerService.getById(interviewerId),HttpStatus.OK);
    }
}
