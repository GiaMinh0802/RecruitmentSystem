package com.fpt.recruitmentsystem.controller.candidate;

import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.recruitmentsystem.service.IInterviewService;
import com.fpt.recruitmentsystem.dto.InterviewDTO;

import lombok.RequiredArgsConstructor;

@RestController("CandidateInterviewController")
@RequestMapping("/candidates/interviews")
@SecurityRequirement(name="bearerAuth")
@Tag(name = "Candidate", description = "Candidate Controller")
@RequiredArgsConstructor
public class InterviewController {
	private final IInterviewService interviewService;

	@GetMapping
	public ResponseEntity<List<InterviewDTO>> getAll(@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
		return new ResponseEntity<>(interviewService.getInterviewHistoryOfCandidate(authorization), HttpStatus.OK);
	}

	@GetMapping("/{interviewId}")
	public ResponseEntity<InterviewDTO> getByInterviewId(@PathVariable Integer interviewId, @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
		return new ResponseEntity<>(interviewService.getInterviewByCandidate(interviewId, authorization), HttpStatus.OK);
	}
}
