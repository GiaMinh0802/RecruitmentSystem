package com.fpt.recruitmentsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fpt.recruitmentsystem.model.CV;
import com.fpt.recruitmentsystem.model.Candidate;

public interface CVRepository extends JpaRepository<CV, Integer> {
    public CV findCVById(Integer id);

    public List<CV> findByCandidate(Candidate candidate);

    public Page<CV> findAll(Specification<CV> spec, Pageable pageable);

}
