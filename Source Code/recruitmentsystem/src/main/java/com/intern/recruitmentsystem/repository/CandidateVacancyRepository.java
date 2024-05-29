package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.CandidateVacancy;
import com.fpt.recruitmentsystem.model.CandidateVacancyKey;
import com.fpt.recruitmentsystem.dto.CandidateVacancyDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CandidateVacancyRepository extends JpaRepository<CandidateVacancy, CandidateVacancyKey> {
    @Query(value = "select new com.fpt.recruitmentsystem.dto.CandidateVacancyDTO(v.candidateVacancy.candidate.id, v.candidateVacancy.vacancy.id, v.candidateVacancy.applyDate, v.candidateVacancy.status, v.candidateVacancy.cvId) from Interview v where v.id=:id")
    public CandidateVacancyDTO findCandidateVacancy (int id);
    public CandidateVacancy findByCandidateIdAndVacancyId(Integer candidateId, Integer vacancyId);
    public CandidateVacancy findByCandidateIdAndCvId(Integer candidateId, Integer cvId);
}
