package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
    public Skill findSkillById(int id);
    public Skill findSkillByName(String name);
}
