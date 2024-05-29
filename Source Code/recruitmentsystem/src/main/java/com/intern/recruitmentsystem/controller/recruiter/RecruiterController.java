package com.fpt.recruitmentsystem.controller.recruiter;

import com.fpt.recruitmentsystem.service.IAuthenticationService;
import com.fpt.recruitmentsystem.service.IAvatarService;
import com.fpt.recruitmentsystem.service.IRecruiterService;
import com.fpt.recruitmentsystem.dto.AvatarDTO;
import com.fpt.recruitmentsystem.dto.RecruiterDTO;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/recruiters")
@SecurityRequirement(name="bearerAuth")
@Tag(name = "Recruiter",description = "Recruiter Controller")
@RequiredArgsConstructor
public class RecruiterController {
    private final IRecruiterService recruiterService;
    private final IAuthenticationService authenticationService;
    private final IAvatarService avatarService;
    @PutMapping
    public ResponseEntity<RecruiterDTO> updateRecruiter(@RequestBody RecruiterDTO recruiterDTO, @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        RecruiterDTO updatedRecruiter = recruiterService.update(recruiterDTO, authorization);
        return new ResponseEntity<>(updatedRecruiter, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getRecruiter(@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return new ResponseEntity<>(recruiterService.getRecruiter(authorization), HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword(HttpServletRequest request, @RequestParam String password) {
        return new ResponseEntity<>(authenticationService.changePassword(request, password), HttpStatus.OK);
    }
    @PutMapping("/avatar")
	public ResponseEntity<AvatarDTO> uploadAvatar(@RequestParam("file") MultipartFile multipartFile,
			@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws IOException {
		return new ResponseEntity<>(avatarService.updateAvatarRecruiter(multipartFile, authorization), HttpStatus.OK);
	}
}
