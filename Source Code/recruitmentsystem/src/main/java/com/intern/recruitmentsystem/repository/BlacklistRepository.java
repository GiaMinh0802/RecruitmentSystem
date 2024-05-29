package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.Blacklist;
import com.fpt.recruitmentsystem.model.Candidate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface BlacklistRepository extends JpaRepository<Blacklist, Integer> {

    List<Blacklist> findAllByCandidateId(Integer candidateId);

    Blacklist findFirstByCandidateIdOrderByStartDateDesc(Integer candidateId);

    @Query("SELECT b.candidate FROM Blacklist b WHERE b.endDate IS NULL OR b.endDate >= :currentDate")
    List<Candidate> findCandidateByEndDateIsNullOrEndDateAfter(Date currentDate);

}
