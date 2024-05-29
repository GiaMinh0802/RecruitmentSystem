package com.fpt.recruitmentsystem.controller.recruiter;

import com.fpt.recruitmentsystem.service.ICandidateService;
import com.fpt.recruitmentsystem.dto.CandidateDTO;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("RecruiterCandidateController")
@RequestMapping("/recruiters/candidate-profiles")
@SecurityRequirement(name="bearerAuth")
@Tag(name = "Recruiter",description = "Recruiter Controller")
@RequiredArgsConstructor
public class CandidateController {
    private final ICandidateService candidateService;

    @GetMapping
    public ResponseEntity<List<CandidateDTO>> search(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer limit
    ) {
        return new ResponseEntity<>(candidateService.search(page, limit), HttpStatus.OK);
    }
    @GetMapping("position/{positionId}")
    public ResponseEntity<List<CandidateDTO>> getCandidatesByPosition(@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,@PathVariable int positionId)
    {
        return new ResponseEntity<>(candidateService.getCandidateByPosition(authorization,positionId),HttpStatus.OK);
    }

}
