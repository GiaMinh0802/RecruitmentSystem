package com.fpt.recruitmentsystem.controller;

import com.fpt.recruitmentsystem.service.IAuthenticationService;
import com.fpt.recruitmentsystem.dto.LoginDTO;
import com.fpt.recruitmentsystem.dto.RegisterDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth",description = "Auth Controller")
public class AuthenticationController {
    private final IAuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterDTO registerDTO) {
        return new ResponseEntity<>(authenticationService.register(registerDTO, "CANDIDATE"), HttpStatus.OK);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<Object> activeAccount(@RequestParam String token) {
        return new ResponseEntity<>(authenticationService.activeAccount(token), HttpStatus.OK);
    }

    @PostMapping("/resend-active")
    public ResponseEntity<Object> resendActiveAccount(@RequestParam String email) {
        return new ResponseEntity<>(authenticationService.resendActiveAccount(email), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO) {
        return new ResponseEntity<>(authenticationService.login(loginDTO), HttpStatus.OK);
    }

    @SecurityRequirement(name="bearerAuth")
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        return new ResponseEntity<>(authenticationService.logout(request), HttpStatus.OK);
    }

    @SecurityRequirement(name="bearerAuth")
    @PostMapping ("/refresh-token")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request) {
        Object result = authenticationService.refreshToken(request);
        if (result.equals("Token has expired"))
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping ("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestParam String email) {
        return new ResponseEntity<>(authenticationService.forgotPassword(email), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestParam String token, @RequestParam String password) {
        return new ResponseEntity<>(authenticationService.resetPassword(token, password), HttpStatus.OK);
    }

    @GetMapping("/login/google")
    public ResponseEntity<Object> loginGoogle(){
        String redirectUrl = "/oauth2/authorization/google";
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectUrl)
                .build();
    }
}
