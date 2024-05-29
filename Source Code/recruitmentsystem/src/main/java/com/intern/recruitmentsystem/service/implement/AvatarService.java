package com.fpt.recruitmentsystem.service.implement;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fpt.recruitmentsystem.dto.AvatarDTO;
import com.fpt.recruitmentsystem.model.Admin;
import com.fpt.recruitmentsystem.model.Candidate;
import com.fpt.recruitmentsystem.model.Interviewer;
import com.fpt.recruitmentsystem.model.Recruiter;
import com.fpt.recruitmentsystem.repository.AdminRepository;
import com.fpt.recruitmentsystem.repository.CandidateRepository;
import com.fpt.recruitmentsystem.repository.InterviewerRepository;
import com.fpt.recruitmentsystem.repository.RecruiterRepository;
import com.fpt.recruitmentsystem.service.IAccountService;
import com.fpt.recruitmentsystem.service.IAvatarService;
import com.fpt.recruitmentsystem.util.FileUploadUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvatarService implements IAvatarService {
	private final IAccountService accountService;
	private final FileUploadUtil uploadUtil;
	private final CandidateRepository candidateRepository;
	private final AdminRepository adminRepository;
	private final InterviewerRepository interviewerRepository;
	private final RecruiterRepository recruiterRepository;
	
	@Override
public AvatarDTO updateAvatarCandidate(MultipartFile multipartFile, String authorization) throws IOException{
		Candidate candidate = accountService.getCandidateByAuthorizationHeader(authorization);
		AvatarDTO avatarDTO = new AvatarDTO();
		String linkAvt = candidate.getLinkAvt();
		String newFileName = multipartFile.getOriginalFilename();
		String urlAvt;
		if(linkAvt == null) {
			urlAvt = uploadUtil.saveFile(newFileName, multipartFile);
			candidate.setLinkAvt(urlAvt);
			candidateRepository.save(candidate);
			avatarDTO.setLinkAvatar(urlAvt);
			return avatarDTO;
		}
		File file = new File(linkAvt);
		String oldFileName = file.getName();
		urlAvt = uploadUtil.updateAvatar(oldFileName, newFileName, multipartFile);
		
		candidate.setLinkAvt(urlAvt);
		candidateRepository.save(candidate);
		avatarDTO.setLinkAvatar(urlAvt);
		return avatarDTO;
}

	@Override
	public AvatarDTO updateAvatarAdmin(MultipartFile multipartFile, String authorization) throws IOException {
		Admin admin = accountService.getAdminByAuthorizationHeader(authorization);
		AvatarDTO avatarDTO = new AvatarDTO();
		String linkAvt = admin.getLinkAvt();
		String newFileName = multipartFile.getOriginalFilename();
		String urlAvt;
		if(linkAvt == null) {
			urlAvt = uploadUtil.saveFile(newFileName, multipartFile);
			admin.setLinkAvt(urlAvt);
			adminRepository.save(admin);
			avatarDTO.setLinkAvatar(urlAvt);
			return avatarDTO;
		}
		File file = new File(linkAvt);
		String oldFileName = file.getName();
		urlAvt = uploadUtil.updateAvatar(oldFileName, newFileName, multipartFile);
		
		admin.setLinkAvt(urlAvt);
		adminRepository.save(admin);
		avatarDTO.setLinkAvatar(urlAvt);
		return avatarDTO;
	}

	@Override
	public AvatarDTO updateAvatarInterviewer(MultipartFile multipartFile, String authorization) throws IOException {
		Interviewer interviewer = accountService.getInterviewerByAuthorizationHeader(authorization);
		AvatarDTO avatarDTO = new AvatarDTO();
		String linkAvt = interviewer.getLinkAvt();
		String newFileName = multipartFile.getOriginalFilename();
		String urlAvt;
		if(linkAvt == null) {
			urlAvt = uploadUtil.saveFile(newFileName, multipartFile);
			interviewer.setLinkAvt(urlAvt);
			interviewerRepository.save(interviewer);
			avatarDTO.setLinkAvatar(urlAvt);
			return avatarDTO;
		}
		File file = new File(linkAvt);
		String oldFileName = file.getName();
		urlAvt = uploadUtil.updateAvatar(oldFileName, newFileName, multipartFile);
		
		interviewer.setLinkAvt(urlAvt);
		interviewerRepository.save(interviewer);
		avatarDTO.setLinkAvatar(urlAvt);
		return avatarDTO;
	}

	@Override
	public AvatarDTO updateAvatarRecruiter(MultipartFile multipartFile, String authorization) throws IOException {
		Recruiter recruiter = accountService.getRecruiterByAuthorizationHeader(authorization);
		AvatarDTO avatarDTO = new AvatarDTO();
		String linkAvt = recruiter.getLinkAvt();
		String newFileName = multipartFile.getOriginalFilename();
		String urlAvt;
		if(linkAvt == null) {
			urlAvt = uploadUtil.saveFile(newFileName, multipartFile);
			recruiter.setLinkAvt(urlAvt);
			recruiterRepository.save(recruiter);
			avatarDTO.setLinkAvatar(urlAvt);
			return avatarDTO;
		}
		File file = new File(linkAvt);
		String oldFileName = file.getName();
		urlAvt = uploadUtil.updateAvatar(oldFileName, newFileName, multipartFile);
		
		recruiter.setLinkAvt(urlAvt);
		recruiterRepository.save(recruiter);
		avatarDTO.setLinkAvatar(urlAvt);
		return avatarDTO;
	}

}
