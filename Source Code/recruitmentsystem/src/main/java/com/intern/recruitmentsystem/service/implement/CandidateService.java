package com.fpt.recruitmentsystem.service.implement;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import com.fpt.recruitmentsystem.model.*;
import com.fpt.recruitmentsystem.service.*;
import com.fpt.recruitmentsystem.util.Message;
import com.fpt.recruitmentsystem.util.Utility;
import com.fpt.recruitmentsystem.dto.*;
import com.fpt.recruitmentsystem.exception.ConflictException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.CandidateMapper;
import com.fpt.recruitmentsystem.mapper.CandidateVacancyMapper;
import com.fpt.recruitmentsystem.mapper.VacancyMapper;
import com.fpt.recruitmentsystem.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpt.recruitmentsystem.model.CV;
import com.fpt.recruitmentsystem.model.Candidate;
import com.fpt.recruitmentsystem.model.CandidateVacancy;
import com.fpt.recruitmentsystem.model.CandidateVacancyKey;
import com.fpt.recruitmentsystem.model.Vacancy;


@Service
@RequiredArgsConstructor
public class CandidateService implements ICandidateService {
	private final CandidateRepository candidateRepository;
	private final CandidateMapper candidateMapper;
	private final VacancyRepository vacancyRepository;
	private final CandidateVacancyMapper candidateVacancyMapper;
	private final CandidateVacancyRepository candidateVacancyRepository;
	private final IAddressService addressService;
	private final IAccountService accountService;
	private final CVRepository cvRepository;
	private final VacancyMapper vacancyMapper;
	private final InterviewRepository interviewRepository;
	private final IBlacklistService blacklistService;
	private final IMailService mailService;

	@Override
	public CandidateDTO getCandidate(String authorizationHeader) {
		Candidate candidate = accountService.getCandidateByAuthorizationHeader(authorizationHeader);
		return candidateMapper.mapToDTO(candidate);
	}

	public List<CandidateDTO> getAll() {
		List<Candidate> candidates = candidateRepository.findAll();
		if (candidates.isEmpty()) {
			throw new NotFoundException(Message.CANDIDATE_NOT_FOUND);
		}
		return candidates.stream().
				map(candidateMapper::mapToDTO).
				toList();
	}
	public CandidateDTO getCandidateById(Integer id) {
		Candidate candidate = candidateRepository.findCandidateById(id);
		if (candidate == null) {
			throw new NotFoundException(Message.CANDIDATE_NOT_FOUND);
		}
		return candidateMapper.mapToDTO(candidate);
	}
	public CandidateDTO updateTest(CandidateDTO candidateDTO, Integer id) {
		Candidate candidateUpdate = candidateRepository.findCandidateById(id);
		if (candidateUpdate == null) {
			throw new NotFoundException(Message.CANDIDATE_NOT_FOUND);
		}
		Candidate candidateToEntity = candidateMapper.mapToEntity(candidateDTO);

		candidateUpdate.setBirthday(candidateToEntity.getBirthday());
		candidateUpdate.setFirstName(candidateToEntity.getFirstName());
		candidateUpdate.setLastName(candidateToEntity.getLastName());
		candidateUpdate.setLinkAvt(candidateToEntity.getLinkAvt());
		candidateUpdate.setPhoneNumber(candidateToEntity.getPhoneNumber());
		candidateUpdate.setSex(candidateToEntity.getSex());

		Candidate candidateNew = candidateRepository.save(candidateUpdate);
		return candidateMapper.mapToDTO(candidateNew);
	}

    public CandidateDTO update(CandidateDTO candidateDTO, String authorization) {
        Candidate existingCandidate = accountService.getCandidateByAuthorizationHeader(authorization);

		// Copy properties from candidateDTO to existingCandidate EXCEPT for id
		BeanUtils.copyProperties(candidateDTO, existingCandidate, "id");

        AddressDTO candidateAddressdDto = candidateDTO.getAddress();
        Address newCandidateAddress = (candidateAddressdDto != null) ? addressService.findOrInsert(candidateAddressdDto) : null;

        existingCandidate.setAddress(newCandidateAddress);
        Candidate updatedCandidate = candidateRepository.save(existingCandidate);

        return candidateMapper.mapToDTO(updatedCandidate);
    }

	public List<CandidateDTO> findPaginated (int pageNo, int pageSize){
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		Page<Candidate> page = candidateRepository.findAll(pageable);
		List<Candidate> candidateList = page.getContent();
		if (candidateList.isEmpty()){
			throw new NotFoundException(Message.VACANCY_NOT_FOUND);
		}
		return candidateList.stream().
				map(candidateMapper::mapToDTO).
				toList();
	}

