package com.fpt.recruitmentsystem.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.recruitmentsystem.service.ICvService;
import com.fpt.recruitmentsystem.dto.CvDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController("AdminCVController")
@RequestMapping("/admins/cvs")
@Tag(name = "Admin", description = "CV Controller")
@SecurityRequirement(name="bearerAuth")
@RequiredArgsConstructor
public class CVController {
    private final ICvService cvService;

    @GetMapping
    public ResponseEntity<List<CvDTO>> getAll(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) Integer candidateId
    ) {
        return new ResponseEntity<>(cvService.search(page, limit, candidateId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CvDTO> getCvByIdCv(@PathVariable Integer id) {
        return new ResponseEntity<>(cvService.getCvByIdCv(id), HttpStatus.OK);
    }
}
