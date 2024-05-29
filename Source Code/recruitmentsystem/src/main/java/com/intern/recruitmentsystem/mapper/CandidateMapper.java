package com.fpt.recruitmentsystem.mapper;

import com.fpt.recruitmentsystem.dto.*;
import com.fpt.recruitmentsystem.model.Account;
import com.fpt.recruitmentsystem.model.Candidate;
import com.fpt.recruitmentsystem.model.Interview;
import com.fpt.recruitmentsystem.repository.CandidateRepository;

import com.fpt.recruitmentsystem.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CandidateMapper {
    private final CandidateRepository candidateRepository;
    private final ModelMapper modelMapper;
    private final AddressMapper addressMapper;
    private final BlacklistMapper blacklistMapper;
    private final InterviewRepository interviewRepository;
    private final InterviewerMapper interviewerMapper;
    private final RecruiterMapper recruiterMapper;

    public CandidateDTO mapToDTO(Candidate candidate) {
        candidate = candidateRepository.findCandidateById(candidate.getId());
        CandidateDTO candidateDTO = modelMapper.map(candidate, CandidateDTO.class);
        candidateDTO.setEmail(candidate.getAccount().getEmail());
        candidateDTO.setIsActive(candidate.getAccount().isActive());
        if (candidate.getAddress() != null)
            candidateDTO.setAddress(addressMapper.mapToDTO(candidate.getAddress()));
        if (candidate.getBlacklists() != null) {
            Set<BlacklistDTO> blacklistDTOs = candidate.getBlacklists().stream()
                    .map(blacklistMapper::mapToDTO)
                    .collect(Collectors.toSet());
            candidateDTO.setBlacklists(blacklistDTOs);
        }
        return candidateDTO;
    }

    public CandidateInterviewDTO mapToCandidateInterviewDTO(Candidate candidate,Integer id) {
        CandidateInterviewDTO candidateInterviewDTO = new CandidateInterviewDTO();

        CandidateDTO candidateDTO = mapToDTO(candidate);
        candidateInterviewDTO.setCandidate(candidateDTO);

        InterviewCompactDTO interviewCompactDTO = new InterviewCompactDTO();
        Interview interview = interviewRepository.findInterviewByCandidateIdAndVacancyId(candidate.getId(), id);
        if(interview == null){
            candidateInterviewDTO.setInterview(null);
        } else {
            interviewCompactDTO.setInterviewId(interview.getId());
            interviewCompactDTO.setInterviewDatetime(interview.getInterviewDatetime());
            interviewCompactDTO.setVenue(interview.getVenue());
            interviewCompactDTO.setLinkMeet(interview.getLinkMeet());
            interviewCompactDTO.setInterviewerScore(interview.getInterviewerScore());
            interviewCompactDTO.setSoftSkillScore(interview.getSoftSkillScore());
            interviewCompactDTO.setLanguageSkillScore(interview.getLanguageSkillScore());
            interviewCompactDTO.setTotalScore(interview.getTotalScore());
            interviewCompactDTO.setSummary(interview.getSummary());
            interviewCompactDTO.setStatus(interview.getStatus());

            InterviewerDTO interviewerDTO = null;
            if(interview.getInterviewer() != null) {
                interviewerDTO = interviewerMapper.mapToDTO(interview.getInterviewer());
            }
            RecruiterDTO recruiterDTO = null;
            if(interview.getRecruiter() != null){
                recruiterDTO = recruiterMapper.mapToDTO(interview.getRecruiter());
            }

            interviewCompactDTO.setInterviewer(interviewerDTO);
            interviewCompactDTO.setRecruiter(recruiterDTO);


            candidateInterviewDTO.setInterview(interviewCompactDTO);
        }

        return candidateInterviewDTO;
    }

    public Candidate mapToEntity(CandidateDTO candidateDTO) {
        Candidate candidate = modelMapper.map(candidateDTO, Candidate.class);
        // Set the Account and Address entities from the corresponding DTOs
        candidate.setAccount(Account.builder().email(candidateDTO.getEmail()).build());
        if (candidateDTO.getAddress() != null)
            candidate.setAddress(addressMapper.mapToEntity(candidateDTO.getAddress()));
        return candidate;
    }
}