	public CandidateVacancyDTO applyVacancy(CandidateVacancyDTO candidateVacancyDTO, String authorization) {
		Date applyDate = new Date(System.currentTimeMillis());
		Integer vacancyId = candidateVacancyDTO.getVacancy().getId();
		Integer cvId = candidateVacancyDTO.getCvId();

		Candidate candidate = accountService.getCandidateByAuthorizationHeader(authorization);
		boolean isCandidateInBlacklist = blacklistService.isCandidateAlreadyInBlacklist(candidate.getId());
		if (isCandidateInBlacklist) {
			throw new ConflictException("Candidate is in the BlackList");
		}

		Vacancy vacancy = vacancyRepository.findById(vacancyId)
				.orElseThrow(() -> new NotFoundException(Message.VACANCY_NOT_FOUND));
		CV cv = cvRepository.findCVById(cvId);
		if (cv == null)
			throw new NotFoundException(Message.CV_NOT_FOUND);

		// check if this candidate applied for this vacancy yet
		CandidateVacancy existingCandidateVacancy = candidateVacancyRepository.findByCandidateIdAndVacancyId(candidate.getId(), vacancyId);
		if (existingCandidateVacancy != null) {
			throw new ConflictException("Candidate cannot apply for a vacancy twice");
		}

		CandidateVacancyKey candidateVacancyKey = new CandidateVacancyKey(candidate.getId(), vacancyId);

		CandidateVacancy newCandidateVacancy = CandidateVacancy.builder().
				id(candidateVacancyKey).
				candidate(candidate).
				vacancy(vacancy).
				status("PENDING").
				cvId(cvId).
				applyDate(applyDate).build();
		CandidateVacancy savedCandidateVacancy = candidateVacancyRepository.save(newCandidateVacancy);

		sendEmailForCandidate(vacancy, candidateVacancyDTO.getCandidate().getId());
		return candidateVacancyMapper.mapToDTO(savedCandidateVacancy);
	}

	private void sendEmailForCandidate(Vacancy vacancy, Integer candidateId){
		Candidate candidate = candidateRepository.findCandidateById(candidateId);
		CandidateDTO candidateDTO = candidateMapper.mapToDTO(candidate);
		mailService.sendApplyVacancySuccess(vacancy, candidateDTO);
	}

	@Override
	public List<VacancyInfoDTO> getListVacancyByCandidate(String authorizationHeader) {
		Candidate candidateAuthor = accountService.getCandidateByAuthorizationHeader(authorizationHeader);
		List<CandidateVacancy> candidateVacancies = candidateRepository.findListVacancyByCandidateId(candidateAuthor.getId());
		if (candidateVacancies.isEmpty()) {
			throw new NotFoundException(Message.CANDIDATE_NOT_FOUND);
		}
		List<VacancyInfoDTO> vacancyInfoDTOs = new ArrayList<>();
		for (CandidateVacancy candidateVacancy : candidateVacancies) {
			Vacancy vacancy = candidateVacancy.getVacancy();
			CV cv = cvRepository.findCVById(candidateVacancy.getCvId());
			Interview interview = interviewRepository.findInterviewByCandidateIdAndVacancyId(
					candidateVacancy.getCandidate().getId(), // Pass the candidate ID
					candidateVacancy.getVacancy().getId()    // Pass the vacancy ID
			);
			VacancyInfoDTO vacancyInfoDTO = vacancyMapper.mapToVacancyInfoDTO(vacancy,candidateVacancy, cv, interview);
			vacancyInfoDTOs.add(vacancyInfoDTO);
		}
		return vacancyInfoDTOs;
	}
	public List<CandidateDTO> search(Integer page, Integer limit) {
        Pageable pageable = Utility.getPageable(page, limit);

		Page<Candidate> candidates = candidateRepository.findAll(pageable);

		if (candidates.isEmpty())
			throw new NotFoundException(Message.CANDIDATE_NOT_FOUND);

		return candidates.stream().
			map(candidateMapper::mapToDTO).
			toList();
	}

	@Override
	public List<CandidateDTO> getCandidateByPosition(String authorizationHeader, int position) {
		Recruiter recruiter = accountService.getRecruiterByAuthorizationHeader(authorizationHeader);
		List<Candidate> candidateList=candidateRepository.findAllCandidatesByPositionAndRecruiter(position, recruiter.getId());
		if (candidateList.isEmpty())
			throw new NotFoundException(Message.CANDIDATE_NOT_FOUND);

		return candidateList.stream().
				map(candidateMapper::mapToDTO).
				toList();
	}

}
