package com.fpt.recruitmentsystem.controller.candidate;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fpt.recruitmentsystem.dto.AvatarDTO;
import com.fpt.recruitmentsystem.dto.CandidateDTO;
import com.fpt.recruitmentsystem.dto.CandidateVacancyDTO;
import com.fpt.recruitmentsystem.dto.VacancyDTO;
import com.fpt.recruitmentsystem.dto.VacancyInfoDTO;
import com.fpt.recruitmentsystem.service.IAuthenticationService;
import com.fpt.recruitmentsystem.service.IAvatarService;
import com.fpt.recruitmentsystem.service.ICandidateService;
import com.fpt.recruitmentsystem.service.IVacancyService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController("CandidateController")
@RequestMapping("/candidates")
@SecurityRequirement(name="bearerAuth")
@Tag(name = "Candidate",description = "Candidate Controller")
@RequiredArgsConstructor
public class CandidateController {
	private final ICandidateService candidateService;
	private final IVacancyService vacancyService;
	private final IAuthenticationService authenticationService;
	private final IAvatarService avatarService;

	@GetMapping
	public ResponseEntity<CandidateDTO> getCandidate(@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
		return new ResponseEntity<>(candidateService.getCandidate(authorization), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CandidateDTO> getCandidateById(@PathVariable Integer id) {
		return new ResponseEntity<>(candidateService.getCandidateById(id), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<CandidateDTO> update(@RequestBody CandidateDTO candidateDTO, @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
		return new ResponseEntity<>(candidateService.update(candidateDTO, authorization), HttpStatus.OK);
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

	@PostMapping("/apply")
	public ResponseEntity<CandidateVacancyDTO> applyVacancy(
		@RequestBody CandidateVacancyDTO candidateVacancyDTO,
		@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
	) {
		return new ResponseEntity<>(candidateService.applyVacancy(candidateVacancyDTO, authorization), HttpStatus.OK);
	}

	@GetMapping("/vacancy-list")
	public ResponseEntity<List<VacancyInfoDTO>> getCandidateById(@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
		return new ResponseEntity<>(candidateService.getListVacancyByCandidate(authorization), HttpStatus.OK);
	}

	@PutMapping("/change-password")
	public ResponseEntity<Object> changePassword(HttpServletRequest request, @RequestParam String password) {
		return new ResponseEntity<>(authenticationService.changePassword(request, password), HttpStatus.OK);
	}
	@PutMapping("/avatar")
	public ResponseEntity<AvatarDTO> uploadAvatar(@RequestParam("file") MultipartFile multipartFile,
			@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws IOException {
		return new ResponseEntity<>(avatarService.updateAvatarCandidate(multipartFile, authorization), HttpStatus.OK);
	}
	
}
