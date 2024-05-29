package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.CollectedCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollectedCandidateRepository extends JpaRepository<CollectedCandidate, Integer> {
    CollectedCandidate findCollectedCandidateById(int id);
    @Query(value = "SELECT cc.id, cc.birthday, cc.email, cc.first_name, cc.last_name, cc.phone_number, cc.sex " +
            "FROM event_collectedcandidate ecc " +
            "JOIN collectedcandidate cc ON ecc.collect_candidate_id = cc.id " +
            "WHERE ecc.event_id = :eventId", nativeQuery = true)
    List<CollectedCandidate> findCollectedCandidateByEvent(int eventId);
    CollectedCandidate findCollectedCandidateByEmail(String email);
}
