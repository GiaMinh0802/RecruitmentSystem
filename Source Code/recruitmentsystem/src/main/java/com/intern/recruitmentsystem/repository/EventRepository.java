package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Event findEventById (int id);

    @Query(value = "SELECT e.id, e.description, e.start_date, e.end_date, e.location, e.name, e.status, e.recruiter_id, e.rule, e.benefit, e.create_at " +
            "FROM event_collectedcandidate ecc " +
            "INNER JOIN event e ON ecc.event_id=e.id " +
            "WHERE ecc.collect_candidate_id=:ccId", nativeQuery = true)
    List<Event> findEventByCollectedCandidate(int ccId);

}
