package com.fpt.recruitmentsystem.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.fpt.recruitmentsystem.model.*;
import com.fpt.recruitmentsystem.dto.*;
import com.fpt.recruitmentsystem.repository.InterviewQuesionRepository;
import com.fpt.recruitmentsystem.repository.InterviewerRepository;
import com.fpt.recruitmentsystem.repository.RecruiterRepository;

import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InterviewMapper {
    private final ModelMapper modelMapper;
    private final RecruiterRepository recruiterRepository;
    private final InterviewerRepository interviewerRepository;
    private final InterviewQuesionRepository interviewQuesionRepository;
    private final RecruiterMapper recruiterMapper;
    private final InterviewerMapper interviewerMapper;
    private final InterviewQuestionMapper interviewQuestionMapper;
    private final CandidateVacancyMapper candidateVacancyMapper;

    public InterviewDTO mapToDTO(Interview interview) {
        Recruiter recruiter = recruiterRepository.findRecruiterById(interview.getRecruiter().getId());
        RecruiterDTO recruiterDTO = recruiterMapper.mapToDTO(recruiter);
        InterviewerDTO interviewerDTO = null;
        if (interview.getInterviewer() != null) {
            Interviewer interviewer = interviewerRepository.findInterviewerById(interview.getInterviewer().getId());
            interviewerDTO = interviewerMapper.mapToDTO(interviewer);
        }
        CandidateVacancyDTO candidateVacancyDTO = candidateVacancyMapper.mapToDTO(interview.getCandidateVacancy());
        
        Set<InterviewQuestionDTO> interviewQuestionDTOS = new HashSet<>();
        List<InterviewQuestion> interviewQuestions = interviewQuesionRepository.findAllByInterviewId(interview.getId());
        if (interviewQuestions != null) {
            interviewQuestionDTOS = interviewQuestions.stream()
                    .map(interviewQuestionMapper::mapToDTO)
                    .collect(Collectors.toSet());
        }

        InterviewDTO interviewDTO = modelMapper.map(interview, InterviewDTO.class);
        interviewDTO.setRecruiter(recruiterDTO);
        interviewDTO.setInterviewer(interviewerDTO);
        interviewDTO.setCandidateVacancy(candidateVacancyDTO);
        interviewDTO.setInterviewQuestion(interviewQuestionDTOS);

        return interviewDTO;
    }

    public Interview mapToEntity(InterviewDTO interviewDTO) {
        Interview interview = new Interview();

        CandidateVacancyKey candidateVacancyKey = new CandidateVacancyKey(interviewDTO.getCandidateVacancy().getCandidate().getId(),
                                                                            interviewDTO.getCandidateVacancy().getVacancy().getId());
        CandidateVacancy candidateVacancy = new CandidateVacancy();
        candidateVacancy.setId(candidateVacancyKey);
        candidateVacancy.setInterview(null);
        candidateVacancy.setVacancy(null);
        candidateVacancy.setCandidate(null);

        interview.setCandidateVacancy(candidateVacancy);

        Recruiter recruiter = recruiterMapper.mapToEntity(interviewDTO.getRecruiter());
        interview.setRecruiter(recruiter);

        interview.setInterviewQuestions(null);
        interview.setId(interviewDTO.getId());
        interview.setInterviewDatetime(interviewDTO.getInterviewDatetime());
        interview.setVenue(interviewDTO.getVenue());
        interview.setLinkMeet(interviewDTO.getLinkMeet());
        interview.setStatus(interviewDTO.getStatus());

        return interview;
    }
}
