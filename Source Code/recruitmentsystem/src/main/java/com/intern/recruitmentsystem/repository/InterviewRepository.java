package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.Interview;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Integer> {

    @Query("select i from Interview i join Recruiter r on i.recruiter.id = r.id where i.id=:interviewId and r.id=:recruiterId")
    public Optional<Interview> findInterviewByRecruiterIdAndInterviewId(int interviewId, int recruiterId);
    @Query("select i, c from Interview i " +
            "join Candidate c ON i.candidateVacancy.candidate.id=c.id " +
            "where i.interviewer.id=:interviewerId")
    public List<Interview> findInterviewsByInterviewerId(int interviewerId);
    public List<Interview> findByRecruiterId(Integer recruiterId);
    @Query("select i from Interview i where i.candidateVacancy.vacancy.id =:vacancyId and i.candidateVacancy.candidate.id =:candidateId")
    public Interview findInterviewByCandidateIdAndVacancyId(Integer candidateId,Integer vacancyId);

    List<Interview> findAll(Specification<Interview> interviewSpecification, Pageable pageable);
    @Query("SELECT i FROM Interview i " +
            "JOIN i.candidateVacancy cv " +
            "JOIN cv.vacancy v " +
            "WHERE i.recruiter.id = :recruiterId " +
            "AND v.position.id = :positionId")
    List<Interview> findInterviewsByRecruiterAndPosition(int  recruiterId,  int positionId);
    public List<Interview> findByCandidateVacancyCandidateId(Integer candidateId);
}
