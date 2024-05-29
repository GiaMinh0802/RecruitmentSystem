package com.fpt.recruitmentsystem.controller.recruiter;

import com.fpt.recruitmentsystem.service.ICollectedCandidateService;
import com.fpt.recruitmentsystem.dto.CollectedCandidateDTO;
import com.fpt.recruitmentsystem.dto.CollectedCandidateFeaturesDTO;
import com.fpt.recruitmentsystem.util.ResponseMessage;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruiters/collected-candidates")
@SecurityRequirement(name="bearerAuth")
@Tag(name = "Recruiter",description = "Recruiter Controller")
@RequiredArgsConstructor
public class CollectedCandidateController {
    private final ICollectedCandidateService collectedCandidateService;
    @GetMapping
    public ResponseEntity<List<CollectedCandidateFeaturesDTO>> getListCollectedCandidate(){
        return new ResponseEntity<>(collectedCandidateService.getListCollectedCandidate(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CollectedCandidateFeaturesDTO> getCollectedCandidateDetails(@PathVariable int id){
        return new ResponseEntity<>(collectedCandidateService.getCollectedCandidateDetails(id), HttpStatus.OK);
    }
    @GetMapping("/events")
    public ResponseEntity<List<CollectedCandidateFeaturesDTO>> getListCollectedCandidateByEvent(@RequestParam int eventId){
        return new ResponseEntity<>(collectedCandidateService.getListCollectedCandidateByEvent(eventId), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteCollectedCandidate(@PathVariable int id){
        return new ResponseEntity<>(collectedCandidateService.deleteCollectedCandidate(id), HttpStatus.OK);
    }
    @PostMapping("/{eventId}")
    public ResponseEntity<CollectedCandidateDTO> addCollectedCandidate(@PathVariable int eventId, @RequestBody CollectedCandidateDTO collectedCandidateDTO){
        return new ResponseEntity<>(collectedCandidateService.addCollectedCandidate(eventId, collectedCandidateDTO), HttpStatus.CREATED);
    }
    @GetMapping("/paging")
    public ResponseEntity<List<CollectedCandidateFeaturesDTO>> findPaginated(@RequestParam int page, @RequestParam int limit){
        return new ResponseEntity<>(collectedCandidateService.findPaginated(page, limit), HttpStatus.OK);
    }
}
