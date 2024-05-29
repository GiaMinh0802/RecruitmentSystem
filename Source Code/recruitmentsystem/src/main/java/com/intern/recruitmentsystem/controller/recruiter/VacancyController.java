package com.fpt.recruitmentsystem.controller.recruiter;

import com.fpt.recruitmentsystem.service.ICvService;
import com.fpt.recruitmentsystem.service.IVacancyService;
import com.fpt.recruitmentsystem.dto.RequestFillterDTO;
import com.fpt.recruitmentsystem.dto.VacancyDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recruiters/vacancies")
@Tag(name = "Recruiter",description = "Recruiter Controller")
@SecurityRequirement(name="bearerAuth")
@RequiredArgsConstructor
public class VacancyController {
    private final IVacancyService vacancyService;
    private final ICvService cvService;

    @PostMapping
    public ResponseEntity<VacancyDTO> addVacancy(@RequestBody VacancyDTO vacancyDTO){
        return new ResponseEntity<>(vacancyService.addVacancy(vacancyDTO), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<VacancyDTO> updateVacancy(@PathVariable int id, @RequestBody VacancyDTO vacancyDTO){
        return new ResponseEntity<>(vacancyService.updateVacancy(id, vacancyDTO), HttpStatus.OK);
    }

    @GetMapping("findCandidateAll")
    public ResponseEntity<Object> getCandidateAll(@RequestBody RequestFillterDTO requestFillterDTO){
        return new ResponseEntity<>(vacancyService.filterCandidateAll(requestFillterDTO.getSkill(),
                requestFillterDTO.getLevel(), requestFillterDTO.getPositionId()), HttpStatus.OK);
    }

	@GetMapping("/search")
	public ResponseEntity<List<VacancyDTO>> search(
			@RequestParam(value = "positionIds", required = false) String positionIds,
			@RequestParam(value = "skillIds", required = false) String skillIds,
			@RequestParam(value = "levelIds", required = false) String levelIds,
			@RequestParam(value = "q", required = false) String searchText,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "limit", required = false) Integer limit) {
		return new ResponseEntity<>(vacancyService.search(positionIds, skillIds, levelIds, searchText, page, limit), HttpStatus.OK);
	}

    @GetMapping("{vacancyId}")
    public ResponseEntity<Object> getListCandidateByVacancyId(@PathVariable int vacancyId){
        return new ResponseEntity<>(vacancyService.getListCandidateByVacancyId(vacancyId), HttpStatus.OK);
    }

    @GetMapping("/details/{vacancyId}")
    public ResponseEntity<Object> getVacancy(@PathVariable int vacancyId){
        return new ResponseEntity<>(vacancyService.getVacancy(vacancyId),HttpStatus.OK);
    }

    @GetMapping("/cv")
    public ResponseEntity<Object> getCvForVacancy(@RequestParam int candidateId,
                                                 @RequestParam int vacancyId,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return new ResponseEntity<>(cvService.getCvForVacancy(candidateId,vacancyId,authorization), HttpStatus.OK);
    }
}
