package com.fpt.recruitmentsystem.controller.admin;

import java.util.List;

import com.fpt.recruitmentsystem.service.IAuthenticationService;
import com.fpt.recruitmentsystem.service.IAccountService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.recruitmentsystem.dto.AccountDTO;
import com.fpt.recruitmentsystem.dto.RegisterDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admins/accounts")
@Tag(name = "Admin",description = "Admin Controller")
@SecurityRequirement(name="bearerAuth")
@RequiredArgsConstructor
public class AccountController {
	private final IAccountService accountService;
	private final IAuthenticationService authenticationService;

	@GetMapping("/search")
    public ResponseEntity<List<AccountDTO>> getAll( 
    		@RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit) {
        return new ResponseEntity<>(accountService.getAll(page, limit), HttpStatus.OK);
    } 
	@GetMapping
    public ResponseEntity<AccountDTO> searchByEmail(@RequestParam(value = "email") String email) {
		 return new ResponseEntity<>(accountService.getAccountByEmail(email), HttpStatus.OK);
    }
	
	@GetMapping("/{rolename}")
    public ResponseEntity<List<AccountDTO>> getAllAccountByRole(@PathVariable String rolename) {
        return new ResponseEntity<>(accountService.getAccountsByRole(rolename), HttpStatus.OK);
    }
	@PostMapping("/registerRecruiter")
    public ResponseEntity<Object> registerRecruiter(@RequestBody @Valid RegisterDTO registerDTO) {
        return new ResponseEntity<>(authenticationService.register(registerDTO, "RECRUITER"), HttpStatus.OK);
    }
	@PostMapping("/registerInterviewer")
    public ResponseEntity<Object> registerInterviewer(@RequestBody @Valid RegisterDTO registerDTO) {
        return new ResponseEntity<>(authenticationService.register(registerDTO, "INTERVIEWER"), HttpStatus.OK);
    }
	@PostMapping("/registerAdmin")
    public ResponseEntity<Object> registerAdmin(@RequestBody @Valid RegisterDTO registerDTO) {
        return new ResponseEntity<>(authenticationService.registerAdmin(registerDTO), HttpStatus.OK);
    }
}
