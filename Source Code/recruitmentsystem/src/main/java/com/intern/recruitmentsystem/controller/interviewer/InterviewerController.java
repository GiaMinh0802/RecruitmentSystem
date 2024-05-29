package com.fpt.recruitmentsystem.controller.interviewer;

import com.fpt.recruitmentsystem.service.IAuthenticationService;
import com.fpt.recruitmentsystem.service.IAvatarService;
import com.fpt.recruitmentsystem.service.IInterviewService;
import com.fpt.recruitmentsystem.service.IInterviewerService;
import com.fpt.recruitmentsystem.dto.AvatarDTO;
import com.fpt.recruitmentsystem.dto.InterviewDTO;
import com.fpt.recruitmentsystem.dto.InterviewerDTO;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/interviewers")
@RequiredArgsConstructor
@SecurityRequirement(name="bearerAuth")
@Tag(name = "Interviewer",description = "Interviewer Controller")
public class InterviewerController {
    private final IInterviewerService interviewerService;
    private final IInterviewService iInterviewService;
    private final IAuthenticationService authenticationService;
    private final IAvatarService avatarService;
    @PutMapping()
    public ResponseEntity<InterviewerDTO> updateInterviewer(@RequestBody InterviewerDTO interviewerDTO, @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        InterviewerDTO updatedInterviewer = interviewerService.updateInterviewer(interviewerDTO, authorization);
        return new ResponseEntity<>(updatedInterviewer, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<InterviewerDTO> getInterviewer(@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        InterviewerDTO interviewer = interviewerService.getInterviewer(authorization);
        return new ResponseEntity<>(interviewer, HttpStatus.OK);
    }
    @GetMapping("/interviews")
    public ResponseEntity<List<InterviewDTO>> getInterviews(@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return new ResponseEntity<>(iInterviewService.getInterviewsByInterviewer(authorization), HttpStatus.OK);
    }
    @GetMapping("/interviews/{id}")
    public ResponseEntity<InterviewDTO> getInterviewByInterviewer(@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,@PathVariable Integer id){
        return new ResponseEntity<>(iInterviewService.getInterviewByInterviewer(authorization,id), HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword(HttpServletRequest request, @RequestParam String password) {
        return new ResponseEntity<>(authenticationService.changePassword(request, password), HttpStatus.OK);
    }

    @PutMapping("/avatar")
	public ResponseEntity<AvatarDTO> uploadAvatar(@RequestParam("file") MultipartFile multipartFile,
			@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws IOException {
		return new ResponseEntity<>(avatarService.updateAvatarInterviewer(multipartFile, authorization), HttpStatus.OK);
	}
}
