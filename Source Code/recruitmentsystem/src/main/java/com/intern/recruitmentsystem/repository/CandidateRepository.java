package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.Candidate;
import com.fpt.recruitmentsystem.model.CandidateVacancy;
import com.fpt.recruitmentsystem.model.CandidateVacancyKey;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    Candidate findCandidateById(Integer id);

    Candidate findRecruiterByAccountId(Integer id);

    @Query("select ac.email from Interview i join Candidate c on i.candidateVacancy.candidate.id = c.id " +
            "join Account ac on c.account.id = ac.id" +
            " where i.candidateVacancy.id=:candidateVacancyKey")
    String findEmailCandidateByCandidateVacancy(CandidateVacancyKey candidateVacancyKey);

    @Query(value = "SELECT cv FROM CandidateVacancy cv  WHERE cv.candidate.id = :candidateId")
    public List<CandidateVacancy> findListVacancyByCandidateId(@Param("candidateId") Integer candidateId);

    public Page<Candidate> findAll(Pageable pageable);
    @Query("SELECT c FROM Candidate c " +
            "JOIN c.candidateVacancies cv " +
            "JOIN cv.vacancy v " +
            "JOIN v.recruiter r " +
            "WHERE v.id = :positionId AND r.id = :recruiterId")
    List<Candidate> findAllCandidatesByPositionAndRecruiter(
            @Param("positionId") Integer positionId,
            @Param("recruiterId") Integer recruiterId
    );
}
