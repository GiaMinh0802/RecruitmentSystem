package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelRepository extends JpaRepository<Level, Integer> {
    public Level findLevelById(int id);
    public Level findLevelByName(String name);
}
