package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    public Position findPositionById(int id);
    public Position findPositionByName(String name);
}
