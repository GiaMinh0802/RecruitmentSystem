package com.fpt.recruitmentsystem.controller.recruiter;

import com.fpt.recruitmentsystem.service.IBlacklistService;
import com.fpt.recruitmentsystem.dto.BlacklistDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("RecruiterBlacklistController")
@RequestMapping("/recruiters/blacklists")
@SecurityRequirement(name="bearerAuth")
@Tag(name = "Recruiter",description = "Recruiter Controller")
@RequiredArgsConstructor
public class BlacklistController {
    private final IBlacklistService blacklistService;
    @GetMapping("/{candidateId}")
    public ResponseEntity<List<BlacklistDTO>> getBlacklistHistory(@PathVariable int candidateId) {
        return new ResponseEntity<>(blacklistService.getBlacklistHistory(candidateId), HttpStatus.OK);
    }
    @GetMapping("paging")
    public ResponseEntity<List<BlacklistDTO>> findPaginated(@RequestParam Integer page, @RequestParam Integer limit){
        return new ResponseEntity<>(blacklistService.findPaginated(page, limit), HttpStatus.OK);
    }
}
