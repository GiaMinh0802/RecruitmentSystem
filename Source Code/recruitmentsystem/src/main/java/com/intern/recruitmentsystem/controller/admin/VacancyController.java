package com.fpt.recruitmentsystem.controller.admin;

import com.fpt.recruitmentsystem.service.IVacancyService;
import com.fpt.recruitmentsystem.dto.VacancyDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController("AdminVacancyController")
@RequestMapping("/admins/vacancies")
@Tag(name = "Admin",description = "Admin Controller")
@SecurityRequirement(name="bearerAuth")
@RequiredArgsConstructor
public class VacancyController {
    private final IVacancyService vacancyService;

    @PutMapping("/hide/{vacancyId}")
    public ResponseEntity<VacancyDTO> hide(@PathVariable int vacancyId) {
        return new ResponseEntity<>(vacancyService.hide(vacancyId), HttpStatus.OK);
    }

    @PutMapping("/unhide/{vacancyId}")
    public ResponseEntity<VacancyDTO> unhide(@PathVariable int vacancyId) {
        return new ResponseEntity<>(vacancyService.unhide(vacancyId), HttpStatus.OK);
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
}
