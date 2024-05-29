package com.fpt.recruitmentsystem.controller.admin;

import com.fpt.recruitmentsystem.dto.BlacklistDTO;
import com.fpt.recruitmentsystem.dto.CandidateDTO;
import com.fpt.recruitmentsystem.service.IBlacklistService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/admins/blacklists")
@Tag(name = "Admin",description = "Admin Controller")
@SecurityRequirement(name="bearerAuth")
@RequiredArgsConstructor
public class BlacklistController {
    private final IBlacklistService blacklistService;

    @GetMapping("/{candidateId}")
    public ResponseEntity<List<BlacklistDTO>> getBlacklistHistory(@PathVariable int candidateId) {
        return new ResponseEntity<>(blacklistService.getBlacklistHistory(candidateId), HttpStatus.OK);
    }
    @GetMapping("/candidates")
    public ResponseEntity<List<CandidateDTO>> getCandidateInBlacklist() {
        return new ResponseEntity<>(blacklistService.getAllCandidateInBlacklist(), HttpStatus.OK);
    }

    @PostMapping("/{candidateId}")
    public ResponseEntity<BlacklistDTO> addToBlacklist(@PathVariable int candidateId, @RequestBody @Valid BlacklistDTO newRecord) {
        return new ResponseEntity<>(blacklistService.insert(candidateId, newRecord), HttpStatus.CREATED);
    }

    @PutMapping("/unblacklist/{candidateId}")
    public ResponseEntity<BlacklistDTO> unblacklist(@PathVariable int candidateId) {
        return new ResponseEntity<>(blacklistService.unblacklist(candidateId), HttpStatus.OK);
    }
}
