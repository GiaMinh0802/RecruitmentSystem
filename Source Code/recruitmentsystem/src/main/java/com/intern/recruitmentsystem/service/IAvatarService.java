package com.fpt.recruitmentsystem.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.fpt.recruitmentsystem.dto.AvatarDTO;


public interface IAvatarService {
	AvatarDTO updateAvatarCandidate(MultipartFile multipartFile, String authorization) throws IOException;
	AvatarDTO updateAvatarAdmin(MultipartFile multipartFile, String authorization) throws IOException;
	AvatarDTO updateAvatarInterviewer(MultipartFile multipartFile, String authorization) throws IOException;
	AvatarDTO updateAvatarRecruiter(MultipartFile multipartFile, String authorization) throws IOException;
}
