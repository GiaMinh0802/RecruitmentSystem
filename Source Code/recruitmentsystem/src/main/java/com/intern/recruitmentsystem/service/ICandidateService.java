package com.fpt.recruitmentsystem.service;

import java.util.List;

import com.fpt.recruitmentsystem.dto.CandidateDTO;
import com.fpt.recruitmentsystem.dto.CandidateVacancyDTO;
import com.fpt.recruitmentsystem.dto.VacancyInfoDTO;

public interface ICandidateService {
    CandidateDTO getCandidateById(Integer id);
    CandidateDTO updateTest(CandidateDTO candidateDTO, Integer id);
    CandidateDTO update(CandidateDTO candidateDTO, String authorizationHeader);
    CandidateVacancyDTO applyVacancy(CandidateVacancyDTO candidateVacancyDTO, String authorization);
    CandidateDTO getCandidate(String authorizationHeader);
    List<VacancyInfoDTO> getListVacancyByCandidate(String authorizationHeader);
    List<CandidateDTO> search(Integer page, Integer limit);
    List<CandidateDTO> getCandidateByPosition(String authorizationHeader, int position);
}

