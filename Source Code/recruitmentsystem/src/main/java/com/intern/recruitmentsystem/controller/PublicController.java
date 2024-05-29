package com.fpt.recruitmentsystem.controller;

import com.fpt.recruitmentsystem.service.*;
import com.fpt.recruitmentsystem.dto.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Public",description = "Public Controller")
@RequiredArgsConstructor
public class PublicController {
    private final ILevelService levelService;
    private final IPositionService positionService;
    private final ISkillService skillService;
    private final IVacancyService vacancyService;
    private final IEventService eventService;
    private final ICollectedCandidateService collectedCandidateService;

    @GetMapping("/levels")
    public ResponseEntity<List<LevelDTO>> getLevels() {
        return new ResponseEntity<>(levelService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/positions")
    public ResponseEntity<List<PositionDTO>> getPosition() {
        return new ResponseEntity<>(positionService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/skills")
    public ResponseEntity<List<SkillDTO>> getSkills() {
        return new ResponseEntity<>(skillService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/vacancies")
    public ResponseEntity<List<VacancyDTO>> getVacancies(
        @RequestParam(value = "positionIds", required = false) String positionIds,
        @RequestParam(value = "skillIds", required = false) String skillIds,
        @RequestParam(value = "levelIds", required = false) String levelIds,
        @RequestParam(value = "q", required = false) String searchText,
        @RequestParam(value = "page", required = false) Integer page,
        @RequestParam(value = "limit", required = false) Integer limit) {
        return new ResponseEntity<>(vacancyService.search(positionIds, skillIds, levelIds, searchText, page, limit), HttpStatus.OK);
    }

    @GetMapping("/vacancies/count")
    public ResponseEntity<Integer> getVacancyCount(
        @RequestParam(value = "positionIds", required = false) String positionIds,
        @RequestParam(value = "skillIds", required = false) String skillIds,
        @RequestParam(value = "levelIds", required = false) String levelIds,
        @RequestParam(value = "q", required = false) String searchText
    ) {
        return new ResponseEntity<>(vacancyService.count(positionIds, skillIds, levelIds, searchText), HttpStatus.OK);
    }

    @GetMapping("/vacancies/{id}")
    public ResponseEntity<VacancyDTO> getVacancyById(@PathVariable Integer id) {
        return new ResponseEntity<>(vacancyService.getVacancy(id), HttpStatus.OK);
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventFeaturesDTO>> getEvents(@RequestParam Integer page, @RequestParam int limit){
        return new ResponseEntity<>(eventService.findPaginated(page, limit), HttpStatus.OK);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventFeaturesDTO> getEventDetails(@PathVariable int id){
        return new ResponseEntity<>(eventService.getEventDetails(id), HttpStatus.OK);
    }
    @PostMapping("/events/apply/{eventId}")
    public ResponseEntity<CollectedCandidateDTO> applyEvent(@PathVariable int eventId, @RequestBody CollectedCandidateDTO collectedCandidateDTO){
        return new ResponseEntity<>(collectedCandidateService.addCollectedCandidate(eventId, collectedCandidateDTO), HttpStatus.OK);
    }
}
