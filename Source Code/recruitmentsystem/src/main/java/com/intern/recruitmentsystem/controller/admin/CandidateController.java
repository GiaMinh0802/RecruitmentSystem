package com.fpt.recruitmentsystem.controller.admin;

import com.fpt.recruitmentsystem.service.ICandidateService;
import com.fpt.recruitmentsystem.dto.CandidateDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("AdminCandidateController")
@RequestMapping("/admins/candidate-profiles")
@SecurityRequirement(name="bearerAuth")
@Tag(name = "Admin", description = "Admin Controller")
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
}
