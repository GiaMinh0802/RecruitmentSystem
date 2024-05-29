package com.fpt.recruitmentsystem.service.implement;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fpt.recruitmentsystem.model.Skill;
import com.fpt.recruitmentsystem.service.ISkillService;
import com.fpt.recruitmentsystem.dto.SkillDTO;
import com.fpt.recruitmentsystem.exception.ConflictException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.SkillMapper;
import com.fpt.recruitmentsystem.repository.SkillRepository;
import com.fpt.recruitmentsystem.util.Message;
import com.fpt.recruitmentsystem.util.Utility;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SkillService implements ISkillService {
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    public List<SkillDTO> getAll() {
        List<Skill> skills = skillRepository.findAll();
        if (skills.isEmpty()) {
            throw new NotFoundException(Message.SKILL_NOT_FOUND);
        }
        return skills.stream()
                .map(skillMapper::mapToDTO)
                .toList();
    }

    public SkillDTO insert(SkillDTO newSkill) {
        String trimmedName = Utility.trimString(newSkill.getName());
        Skill existingSkill = skillRepository.findSkillByName(trimmedName);
        if (existingSkill != null) {
            throw new ConflictException("Skill name already exists");
        }

        Skill skill = skillMapper.mapToEntity(newSkill);
        skill.setName(trimmedName); // Set the trimmed name before saving
        Skill savedSkill = skillRepository.save(skill);
        return skillMapper.mapToDTO(savedSkill);
    }

    public SkillDTO update(int id, SkillDTO updatedSkill) {
        Skill skill = skillRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(Message.SKILL_NOT_FOUND));

        String trimmedName = Utility.trimString(updatedSkill.getName());
        Skill existingSkill = skillRepository.findSkillByName(trimmedName);
        if (existingSkill != null && existingSkill.getId() != id) {
            throw new ConflictException("Skill name already exists");
        }

        skill.setName(trimmedName); // Set the trimmed name before updating
        Skill updated = skillRepository.save(skill);
        return skillMapper.mapToDTO(updated);
    }
}
