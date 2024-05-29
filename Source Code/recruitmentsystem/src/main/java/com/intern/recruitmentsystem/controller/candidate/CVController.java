package com.fpt.recruitmentsystem.controller.candidate;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fpt.recruitmentsystem.dto.CvDTO;
import com.fpt.recruitmentsystem.service.ICvService;
import com.google.common.net.HttpHeaders;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/candidates/cv")
@SecurityRequirement(name="bearerAuth")
@Tag(name = "Candidate",description = "Candidate Controller")
@RequiredArgsConstructor
public class CVController {
	private final ICvService cvService;

	@GetMapping
	public ResponseEntity<List<CvDTO>> getCvById(@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
		return new ResponseEntity<>(cvService.getCvByCandidateId(authorization), HttpStatus.OK);
	}
    
	@GetMapping("/{cvId}")
	public ResponseEntity<CvDTO> getOneCv(@PathVariable("cvId") Integer cvId, @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws IOException {
		return new ResponseEntity<>(cvService.getOneCv(cvId, authorization), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<CvDTO> uploadCv(@RequestParam("file") MultipartFile multipartFile,
			@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,String data) throws IOException {
		return new ResponseEntity<>(cvService.uploadCv(multipartFile, authorization,data), HttpStatus.OK);
	}

	@DeleteMapping("/{cvId}")
	public ResponseEntity<String> deleteCv(@PathVariable("cvId") Integer cvId, @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws IOException {
		cvService.deleteCv(cvId, authorization);
		return new ResponseEntity<>("Successfully delete CV with ID " + cvId, HttpStatus.OK);
	}
	@PutMapping("/{cvId}")
	public ResponseEntity<CvDTO> updateCv(@PathVariable("cvId") Integer cvId, @RequestParam("file") MultipartFile multipartFile,
										  @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,String data) throws IOException {
		return new ResponseEntity<>(cvService.updateCv(cvId,multipartFile, authorization,data), HttpStatus.OK);
	}
}
