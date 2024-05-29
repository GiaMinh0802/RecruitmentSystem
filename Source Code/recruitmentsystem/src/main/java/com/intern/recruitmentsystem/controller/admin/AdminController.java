package com.fpt.recruitmentsystem.controller.admin;

import com.fpt.recruitmentsystem.service.IAdminService;
import com.fpt.recruitmentsystem.dto.AdminDTO;
import com.fpt.recruitmentsystem.dto.AvatarDTO;
import com.fpt.recruitmentsystem.service.IAuthenticationService;
import com.fpt.recruitmentsystem.service.IAvatarService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
@SecurityRequirement(name="bearerAuth")
@Tag(name = "Admin", description = "Admin Controller")
public class AdminController {
    private final IAdminService adminService;
    private final IAuthenticationService authenticationService;
    private final IAvatarService avatarService;
    @PutMapping
    public ResponseEntity<AdminDTO> updateAdmin(@RequestBody AdminDTO adminDTO, @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        AdminDTO updatedAdmin = adminService.update(adminDTO, authorization);
        return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<AdminDTO> getAdmin(@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        AdminDTO admin = adminService.getAdmin(authorization);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword(HttpServletRequest request, @RequestParam String password) {
        return new ResponseEntity<>(authenticationService.changePassword(request, password), HttpStatus.OK);
    }
    
    @PutMapping("/avatar")
	public ResponseEntity<AvatarDTO> uploadAvatar(@RequestParam("file") MultipartFile multipartFile,
			@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws IOException {
		return new ResponseEntity<>(avatarService.updateAvatarAdmin(multipartFile, authorization), HttpStatus.OK);
	}
}
