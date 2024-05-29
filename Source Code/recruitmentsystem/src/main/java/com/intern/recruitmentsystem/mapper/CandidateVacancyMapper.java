package com.fpt.recruitmentsystem.mapper;

import com.fpt.recruitmentsystem.model.Candidate;
import com.fpt.recruitmentsystem.model.CandidateVacancy;

import com.fpt.recruitmentsystem.model.Vacancy;
import com.fpt.recruitmentsystem.dto.CandidateVacancyDTO;
import com.fpt.recruitmentsystem.repository.CandidateVacancyRepository;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CandidateVacancyMapper {
    private final ModelMapper modelMapper;
    private final CandidateMapper candidateMapper;
    private final VacancyMapper vacancyMapper;
    private final CandidateVacancyRepository candidateVacancyRepository;

    public CandidateVacancyDTO mapToDTO(CandidateVacancy candidateVacancy) {
        Integer candidateId = candidateVacancy.getId().getCandidateId();
        Integer vacancyId = candidateVacancy.getId().getVacancyId();
        candidateVacancy = candidateVacancyRepository.findByCandidateIdAndVacancyId(candidateId, vacancyId);
        CandidateVacancyDTO candidateVacancyDTO = modelMapper.map(candidateVacancy, CandidateVacancyDTO.class);
        Candidate candidate = candidateVacancy.getCandidate();
        Vacancy vacancy = candidateVacancy.getVacancy();
        candidateVacancyDTO.setCandidate(candidateMapper.mapToDTO(candidate));
        candidateVacancyDTO.setVacancy(vacancyMapper.mapToDTO(vacancy));
        return candidateVacancyDTO;
    }

    public CandidateVacancy mapToEntity(CandidateVacancyDTO candidateVacancyDTO) {
        return modelMapper.map(candidateVacancyDTO, CandidateVacancy.class);
    }
}
