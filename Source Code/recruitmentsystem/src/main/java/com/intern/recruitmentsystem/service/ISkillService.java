package com.fpt.recruitmentsystem.service;

import java.util.List;

import com.fpt.recruitmentsystem.dto.SkillDTO;

public interface ISkillService {
    List<SkillDTO> getAll();
    SkillDTO insert(SkillDTO newSkill);
    SkillDTO update(int id, SkillDTO updatedSkill);
}
