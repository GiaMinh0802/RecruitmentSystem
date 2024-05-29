package com.fpt.recruitmentsystem.service.implement;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;


import com.fpt.recruitmentsystem.exception.BadRequestException;
import com.fpt.recruitmentsystem.model.*;
import com.fpt.recruitmentsystem.dto.CvDTO;
import com.fpt.recruitmentsystem.exception.ConflictException;
import com.fpt.recruitmentsystem.exception.ForbiddenException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.CvMapper;
import com.fpt.recruitmentsystem.repository.*;
import com.fpt.recruitmentsystem.util.FileUploadUtil;
import com.fpt.recruitmentsystem.util.Message;
import com.fpt.recruitmentsystem.util.Utility;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fpt.recruitmentsystem.model.Account;
import com.fpt.recruitmentsystem.model.CV;
import com.fpt.recruitmentsystem.model.Candidate;
import com.fpt.recruitmentsystem.model.CandidateVacancy;
import com.fpt.recruitmentsystem.service.IAccountService;
import com.fpt.recruitmentsystem.service.ICvService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CvService implements ICvService {
	private final CVRepository cvRepository;
	private final CvMapper cvMapper;
	private final FileUploadUtil fileUploadUtil;
	private final CandidateVacancyRepository candidateVacancyRepository;
	private final InterviewRepository interviewRepository;
	private final IAccountService accountService;
	private final VacancyRepository vacancyRepository;

	public List<CvDTO> getCvByCandidateId(String authorization) {
		Candidate candidate = accountService.getCandidateByAuthorizationHeader(authorization);

		List<CV> cvs = cvRepository.findByCandidate(candidate);

		return cvs.stream().map(cvMapper::mapToDTO).toList();
	}

	@Override
	public CvDTO uploadCv(MultipartFile multipartFile, String authorization, String data) throws IOException {
		Candidate candidate = accountService.getCandidateByAuthorizationHeader(authorization);
		String path = multipartFile.getOriginalFilename();
		if (path == null){
			throw  new BadRequestException(Message.CV_NOT_FOUND);
		}
		String fileName = StringUtils.cleanPath(path);
		String fileUrl = fileUploadUtil.saveFile(fileName, multipartFile);

		CV cv = new CV();
		Date createDate = new Date();
		cv.setFileName(fileName);
		cv.setCandidate(candidate);
		cv.setLinkCV(fileUrl);
		cv.setCreatdDate(createDate);
		cv.setData(data);
		cvRepository.save(cv);
		return cvMapper.mapToDTO(cv);
	}

	@Override
	public void deleteCv(Integer cvId, String authorization) throws IOException {
		Candidate candidate = accountService.getCandidateByAuthorizationHeader(authorization);
		CV cv = cvRepository.findCVById(cvId);
		if (cv == null) {
			throw new NotFoundException(Message.CV_NOT_FOUND);
		}
		Integer candidateIdOfCv = cv.getCandidate().getId();
		if (!Objects.equals(candidate.getId(), candidateIdOfCv)) {
			throw new ForbiddenException("You are not owner");
		}
		CandidateVacancy candidateVacancy = candidateVacancyRepository
				.findByCandidateIdAndCvId(cv.getCandidate().getId(), cv.getId());
		if (candidateVacancy != null) {
			throw new ConflictException("Can not delete cv have apply for vacancy");
		}
		String fileUrl = cv.getLinkCV();
		URL url = new URL(fileUrl);
		String notParamUrl = url.getPath().split("\\?")[0];
		File file = new File(notParamUrl);
		String fileName = file.getName();
		Bucket bucket = StorageClient.getInstance().bucket();
		Blob blob = bucket.get(fileName);

		blob.delete();
		cvRepository.delete(cv);
	}

	@Override
	public CvDTO getOneCv(Integer cvId, String authorization) throws IOException {
		Candidate candidate = accountService.getCandidateByAuthorizationHeader(authorization);
		CV cv = cvRepository.findCVById(cvId);
		if (cv == null) {
			throw new NotFoundException(Message.CV_NOT_FOUND);
		}
		if (!Objects.equals(candidate.getId(), cv.getCandidate().getId())) {
			throw new ForbiddenException("You are not owner");
		}
		return cvMapper.mapToDTO(cv);
	}

	@Override
	public List<CvDTO> search(Integer page, Integer limit, Integer candidateId) {
		Pageable pageable = Utility.getPageable(page, limit);
		Specification<CV> spec = buildSpecification(candidateId);

		Page<CV> cvs = cvRepository.findAll(spec, pageable);

		if (cvs.isEmpty()) {
			throw new NotFoundException(Message.CV_NOT_FOUND);
		}

		return cvs.stream()
				.map(cvMapper::mapToDTO)
				.toList();
	}

	@Override
	public CvDTO getCv(Integer interviewId, String authorizationHeader) {
		Interview interview = interviewRepository.findById(interviewId)
				.orElseThrow(() -> new NotFoundException(Message.INTERVIEW_NOT_FOUND));

		Account account = accountService.getAccountByAuthorizationHeader(authorizationHeader);
		Recruiter recruiter;
		Interviewer interviewer;

		if (account.getRole().getId() == 1) {
			CandidateVacancy candidateVacancy = candidateVacancyRepository.findByCandidateIdAndVacancyId(interview.getCandidateVacancy().getCandidate().getId(), interview.getCandidateVacancy().getVacancy().getId());
			CV cv = cvRepository.findById(candidateVacancy.getCvId())
					.orElseThrow(() -> new NotFoundException(Message.CV_NOT_FOUND));
			return cvMapper.mapToDTO(cv);
		}

		if (interview.getInterviewer() == null) {
			if (account.getRole().getId() == 3) {
				throw new ForbiddenException(Message.ACCESS_DENIED);
			}
			recruiter = account.getRecruiter();
			if (recruiter.getId().equals(interview.getRecruiter().getId())) {
				CandidateVacancy candidateVacancy = candidateVacancyRepository.findByCandidateIdAndVacancyId(interview.getCandidateVacancy().getCandidate().getId(), interview.getCandidateVacancy().getVacancy().getId());
				CV cv = cvRepository.findById(candidateVacancy.getCvId())
						.orElseThrow(() -> new NotFoundException(Message.CV_NOT_FOUND));
				return cvMapper.mapToDTO(cv);
			}
		}

		if(account.getRole().getId() == 2){
			recruiter = account.getRecruiter();
			if (!recruiter.getId().equals(interview.getRecruiter().getId())) {
				throw new ForbiddenException(Message.ACCESS_DENIED);
			}
			CandidateVacancy candidateVacancy = candidateVacancyRepository.findByCandidateIdAndVacancyId(interview.getCandidateVacancy().getCandidate().getId(), interview.getCandidateVacancy().getVacancy().getId());
			CV cv = cvRepository.findById(candidateVacancy.getCvId())
					.orElseThrow(() -> new NotFoundException(Message.CV_NOT_FOUND));
			return cvMapper.mapToDTO(cv);
		}
		interviewer = account.getInterviewer();
		if (!interviewer.getId().equals(interview.getInterviewer().getId())) {
			throw new ForbiddenException(Message.ACCESS_DENIED);
		}
		CandidateVacancy candidateVacancy = candidateVacancyRepository.findByCandidateIdAndVacancyId(interview.getCandidateVacancy().getCandidate().getId(), interview.getCandidateVacancy().getVacancy().getId());
		CV cv = cvRepository.findById(candidateVacancy.getCvId())
				.orElseThrow(() -> new NotFoundException(Message.CV_NOT_FOUND));
		return cvMapper.mapToDTO(cv);
	}


	@Override
	public CvDTO getCvForVacancy(Integer candidateId, Integer vacancyId, String authorizationHeader) {
		Recruiter recruiter = accountService.getRecruiterByAuthorizationHeader(authorizationHeader);
		Vacancy vacancy = vacancyRepository.findRecruiterById(vacancyId);
		if(!vacancy.getRecruiter().getId().equals(recruiter.getId())){
			throw new ForbiddenException(Message.ACCESS_DENIED);
		}
		CandidateVacancy candidateVacancy = candidateVacancyRepository.findByCandidateIdAndVacancyId(candidateId,vacancyId);
		if(candidateVacancy == null){
			throw new BadRequestException(Message.CANDIDATE_NOT_FOUND);
		}
		CV cv = cvRepository.findCVById(candidateVacancy.getCvId());

		return cvMapper.mapToDTO(cv);
	}
	@Override
	public CvDTO getCvByIdCv(Integer cvId) {
		CV cv = cvRepository.findById(cvId)
				.orElseThrow(() -> new NotFoundException(Message.CV_NOT_FOUND));
		return cvMapper.mapToDTO(cv);
	}

	private Specification<CV> buildSpecification(Integer candidateId) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (candidateId != null) {
				predicates.add(criteriaBuilder.equal(root.get("candidate").get("id"), candidateId));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
	@Override
	public CvDTO updateCv(Integer cvId,MultipartFile multipartFile, String authorization, String data) throws IOException {
		Candidate candidate = accountService.getCandidateByAuthorizationHeader(authorization);
		CV cv = cvRepository.findCVById(cvId);
		if(cv.getCandidate() != candidate)
			throw new ConflictException("Candidate cannot update CV");

		String path = multipartFile.getOriginalFilename();
		if (path == null){
			throw  new BadRequestException(Message.CV_NOT_FOUND);
		}
		String fileName = StringUtils.cleanPath(path);
		String fileUrl = fileUploadUtil.saveFile(fileName, multipartFile);
		Date createDate = new Date();
		cv.setFileName(fileName);
		cv.setLinkCV(fileUrl);
		cv.setCreatdDate(createDate);
		cv.setData(data);
		cvRepository.save(cv);
		return cvMapper.mapToDTO(cv);
	}
}
